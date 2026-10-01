# PR Prediction System: Final Project Architecture

This document describes the complete project as it exists before backend implementation begins. The repository contains the JavaFX desktop client and a minimal HTTP client. It does not yet contain the backend service, database migrations, prediction model, GitHub integration, authentication service, or persistent storage.

The proposed backend data model is documented separately in [ER_DIAGRAM.md](ER_DIAGRAM.md) and [SCHEMA_DIAGRAM.md](SCHEMA_DIAGRAM.md).

## 1. Product boundary

The application is a pull-request intelligence client. A user should be able to:

1. Sign in and manage profile/settings.
2. Submit a GitHub pull-request URL or manual pull-request features.
3. Ask for merge probability, PR quality, or both.
4. Use an available prediction model selected by the backend or, after the model selector is added, selected by the user.
5. See a prediction result with scores, factors, and a recommendation.
6. Review prediction history and usage.
7. View and manage Free/Premium subscription information.

The JavaFX client owns presentation, local input checks, navigation, and rendering. The future backend must own authentication, authorization, GitHub retrieval, feature extraction, model inference, quality analysis, usage enforcement, subscription state, and persistence.

The model catalog is part of the backend design. The current JavaFX New Prediction screen does not yet display an available-model selector, so the backend should use a documented default model until that control is added.

## 2. Technology and runtime

| Area | Current implementation |
|---|---|
| Language | Java 22 |
| UI | JavaFX 22.0.1, built programmatically without FXML |
| Build | Maven, with Maven wrapper 3.8.5 |
| Module | com.aniruddho_roy.delete.delete |
| Entry point | com.aniruddho_roy.delete.delete.Main |
| HTTP client | JDK java.net.http.HttpClient |
| Backend base URL | http://127.0.0.1:8000 in Backend.Base |
| Styles | theme-base.css plus one light/dark palette stylesheet |
| Tests | No src/test test classes currently exist |

The Maven JavaFX launch target is:

~~~text
com.aniruddho_roy.delete.delete/com.aniruddho_roy.delete.delete.Main
~~~

## 3. Architecture overview

~~~mermaid
flowchart TD
    Main[Main<br/>JavaFX Application] --> Scene[Scene<br/>shared BorderPane]
    Scene --> Theme[THEAME<br/>CSS theme manager]
    Scene --> Navigator[NAVIGATOR<br/>center replacement]

    Navigator --> Home[Dashboard<br/>public home]
    Navigator --> Login[LoginScreen]
    Navigator --> AuthShell[DashboardBase<br/>authenticated shell]
    AuthShell --> Screens[UserDashboard / NewPrediction / Result / History / Profile / Settings / Subscription]
    AuthShell --> Components[COMPONETS<br/>shared controls]

    NewPrediction[NewPredictionScreen] --> Predictions[Predictions]
    Predictions --> HTTP[Backend.Base<br/>HttpClient]
    HTTP --> API[Backend API<br/>127.0.0.1:8000]

    AuthShell -. in-memory flag .-> Store[APPLICATION_STORE]
    Components -. constants/assets .-> Helpers[CONSTANTS / LIB / URLS]
~~~

The client is a lightweight view-switching application rather than a full MVC/MVVM implementation:

- Main creates one JavaFX Scene and one shared BorderPane.
- NAVIGATOR constructs a new screen for each route and assigns it to root.center.
- Public screens extend VBox.
- Authenticated screens extend DashboardBase, which provides the common header, sidebar, center, and recent-predictions area.
- COMPONETS builds reusable controls used by the authenticated shell and dashboard.
- Backend calls currently go directly from NewPredictionScreen to Predictions; a typed service/view-model layer should be inserted before production backend integration.

## 4. Source tree

