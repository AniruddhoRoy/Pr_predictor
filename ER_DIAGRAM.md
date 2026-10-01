# Entity Relationship Diagram

This is the proposed backend domain model for the final JavaFX frontend in this repository. The current Java code does not create these tables yet; the diagram is the design to use before implementing the backend.

The model covers the data required by the existing screens:

- Login and profile data from LoginScreen and ProfileScreen.
- Appearance and prediction preferences from SettingsScreen.
- Free and Premium plans from SubscriptionScreen and the dashboard subscription card.
- Monthly usage shown by UserDashboard.
- GitHub repositories and pull requests submitted to NewPredictionScreen.
- Manual feature input and backend-extracted features.
- Prediction lifecycle, results, quality, recommendations, and explanatory factors.
- Available prediction models and the model version used for each prediction.
- History and recent predictions, which are read from prediction records.

## Basic

An ER diagram describes how records in two entities are related. The symbols beside each entity show its cardinality, or how many records can participate in the relationship.

### Common relationship types

| Relationship | Meaning | Example |
|---|---|---|
| One-to-one (1:1) | One record in entity A is related to exactly one record in entity B, and vice versa. | One user has one settings row. |
| One-to-many (1:N) | One record in entity A can be related to many records in entity B. Each B record belongs to one A record. | One repository contains many pull requests. |
| Many-to-one (N:1) | Many records in entity A relate to one record in entity B. This is the same relationship as one-to-many viewed from the opposite side. | Many predictions belong to one user. |
| Many-to-many (M:N) | Many records in entity A can relate to many records in entity B. A junction table is required to store the links. | Many users can belong to many teams through a user_team table. |

