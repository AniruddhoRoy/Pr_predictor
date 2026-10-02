# Backend API Integration Tasks

This task list is based on the API reference at:

R:\BOOKS\L3-S1\CSE 302-Software Development Sessional\project\pr_predictor_backend_v2\api_docs.md

Base URL: http://127.0.0.1:8000

The private admin/browser console API is intentionally excluded. It is reserved for the web/admin project and must not be added to this JavaFX client.

## 1. Shared API foundation

Add or extend the following files first:

| File | Work |
|---|---|
| src/main/java/com/aniruddho_roy/delete/delete/Backend/Base.java | Add GET, POST, PATCH and DELETE methods; configurable base URL; JSON request/response handling; bearer-token support; status/error mapping; and background-friendly execution. |
| src/main/java/com/aniruddho_roy/delete/delete/additional/URLS.java | Add constants for every endpoint listed below. Keep endpoint paths in one place. |
| src/main/java/com/aniruddho_roy/delete/delete/Storage/APPLICATION_STORE.java | Store the access token, token type, current user, subscription, selected model and default settings. Clear authentication data on logout. Never store a password. |
| src/main/java/com/aniruddho_roy/delete/delete/Backend/dto/ | Add typed DTOs/records for users, authentication responses, settings, models, subscriptions, predictions, repositories, pull requests, factors, dashboard data and API errors. |
| pom.xml | Add a JSON library with Java time support, such as Jackson databind plus the Java time module. |
| module-info.java | Export/open the DTO package as required by the selected JSON library and add its module requirements. |

Use camelCase JSON names. Protected calls must send:

~~~text
Authorization: Bearer <accessToken>
~~~

The API error body is:

~~~json
{
  "detail": "Human-readable error message"
}
~~~

## 2. Public authentication and service endpoints

| API endpoint | Add in | Connect to | Required work |
|---|---|---|---|
| GET / | Backend/Auth.java or a small Backend/Health.java | Optional startup/connectivity check | Confirm the service is reachable. Ignore any admin/private information returned by the response. |
| GET /health | Backend/Health.java | Optional startup/connectivity check | Show a service-unavailable message when the backend cannot be reached. |
| POST /register | Backend/Auth.java | RegisterScreen.java | Send username, email, password, fullName and optional githubProfileUrl. Do not send a role. On success store the returned token/user and navigate to the dashboard. Handle duplicate username/email (409) and validation errors (422). |
| POST /login | Backend/Auth.java | LoginScreen.java | Accept username or email plus password. Store accessToken, tokenType and user from the response, then navigate to the dashboard. Handle invalid credentials (401). |

Registration request:

~~~json
{
  "username": "user_name",
  "email": "user@example.com",
  "password": "strong-password",
  "fullName": "Full Name",
  "githubProfileUrl": "https://github.com/user_name"
}
~~~

Login request:

~~~json
{
  "usernameOrEmail": "user@example.com",
  "password": "strong-password"
}
~~~

## 3. User profile and settings

| API endpoint | Add in | Connect to | Required work |
|---|---|---|---|
| GET /me | Backend/Profile.java | ProfileScreen.java and APPLICATION_STORE | Load the authenticated user and make it the canonical current-user object. |
| GET /profile | Backend/Profile.java | Compatibility fallback only | Support the API alias if needed; use one canonical client method. |
| PATCH /profile | Backend/Profile.java | ProfileScreen.java Save Changes action | Send only changed fullName, email and githubProfileUrl values. Handle duplicate email (409). |
| GET /settings | Backend/Settings.java | SettingsScreen.java | Load themeMode, notificationsEnabled, defaultPredictionType and defaultInputMode when the screen opens. |
| PATCH /settings | Backend/Settings.java | SettingsScreen.java Save action | Send changed settings only. Validate LIGHT/DARK, MERGE_PROBABILITY/PR_QUALITY/BOTH and GITHUB_URL/MANUAL_FEATURES. |

Profile update request:

~~~json
{
  "fullName": "Updated Name",
  "email": "updated@example.com",
  "githubProfileUrl": "https://github.com/updated"
}
~~~

## 4. Models and subscriptions

| API endpoint | Add in | Connect to | Required work |
|---|---|---|---|
| GET /models | Backend/Models.java | NewPredictionScreen.java | Load active models. Support the optional predictionType query parameter and populate the model selector/default model. |
| GET /subscription | Backend/Subscriptions.java | SubscriptionScreen.java, UserDashboard.java and APPLICATION_STORE | Replace hard-coded plan, quota and usage values with the current subscription response. |
| POST /subscription | Backend/Subscriptions.java | SubscriptionScreen.java plan buttons | Send planCode FREE or PREMIUM for the local demo plan switch, then refresh subscription and usage data. |