~~~text
.
├── pom.xml
├── mvnw / mvnw.cmd
├── .mvn/wrapper/
├── .idea/                            # IDE metadata
├── src/main/java/
│   ├── module-info.java
│   └── com/aniruddho_roy/delete/delete/
│       ├── Main.java
│       ├── Backend/
│       │   ├── Base.java
│       │   └── Predictions.java
│       ├── Screen/
│       │   ├── Dashboard.java
│       │   ├── DashboardBase.java
│       │   ├── HistoryScreen.java
│       │   ├── LoginScreen.java
│       │   ├── NewPredictionScreen.java
│       │   ├── PredictionResultScreen.java
│       │   ├── ProfileScreen.java
│       │   ├── SettingsScreen.java
│       │   ├── SubscriptionScreen.java
│       │   └── UserDashboard.java
│       ├── Storage/
│       │   └── APPLICATION_STORE.java
│       └── additional/
│           ├── COMPONETS.java
│           ├── CONSTANTS.java
│           ├── LIB.java
│           ├── NAVIGATOR.java
│           ├── THEAME.java
│           └── URLS.java
└── src/main/resources/
    ├── css/
    │   ├── theme-base.css
    │   ├── light-theme.css
    │   └── dark-theme.css
    └── images/
        ├── home.png
        └── login.png
~~~

target/ is generated build output and is not an authoritative source directory.

## 5. Java file responsibilities

### Application and module files

| File | Responsibility |
|---|---|
| src/main/java/module-info.java | Declares the module, requires javafx.controls, javafx.fxml, and java.net.http, opens the root package to FXML, and exports the root package. The current UI does not use FXML, but the dependency remains configured. |
| com/.../Main.java | JavaFX entry point. Creates the shared root and navigator, opens the public dashboard, creates a 1350 × 850 scene, applies the theme, configures the stage title/resizing, and shows the window. |

### Backend client files

| File | Responsibility |
|---|---|
| Backend/Base.java | Common synchronous HTTP wrapper. Builds a POST request to http://127.0.0.1:8000 plus an endpoint path, sets Content-Type: application/json, accepts HTTP 200, returns the response body, and converts exceptions into Execution Error. |
| Backend/Predictions.java | Prediction API facade. Predict(String repourl) delegates to Base.post_request(URLS.PREDICT.PREDICT_URL, repourl), which currently targets /predict. It does not yet serialize or parse a typed JSON contract. |

### Shared helpers and state

| File | Responsibility |
|---|---|
| additional/NAVIGATOR.java | Holds the shared root and routes to every screen. It tracks activePage through an enum so the sidebar can mark the selected item. Each route constructs a new screen, so there is no back stack or preserved form state. |
| additional/COMPONETS.java | Programmatic JavaFX component factory. Builds header/profile avatar, sidebar/menu buttons, statistic cards, insight cards, quick-action card, subscription card, recent predictions, and individual prediction rows. Current dashboard/recent values are sample literals. |
| additional/THEAME.java | Loads the base CSS and exactly one palette CSS file, defaults to light mode, toggles light/dark mode, and creates reusable theme buttons. Theme state exists only in static memory for the current process. |
| additional/CONSTANTS.java | Stores mutable global UI values: height 850, width 1350, and placeholder application name THIS IS APPLICAITON NAME. |
| additional/LIB.java | Loads a classpath image into an ImageView, sets fit dimensions, and optionally preserves its aspect ratio. |
| additional/URLS.java | Central endpoint paths: /predict, /login, /register, and /history. Only /predict is currently called. |
| Storage/APPLICATION_STORE.java | Contains the static isUserSubscribed flag, initially false. It is read when opening PredictionResultScreen; normal navigator routes still use their default free-plan constructor. |

### Screen files