~~~mermaid
erDiagram
    USER ||--|| USER_SETTINGS : "one-to-one"
    REPOSITORY ||--o{ PULL_REQUEST : "one-to-many"
    PREDICTION }o--|| USER_ACCOUNT : "many-to-one"
    USER_MEMBER }o--o{ TEAM : "many-to-many"
~~~

### Crow's-foot notation used here

| Symbol | Meaning |
|---|---|
| `||` | Exactly one |
| `o|` | Zero or one |
| `|{` | One or many |
| `o{` | Zero or many |

For example, `REPOSITORIES ||--o{ PULL_REQUESTS` means one repository can contain zero or many pull requests, while each pull request belongs to one repository. A many-to-many relationship is normally implemented with a separate junction table containing foreign keys to both entities.

## Conceptual ER diagram

~~~mermaid
erDiagram
    USERS ||--|| USER_SETTINGS : "has"
    USERS ||--o{ SUBSCRIPTIONS : "owns"
    PLANS ||--o{ SUBSCRIPTIONS : "defines"
    USERS ||--o{ USAGE_PERIODS : "has monthly usage"
    MODELS ||--o{ PREDICTIONS : "runs"
    USAGE_PERIODS ||--o{ PREDICTIONS : "counts"
    USERS ||--o{ PREDICTIONS : "submits"
    REPOSITORIES ||--o{ PULL_REQUESTS : "contains"
    PULL_REQUESTS o|--o{ PREDICTIONS : "can be analyzed by"
    PREDICTIONS ||--|| PREDICTION_INPUTS : "has"
    PREDICTIONS ||--o{ PREDICTION_FEATURES : "has derived features"
    PREDICTIONS ||--o| PREDICTION_RESULTS : "produces"
    PREDICTION_RESULTS ||--o{ PREDICTION_FACTORS : "explains"

    USERS {
        uuid user_id PK
        string username UK
        string email UK
        string password_hash
        string full_name
        string role
        string github_profile_url
        datetime created_at
        datetime updated_at
    }

    USER_SETTINGS {
        uuid user_id PK
        string theme_mode
        boolean notifications_enabled
        string default_prediction_type
        string default_input_mode
        datetime updated_at
    }

    PLANS {
        uuid plan_id PK
        string code UK
        string name
        integer monthly_prediction_limit
        string description
        boolean active
    }

    MODELS {
        uuid model_id PK
        string code UK
        string name
        string task_type
        string version
        string provider
        string description
        boolean active
        datetime created_at
        datetime retired_at
    }

    SUBSCRIPTIONS {
        uuid subscription_id PK
        uuid user_id FK
        uuid plan_id FK
        string status
        datetime started_at
        datetime ended_at
        string provider_reference
        datetime created_at
    }

    USAGE_PERIODS {
        uuid usage_period_id PK
        uuid user_id FK
        date period_start
        date period_end
        integer predictions_used
        integer limit_snapshot
    }

    REPOSITORIES {
        uuid repository_id PK
        string provider
        string owner
        string name
        string canonical_url
        datetime created_at
    }

    PULL_REQUESTS {
        uuid pull_request_id PK
        uuid repository_id FK
        integer number
        string url UK
        string title
        string state
        string author_login
        datetime fetched_at
        datetime updated_at
    }

    PREDICTIONS {
        uuid prediction_id PK
        uuid user_id FK
        uuid usage_period_id FK
        uuid model_id FK
        uuid pull_request_id FK
        string prediction_type
        string status
        string model_version
        datetime created_at
        datetime completed_at
        string error_message
    }

    PREDICTION_INPUTS {
        uuid prediction_id PK
        string input_mode
        string github_url
        text raw_feature_text
        datetime submitted_at
    }

    PREDICTION_FEATURES {
        uuid feature_id PK
        uuid prediction_id FK
        string feature_name
        string value_type
        string value_text
        decimal value_number
        boolean value_boolean
        string source
        datetime created_at
    }

    PREDICTION_RESULTS {
        uuid result_id PK
        uuid prediction_id FK
        decimal merge_probability
        decimal quality_score
        string quality_label
        text recommendation
        datetime generated_at
    }

    PREDICTION_FACTORS {
        uuid factor_id PK
        uuid result_id FK
        string factor_name
        text description
        string impact
        integer display_order
    }
~~~

## Relationship rules

| Relationship | Meaning |
|---|---|
| USERS → USER_SETTINGS | Each user has one settings row. Settings are separated from identity data so theme and prediction preferences can change independently. |
| USERS → SUBSCRIPTIONS → PLANS | A user can have subscription history; each subscription points to a plan. Only one subscription should be active for a user at a time. |
| USERS → USAGE_PERIODS | One row represents one user's quota window, normally one calendar month. limit_snapshot preserves the limit that applied during that month. |
| MODELS → PREDICTIONS | A model catalog entry can be used by many predictions. A prediction records model_id and a version snapshot so results remain reproducible after a model is retired. |
| USERS → PREDICTIONS | Every prediction belongs to the authenticated user who submitted it. |
| REPOSITORIES → PULL_REQUESTS | A repository contains many pull requests. The unique (repository_id, number) pair prevents the same pull request from being stored twice. |
| PULL_REQUESTS → PREDICTIONS | A GitHub-backed prediction can point to a stored pull request. pull_request_id is nullable for manual-feature predictions. |
| PREDICTIONS → PREDICTION_INPUTS | One prediction has exactly one original input record. The input mode determines whether github_url or raw_feature_text is populated. |
| PREDICTIONS → PREDICTION_FEATURES | A URL prediction receives features extracted by the backend; a manual prediction receives features parsed from the submitted text. A prediction can have zero or many feature rows. |
| PREDICTIONS → PREDICTION_RESULTS | A prediction can be pending/failed without a result. A successful prediction has one result row. |
| PREDICTION_RESULTS → PREDICTION_FACTORS | Result factors explain the model output and preserve the order shown in the result screen. |

## Modeling decisions

- HistoryScreen does not need a separate history table. It can query PREDICTIONS joined to PULL_REQUESTS and PREDICTION_RESULTS, ordered by created_at.
- Passwords are stored only as password_hash; plaintext passwords must never be persisted.
- PREDICTION_FEATURES uses typed value columns so numeric and boolean model inputs remain queryable. Exactly one value column should be populated according to value_type.
- MODELS is the source of truth for available models. Only active models should be returned by the model-list API; old model rows remain available for prediction history and reproducibility.
- A manual input may not have a PULL_REQUESTS row. A URL input may initially have no pull request row while GitHub data is being fetched, then be linked after retrieval.
- An active plan's unlimited quota is represented by a NULL monthly_prediction_limit; the backend still records usage for reporting.
- Authentication sessions or JWT refresh tokens are intentionally not part of this relational model. They may be managed by a token store, identity provider, or a separate session table when the backend contract is finalized.
