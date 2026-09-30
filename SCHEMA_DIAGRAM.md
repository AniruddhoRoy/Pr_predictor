# Relational Schema Diagram

This schema is derived from ER_DIAGRAM.md. It is the recommended logical database design for the backend that will serve the final JavaFX frontend. It is database-agnostic SQL terminology; the backend team can map it to PostgreSQL, MySQL, or another relational database.

## Schema diagram

~~~mermaid
flowchart LR
    users["users<br/>PK user_id<br/>UQ username<br/>UQ email<br/>password_hash<br/>full_name<br/>role<br/>github_profile_url<br/>created_at<br/>updated_at"]
    settings["user_settings<br/>PK/FK user_id<br/>theme_mode<br/>notifications_enabled<br/>default_prediction_type<br/>default_input_mode<br/>updated_at"]
    plans["plans<br/>PK plan_id<br/>UQ code<br/>name<br/>monthly_prediction_limit<br/>description<br/>active"]
    subscriptions["subscriptions<br/>PK subscription_id<br/>FK user_id<br/>FK plan_id<br/>status<br/>started_at<br/>ended_at<br/>provider_reference"]
    usage["usage_periods<br/>PK usage_period_id<br/>FK user_id<br/>period_start<br/>period_end<br/>predictions_used<br/>limit_snapshot"]
    repos["repositories<br/>PK repository_id<br/>provider<br/>owner<br/>name<br/>canonical_url"]
    prs["pull_requests<br/>PK pull_request_id<br/>FK repository_id<br/>number<br/>UQ url<br/>title<br/>state<br/>author_login<br/>fetched_at"]
    predictions["predictions<br/>PK prediction_id<br/>FK user_id<br/>FK usage_period_id<br/>FK pull_request_id NULL<br/>prediction_type<br/>status<br/>model_version<br/>created_at<br/>completed_at<br/>error_message"]
    inputs["prediction_inputs<br/>PK/FK prediction_id<br/>input_mode<br/>github_url NULL<br/>raw_feature_text NULL<br/>submitted_at"]
    features["prediction_features<br/>PK feature_id<br/>FK prediction_id<br/>feature_name<br/>value_type<br/>value_text NULL<br/>value_number NULL<br/>value_boolean NULL<br/>source"]
    results["prediction_results<br/>PK result_id<br/>UQ/FK prediction_id<br/>merge_probability<br/>quality_score<br/>quality_label<br/>recommendation<br/>generated_at"]
    factors["prediction_factors<br/>PK factor_id<br/>FK result_id<br/>factor_name<br/>description<br/>impact<br/>display_order"]

    users -->|user_id| settings
    users -->|user_id| subscriptions
    plans -->|plan_id| subscriptions
    users -->|user_id| usage
    usage -->|usage_period_id| predictions
    users -->|user_id| predictions
    repos -->|repository_id| prs
    prs -. "optional pull_request_id" .-> predictions
    predictions -->|prediction_id| inputs
    predictions -->|prediction_id| features
    predictions -->|prediction_id| results
    results -->|result_id| factors
~~~

Legend: PK = primary key, FK = foreign key, UQ = unique constraint, NULL = nullable column.

## Table specification

### users

| Column | Type | Constraints | Purpose |
|---|---|---|---|
| user_id | UUID | PK | Stable user identifier. |
| username | VARCHAR(80) | NOT NULL, UNIQUE | Login name. |
| email | VARCHAR(255) | NOT NULL, UNIQUE | Account email. |
| password_hash | VARCHAR(255) | NOT NULL | Password hash produced by the backend's password library. |
| full_name | VARCHAR(150) | NOT NULL | Profile display name. |
| role | VARCHAR(50) | NOT NULL | User role, such as developer. |
| github_profile_url | TEXT | NULL | Optional GitHub profile shown on the profile screen. |
| created_at / updated_at | TIMESTAMPTZ | NOT NULL | Audit timestamps. |

### user_settings

| Column | Type | Constraints | Purpose |
|---|---|---|---|
| user_id | UUID | PK, FK → users.user_id | Makes this a one-to-one extension of the user. |
| theme_mode | VARCHAR(10) | NOT NULL, default LIGHT | Matches THEAME.Mode. |
| notifications_enabled | BOOLEAN | NOT NULL, default TRUE | Settings checkbox. |
| default_prediction_type | VARCHAR(30) | NOT NULL | MERGE_PROBABILITY, PR_QUALITY, or BOTH. |
| default_input_mode | VARCHAR(30) | NOT NULL | GITHUB_URL or MANUAL_FEATURES. |
| updated_at | TIMESTAMPTZ | NOT NULL | Settings audit timestamp. |

### plans and subscriptions