| File | Responsibility and current behaviour |
|---|---|
| Screen/Dashboard.java | Public landing page. Displays the placeholder application name, home artwork, explanatory copy, theme toggle, and Login button. |
| Screen/LoginScreen.java | Username/password form. Empty fields fail validation. The current local-only credentials are admin / 1234; success opens UserDashboard, failure shows an error, and Back to Home returns to Dashboard. |
| Screen/DashboardBase.java | Shared authenticated BorderPane. Installs header at top, sidebar at left, an initial center placeholder, and recent predictions at right. Stores the navigator and subscription flag for child screens. |
| Screen/UserDashboard.java | Dashboard overview. Builds summary cards, useful insights, a new-prediction quick action, and a plan card. Displayed values such as 48 total predictions and 72% average probability are hard-coded. |
| Screen/NewPredictionScreen.java | Prediction input form. Provides prediction-type selection, GitHub URL/manual feature mode selection, URL regex validation, non-empty manual input validation, the Analyze button, and the call to Predictions. The current handler sends the url variable even in manual mode, then opens a result with placeholder repository/PR values and a 0.5 probability. |
| Screen/PredictionResultScreen.java | Result view. Displays repository/PR labels, merge probability, progress bar, quality, fixed explanatory factors, a recommendation, and links to New Prediction and History. Probability styles use high >= 70, medium 50–69, and low < 50. |
| Screen/HistoryScreen.java | History view with a search field, export button, and five hard-coded sample rows. Search has no handler; export only displays a placeholder message. |
| Screen/ProfileScreen.java | Profile form for full name, email, role, and GitHub profile. Save Changes only displays a message saying backend saving is pending. |
| Screen/SettingsScreen.java | Settings view with theme toggle, notification checkbox, prediction type selector, and default input selector. Save Settings only displays a placeholder backend message. |
| Screen/SubscriptionScreen.java | Free/Premium plan comparison. Plan buttons update a status label only; no billing or subscription API is connected. |

## 6. Resources and styling

- theme-base.css defines semantic component styles, including app-screen, surface-card, buttons, input fields, menus, stat cards, insight cards, prediction rows, subscription cards, and probability colours.
- light-theme.css defines the light palette variables.
- dark-theme.css defines the dark palette variables.
- home.png is used by the public dashboard.
- login.png is used by the login screen.
- pom.xml configures Java 22 compilation, JavaFX Controls/FXML 22.0.1, JUnit 5.10.0, and the JavaFX Maven plugin.
- .mvn/wrapper and mvnw/mvnw.cmd provide Maven wrapper support.
- .idea contains IntelliJ IDEA metadata and is not application logic.

## 7. Runtime flow

### Startup

~~~text
Main.main()
  → JavaFX launch()
  → Main.start(Stage)
  → create shared BorderPane
  → create NAVIGATOR(root)
  → navigator.loadDashboardScreen()
  → create Scene(root, 1350, 850)
  → THEAME.apply(scene)
  → show stage
~~~

### Public home and login

~~~text
Dashboard
  ├─ Login button ──> NAVIGATOR.loadLoginScreen()
  └─ Theme button ──> THEAME.toggle(scene)

LoginScreen
  ├─ empty username/password ──> validation error
  ├─ admin / 1234 ──> NAVIGATOR.loadUserDashboardScreen()
  ├─ any other values ──> incorrect-credentials message
  └─ Back to Home ──> NAVIGATOR.loadDashboardScreen()
~~~

The credential check is only a temporary UI demonstration. There is no user record, password hashing, session, token, or logout invalidation in the JavaFX code.

### Authenticated navigation

~~~text
UserDashboard / DashboardBase
  ├─ Dashboard      ──> UserDashboard
  ├─ New Prediction ──> NewPredictionScreen
  ├─ History        ──> HistoryScreen
  ├─ Subscription   ──> SubscriptionScreen
  ├─ Profile        ──> ProfileScreen
  ├─ Settings       ──> SettingsScreen
  └─ Log out        ──> LoginScreen
~~~

The DashboardBase shell is rebuilt on every route. The sidebar's selected state is based on NAVIGATOR.activePage.

### Prediction flow currently present in code

~~~text
NewPredictionScreen
  1. User selects output type.
  2. User selects GitHub URL or Manual Features.
  3. URL mode checks the GitHub PR URL regex.
  4. Manual mode checks that the text area is non-empty.
  5. Analyze button calls new Predictions().Predict(url).
  6. Predictions calls Base.post_request("/predict", body).
  7. Base synchronously POSTs to http://127.0.0.1:8000/predict.
  8. Raw response returns to the JavaFX event handler.
  9. Navigator opens PredictionResultScreen with placeholder result values.
~~~

The final backend integration should replace this with a typed request/response flow:

~~~text
Screen → view-model/service → JSON API client → backend
      ← typed result/error ← model + GitHub + database
~~~