Do not add private/admin model-management calls to this client. The JavaFX app only reads the active model list.

Subscription request:

~~~json
{
  "planCode": "PREMIUM"
}
~~~

## 5. Prediction endpoints

| API endpoint | Add in | Connect to | Required work |
|---|---|---|---|
| POST /predict | Backend/Predictions.java | NewPredictionScreen.java Analyze action | Build the correct request for GitHub URL or manual features, include predictionType and optional modelId, persist the returned prediction and open PredictionResultScreen with real response data. |
| GET /history | Backend/History.java or Backend/Predictions.java | HistoryScreen.java and dashboard recent predictions | Load the authenticated user's newest predictions. Support search and limit (default 20, minimum 1, maximum 100). |
| GET /predictions | Backend/History.java or Backend/Predictions.java | Compatibility alias | Support the API alias or choose /history as the single canonical client method. |
| GET /predictions/{predictionId} | Backend/History.java or Backend/Predictions.java | PredictionResultScreen.java and HistoryScreen.java | Fetch one owned prediction when a history row is opened. Handle missing/unauthorized records as 404. |
| GET /dashboard | Backend/Dashboard.java | UserDashboard.java | Replace sample dashboard values with totalPredictions, averageMergeProbability, usage, subscription and recentPredictions from the response. |

GitHub URL prediction request:

~~~json
{
  "inputType": "GITHUB_URL",
  "predictionType": "MERGE_PROBABILITY",
  "modelId": 1,
  "pullRequestUrl": "https://github.com/org/repo/pull/10"
}
~~~

Manual feature prediction request:

~~~json
{
  "inputType": "MANUAL_FEATURES",
  "predictionType": "PR_QUALITY",
  "features": {
    "changedFiles": 5,
    "additions": 40,
    "deletions": 12,
    "reviewComments": 3
  }
}
~~~

The prediction response can contain nullable repository/pull-request data and nullable output fields depending on predictionType. Map all returned values, including modelVersion, recommendation and factors, instead of displaying placeholder values. A quota failure is HTTP 429 and must be shown as a subscription/usage message.

## 6. Screen-to-endpoint wiring

| Screen or class | API calls to add |
|---|---|
| LoginScreen.java | POST /login |
| RegisterScreen.java | POST /register |
| ProfileScreen.java | GET /me (or /profile) and PATCH /profile |
| SettingsScreen.java | GET /settings and PATCH /settings |
| NewPredictionScreen.java | GET /models and POST /predict |
| PredictionResultScreen.java | Render POST /predict data and optionally GET /predictions/{predictionId} |
| HistoryScreen.java | GET /history, then GET /predictions/{predictionId} for a selected row |
| UserDashboard.java | GET /dashboard; use the response for cards and recent predictions |
| SubscriptionScreen.java | GET /subscription and POST /subscription |
| DashboardBase.java and COMPONETS.java | Render DTO values passed by the screen instead of hard-coded sample values |
| NAVIGATOR.java | Pass screen data or prediction IDs; keep HTTP calls in Backend classes |

## 7. Cross-cutting implementation rules

1. Run network work off the JavaFX application thread using Task, Service or CompletableFuture, then update controls on the JavaFX thread.
2. Attach the bearer token to every protected endpoint.
3. On HTTP 401, clear the session and navigate to LoginScreen.
4. Show useful messages for 400, 404, 409, 422 and 429 using the API detail text.
5. Never log, persist or include a password in an exception or DTO.
6. Clear token, user and cached protected data on logout.
7. Keep the backend URL configurable for local, test and production environments.
8. Keep private admin endpoints out of URLS.java, Backend classes, screens and navigation.

## 8. Recommended implementation order

1. Add JSON dependency/module configuration, DTOs, API error type and configurable Base client.
2. Extend URLS.java and APPLICATION_STORE.java.
3. Implement Auth.java and wire LoginScreen/RegisterScreen.
4. Implement Profile.java and Settings.java.
5. Implement Models.java and wire the prediction form.
6. Implement Subscriptions.java and replace quota/plan placeholders.
7. Implement Predictions.java and the real prediction result flow.
8. Implement History.java and Dashboard.java.
9. Add background execution, session expiry, validation and quota handling.
10. Test each endpoint with valid, invalid, unauthorized, not-found, conflict and quota-exceeded responses.