plans contains reusable product plans. monthly_prediction_limit is NULL for an unlimited plan such as Premium.

subscriptions records a user's plan history. Recommended statuses are ACTIVE, CANCELLED, EXPIRED, and TRIAL. Add a partial unique index for one active subscription per user.

| Table | Important columns |
|---|---|
| plans | plan_id PK, code UNIQUE, name, monthly_prediction_limit NULLABLE, description, active. |
| subscriptions | subscription_id PK, user_id FK, plan_id FK, status, started_at, ended_at NULLABLE, provider_reference NULLABLE, created_at. |

### usage_periods

| Column | Type | Constraints | Purpose |
|---|---|---|---|
| usage_period_id | UUID | PK | Quota window identifier. |
| user_id | UUID | FK → users.user_id | Owner of the quota window. |
| period_start / period_end | DATE | NOT NULL | Monthly period boundaries. |
| predictions_used | INTEGER | NOT NULL, default 0, non-negative | Count displayed on the dashboard. |
| limit_snapshot | INTEGER | NULLABLE, non-negative | Limit at the time of the period; NULL means unlimited. |

Add UNIQUE(user_id, period_start) so one user cannot have duplicate monthly periods.

### repositories and pull_requests

| Table | Important columns |
|---|---|
| repositories | repository_id PK, provider, owner, name, canonical_url, created_at; unique (provider, owner, name). |
| pull_requests | pull_request_id PK, repository_id FK, number, url UNIQUE, title, state, author_login, fetched_at, updated_at; unique (repository_id, number). |

These rows are populated by the backend's GitHub integration. The JavaFX client only submits a URL; it does not own GitHub crawling.

### predictions

| Column | Type | Constraints | Purpose |
|---|---|---|---|
| prediction_id | UUID | PK | One submitted analysis request. |
| user_id | UUID | FK → users.user_id | Authenticated owner. |
| usage_period_id | UUID | FK → usage_periods.usage_period_id | Quota period charged by the request. |
| pull_request_id | UUID | NULLABLE FK → pull_requests.pull_request_id | Set for a GitHub-backed request; null for manual-only input. |
| prediction_type | VARCHAR(30) | NOT NULL | Requested output: MERGE_PROBABILITY, PR_QUALITY, or BOTH. |
| status | VARCHAR(20) | NOT NULL | PENDING, PROCESSING, COMPLETED, or FAILED. |
| model_version | VARCHAR(80) | NULLABLE | Model used for the result, if known at request time. |
| created_at / completed_at | TIMESTAMPTZ | NOT NULL / NULLABLE | Lifecycle timestamps. |
| error_message | TEXT | NULLABLE | Safe failure detail for failed requests. |

### prediction_inputs and prediction_features

prediction_inputs preserves what the user submitted. It uses prediction_id as both its primary key and foreign key because each prediction has exactly one original input record.

| Table | Important columns |
|---|---|
| prediction_inputs | prediction_id PK/FK, input_mode, github_url NULLABLE, raw_feature_text NULLABLE, submitted_at. |
| prediction_features | feature_id PK, prediction_id FK, feature_name, value_type, typed value columns, source, created_at; unique (prediction_id, feature_name). |

Use a check constraint on prediction_inputs:

~~~text
GITHUB_URL      => github_url is present and raw_feature_text is null
MANUAL_FEATURES => raw_feature_text is present and github_url is null
~~~

For prediction_features, value_type controls which one of value_text, value_number, and value_boolean may be populated. This keeps model inputs auditable while allowing feature names to evolve.

### prediction_results and prediction_factors

| Table | Important columns |
|---|---|
| prediction_results | result_id PK, prediction_id UNIQUE FK, merge_probability DECIMAL(5,2) in 0..100, quality_score DECIMAL(5,2) in 0..100, quality_label, recommendation, generated_at. |
| prediction_factors | factor_id PK, result_id FK, factor_name, description, impact, display_order; index (result_id, display_order). |

One failed or pending prediction has no result row. A completed prediction has exactly one result row, which may contain one or both output values depending on prediction_type.

## Backend query views

The frontend's current screens map to these query patterns:

| Screen | Backend query |
|---|---|
| Dashboard | Aggregate predictions and usage_periods for the authenticated user, plus recent prediction_results. |
| New Prediction | Create predictions, prediction_inputs, and feature rows; then create a result asynchronously or synchronously. |
| Prediction Result | Fetch one predictions row with its input, result, and ordered factors. |
| History | Search and paginate predictions joined to pull_requests and prediction_results. |
| Profile | Read/update users. |
| Settings | Read/update user_settings. |
| Subscription | Read active subscriptions and its plans; start changes through a billing service. |