The backend should persist the request and input, enforce the user's monthly limit, retrieve GitHub data where necessary, run inference, persist result/factors, and return an identifier or complete result payload. The proposed entities and relationships are in ER_DIAGRAM.md, and the table-level design is in SCHEMA_DIAGRAM.md.

### Theme flow

THEAME.apply(scene) installs theme-base.css plus either light-theme.css or dark-theme.css. A theme button calls THEAME.toggle(scene), reapplies the stylesheets, and updates its accessible text. The selection is not persisted between application launches.

## 8. Frontend-to-backend data contract to implement

The current UI implies two input modes and three output modes.

### GitHub URL request

~~~json
{
  "inputType": "GITHUB_URL",
  "predictionType": "BOTH",
  "modelId": "merge-probability-v1",
  "pullRequestUrl": "https://github.com/owner/repository/pull/123"
}
~~~

### Manual feature request

~~~json
{
  "inputType": "MANUAL_FEATURES",
  "predictionType": "MERGE_PROBABILITY",
  "modelId": "merge-probability-v1",
  "features": {
    "changedFiles": 12,
    "additions": 150,
    "deletions": 25,
    "testsAdded": true
  }
}
~~~

The exact endpoint and response shape should be agreed with the backend before changing Predictions. A modelId may be omitted when the backend chooses the default active model. A successful response should contain a prediction identifier, repository/PR information when available, the model id/name/version used, merge probability on a consistent 0..100 scale, quality score/label, recommendation, ordered factors, status, and model version. Errors should have a status code and safe user-facing message. A separate model-list endpoint should return active models and their supported task types.

## 9. Database design handoff

The ER and relational schema diagrams define the backend persistence boundary:

~~~text
users ── user_settings
  ├── subscriptions ── plans
  ├── usage_periods
  └── predictions
       ├── prediction_inputs
       ├── prediction_features
       ├── models
       ├── pull_requests ── repositories
       └── prediction_results ── prediction_factors
~~~

Important rules:

- History is a query over predictions and results, not a second copy of the same rows.
- A manual prediction may have no pull-request row.
- A URL prediction can be linked to a repository/pull request after GitHub retrieval.
- One completed prediction has one result; a pending/failed prediction may have none.
- Every completed prediction records the model catalog row and version used, even if that model is later retired.
- A user can have subscription history but only one active plan at a time.
- Monthly usage is recorded per user and period, including unlimited plans for reporting.

## 10. Known implementation gaps before backend integration

1. Authentication is hard-coded locally (admin / 1234) and is not secure.
2. Dashboard and history values are sample literals rather than API data.
3. There is no database driver, migration, repository, or persistence code.
4. Base performs blocking HTTP work on the JavaFX event thread.
5. The request is labelled JSON but the current prediction call sends a raw string rather than a structured JSON document.
6. Manual-feature mode validates text but still sends the URL variable, which is empty in that mode.
7. NewPredictionScreen passes test, test, and 0.5 to the result screen; the result view formats the numeric value as a percentage.
8. The raw backend response is currently used as the quality string without parsing.
9. Non-200 responses and exceptions collapse to Execution Error.
10. Search, export, profile save, settings save, plan actions, and several dashboard actions are placeholders.
11. URLS.AUTH and URLS.SESSION are constants only; login/history API calls are not implemented.
12. Subscription state is a static boolean and is not loaded from a backend session.
13. No automated tests are present under src/test.

## 11. Recommended backend implementation order

1. Agree on API request/response DTOs and enum values with the frontend.
2. Create migrations from SCHEMA_DIAGRAM.md, seed Free and Premium plans, and add database constraints/indexes.
3. Implement registration, login, password hashing, access/refresh tokens, and authenticated user context.
4. Implement profile and settings read/update endpoints.
5. Implement subscription and usage services, including monthly quota checks.
6. Implement prediction request persistence and manual-feature parsing.
7. Implement GitHub PR retrieval and repository/pull-request persistence.
8. Implement model inference and quality analysis behind a service boundary.
9. Persist results and factors, then expose result/history/dashboard queries.
10. Update the JavaFX client to use typed JSON, background tasks, real session state, and backend-provided values.

