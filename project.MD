# PR Prediction System — Frontend Project Context

## Purpose of this document

This file is the complete handoff document for AI agents and developers working in this repository. Read this file before changing the project. It describes the product intent, the current implementation, the frontend/backend boundary, the source layout, the user flow, mocked data, missing integrations, and safe directions for future work.

## Critical scope statement

**This repository is the JavaFX frontend project for a larger PR prediction system. It is not the prediction backend.**

The main product logic is intended to happen in a separate backend. The JavaFX application is responsible for collecting user input, sending it to that backend, and presenting the backend's response.

The full product is intended to work like this:

1. A user provides either:
   - a GitHub pull-request URL; or
   - a set of pull-request features entered manually.
2. The frontend validates and submits that input to the backend.
3. The backend performs the main work. Depending on the input mode, it may retrieve PR information from GitHub, process the supplied features, prepare model inputs, and run the prediction/quality analysis.
4. The backend returns results such as:
   - the probability that the pull request will be merged into the main repository; and/or
   - a quality assessment or quality score for the pull request;
   - optionally, useful factors, explanations, warnings, or recommendations.
5. This JavaFX frontend displays the result and, when the relevant features exist, displays history, usage, account, and subscription information supplied by the backend.

Do not move prediction algorithms, GitHub crawling, database ownership, authentication authority, or other core business logic into JavaFX screen classes. Those responsibilities belong to the backend or to a dedicated frontend service/client layer that calls the backend.

## Current implementation status

The current repository is an early frontend prototype. It successfully defines the visual direction and basic navigation, but it is not yet connected to a backend.

Currently implemented:

- A JavaFX desktop application entry point.
- A fixed-size home screen.
- A login screen with temporary local validation.
- A user dashboard layout.
- Reusable dashboard UI component builders.
- In-memory screen navigation using one shared `BorderPane`.
- Light and dark themes using JavaFX CSS.
- Home and login image assets.
- Free-plan and premium-plan visual variants.
- Hard-coded sample dashboard statistics, insights, and recent predictions.

Not implemented in this repository:

- No backend connection.
- No HTTP/REST client.
- No GitHub API integration.
- No PR URL input form.
- No manual feature-input form.
- No prediction model or inference code.
- No PR quality calculation.
- No database connection or database driver.
- No persistent users, sessions, predictions, usage, plans, or history.
- No secure authentication or password hashing.
- No JSON serialization dependency.
- No real subscription/payment integration.
- No implemented History, Subscription, Profile, Settings, or New Prediction screens.
- No automated tests under `src/test`.
- No FXML files, even though the Maven/module configuration includes `javafx-fxml`.

Future agents must distinguish between product intent and implemented behavior. Text visible in the dashboard describes the intended PR prediction product, but current values and most actions are demonstrations only.

## Technology and build configuration

- Language: Java 22.
- UI framework: JavaFX 22.0.1.
- Build system: Maven.
- Maven wrapper distribution: Maven 3.8.5.
- Java module name: `com.aniruddho_roy.delete.delete`.
- Maven coordinates:
  - group: `com.aniruddho_roy.delete`
  - artifact: `delete`
  - version: `1.0-SNAPSHOT`
- Application main class: `com.aniruddho_roy.delete.delete.Main`.
- JUnit 5.10.0 dependencies are declared, but there are currently no tests.

Typical Windows commands, assuming `JAVA_HOME` points to JDK 22:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd javafx:run
```

The Maven JavaFX plugin is configured to launch:

```text
com.aniruddho_roy.delete.delete/com.aniruddho_roy.delete.delete.Main
```

`target/` contains generated build output and is ignored by Git. Source files under `src/` are authoritative. Do not edit compiled `.class` files or copied resources under `target/classes`.

## High-level architecture

Current frontend architecture:

```text
Main (JavaFX Application)
  └─ shared BorderPane root
      └─ NAVIGATOR replaces root.center
          ├─ Dashboard (public/home screen)
          ├─ LoginScreen
          └─ UserDashboard
              └─ extends DashboardBase
                  ├─ shared header from COMPONETS
                  ├─ shared sidebar from COMPONETS
                  ├─ dashboard-specific center content
                  └─ shared recent-predictions panel from COMPONETS

Scene
  └─ THEAME applies base CSS plus either light or dark palette
```

Intended complete-system architecture:

```text
JavaFX screen
  → frontend controller/view-model
  → backend API client
  → PR prediction backend
      ├─ authentication and authorization
      ├─ GitHub PR retrieval when a URL is supplied
      ├─ feature validation/transformation
      ├─ prediction model/inference
      ├─ PR quality analysis
      └─ database persistence
  ← structured result/error response
  ← JavaFX renders result, explanation, history, and usage
```

The controller/view-model and API-client layers shown above are recommended future boundaries; they do not yet exist.

## Current runtime flow

### Startup

1. `Main.main()` calls JavaFX `launch()`.
2. `Main.start(Stage)` creates a shared root `BorderPane`.
3. A `NAVIGATOR` is constructed with that root.
4. `NAVIGATOR.loadDashboardScreen()` places the public `Dashboard` in `root.center`.
5. A `Scene` is created at 1350 × 850 pixels.
6. `THEAME.apply(scene)` installs the base stylesheet and current color palette.
7. The stage title is `Home`, resizing is disabled, and the stage is displayed.

### Home screen

- Shows the placeholder application name.
- Shows `/images/home.png` at a requested 300 × 200 size with preserved aspect ratio.
- Shows generic explanatory copy.
- Provides a theme toggle.
- The Login button calls `navigator.loadLoginScreen()`.

### Login screen

- Shows `/images/login.png` at 130 × 130 with preserved aspect ratio.
- Accepts username and password.
- Empty fields produce a styled validation error.
- Authentication is temporary and entirely local:

```text
username: admin
password: 1234
```

- Those credentials navigate to `UserDashboard`.
- Any other credentials show an error.
- Back to Home returns to the public `Dashboard`.
- This must eventually be replaced by backend authentication. Never treat the current credentials as a real security mechanism.

### User dashboard

- `UserDashboard` extends `DashboardBase`.
- `DashboardBase` creates the common header, sidebar, and right-side recent-predictions panel.
- `UserDashboard` replaces the initially empty center with its dashboard overview content.
- The default constructor represents a free user because it passes `subscribed = false`.
- `NAVIGATOR` always uses that default constructor, so the currently reachable dashboard always displays the free-plan state.
- A premium visual state exists through `new UserDashboard(navigator, true)`, but current navigation does not select it.

### Logout

- Logout only navigates back to the login screen.
- There is no real session or token to clear.

## Hard-coded dashboard demonstration data

All of the following values are frontend samples, not backend results and not database records:

Summary cards:

- Total Predictions: `48`.
- High Probability: `29` above 70%.
- Average Probability: `72%`.
- Monthly Usage for a free user: `48 / 100`, with `52 remaining`.
- Monthly Usage for a premium user: `Unlimited`.

Insights:

- Most Analyzed Language: `Java`.
- Top Repository: `spring-projects/spring`.
- Highest Prediction: `91% merge probability`.
- Recent Trend: `Average score increased by 8%`.

Recent predictions:

| Repository | PR | Probability |
|---|---:|---:|
| `spring-projects/spring` | `#32541` | `87%` |
| `openjdk/jdk` | `#21430` | `74%` |
| `microsoft/vscode` | `#22801` | `63%` |
| `facebook/react` | `#30125` | `91%` |
| `tensorflow/tensorflow` | `#72940` | `58%` |

Probability labels are colored by the frontend as follows:

- 70% or greater: high/green.
- 50% through 69%: medium/orange.
- Below 50%: low/red.

The free plan also displays a 48% usage progress bar and the text `48 of 100 predictions used`.

## Navigation and action status

Working navigation:

- Home → Login.
- Login → Home.
- Successful temporary login → User Dashboard.
- Logout → Login.

Placeholder actions that currently print to standard output only:

- Profile picture.
- Dashboard menu item.
- New Prediction menu item.
- Start New Prediction button.
- History menu item.
- Subscription menu item.
- Upgrade/Manage Premium button.
- Profile menu item.
- Settings menu item.
- View Full History button.

Only three screens are registered by `NAVIGATOR`: public Dashboard, LoginScreen, and UserDashboard.

## Source directory map

```text
.
├─ pom.xml
├─ mvnw
├─ mvnw.cmd
├─ .mvn/
│  └─ wrapper/
│     ├─ maven-wrapper.jar
│     └─ maven-wrapper.properties
├─ src/
│  └─ main/
│     ├─ java/
│     │  ├─ module-info.java
│     │  └─ com/aniruddho_roy/delete/delete/
│     │     ├─ Main.java
│     │     ├─ Screen/
│     │     │  ├─ Dashboard.java
│     │     │  ├─ DashboardBase.java
│     │     │  ├─ LoginScreen.java
│     │     │  └─ UserDashboard.java
│     │     ├─ additional/
│     │     │  ├─ COMPONETS.java
│     │     │  ├─ CONSTANTS.java
│     │     │  ├─ LIB.java
│     │     │  ├─ NAVIGATOR.java
│     │     │  └─ THEAME.java
│     │     └─ Storage/
│     │        └─ APPLICATION.java
│     └─ resources/
│        ├─ css/
│        │  ├─ theme-base.css
│        │  ├─ light-theme.css
│        │  └─ dark-theme.css
│        └─ images/
│           ├─ home.png
│           └─ login.png
├─ .idea/                 # IntelliJ project metadata
└─ target/                # generated/ignored build output
```

There is also an empty package-shaped directory under `src/main/resources/com/...`; it currently contains no authored resource files.

## File-by-file responsibilities

### `pom.xml`

Defines Java 22 compilation, JavaFX Controls/FXML 22.0.1, JUnit 5.10.0, the JavaFX Maven plugin, and the modular `Main` launch target.

### `module-info.java`

Declares module `com.aniruddho_roy.delete.delete`, requires `javafx.controls` and `javafx.fxml`, opens the root application package to FXML, and exports the root application package. No FXML is currently used.

### `Main.java`

JavaFX application entry point. Owns the stage, shared root, scene creation, initial navigation, initial theme installation, window dimensions, and non-resizable setting.

### `Screen/Dashboard.java`

Public landing/home screen. Displays the application title, home artwork, generic information, theme toggle, and Login button.

### `Screen/LoginScreen.java`

Login form and local validation. Uses temporary hard-coded credentials and navigates to the user dashboard on success. It does not call a backend.

### `Screen/DashboardBase.java`

Base `BorderPane` for authenticated dashboard-style screens. Creates a shared header, sidebar, initial empty center, and recent-predictions panel through `COMPONETS`. Stores the navigator and subscription flag as protected fields.

### `Screen/UserDashboard.java`

Dashboard overview content. Builds summary cards, insight cards, quick action, and subscription card using `COMPONETS`. It currently supplies all display values directly as literals.

### `additional/COMPONETS.java`

Reusable programmatic JavaFX component factory. Despite its spelling, this is the central builder for:

- header;
- profile picture;
- sidebar and menu buttons;
- statistic cards;
- insights and insight items;
- quick-action card;
- subscription card;
- recent-predictions card;
- individual prediction rows.

It currently contains sample data and placeholder event handlers. Backend-driven data should eventually be passed into reusable components as typed view data instead of being embedded here.

### `additional/NAVIGATOR.java`

Very small navigation service. Holds the shared root `BorderPane` and replaces its center with newly constructed screen objects. It does not preserve a back stack or screen state.

### `additional/THEAME.java`

Runtime light/dark theme manager. It:

- loads the base, light, and dark stylesheet resource URLs;
- defaults to `LIGHT`;
- installs base CSS once;
- swaps the light/dark palette stylesheet;
- creates theme-toggle buttons;
- stores theme selection only in a static in-memory field.

Theme choice survives screen replacement during one process, but it is not persisted across application restarts.

### `additional/LIB.java`

Contains one image-loading helper named `Loadimage`. It loads a classpath resource and returns a configured `ImageView`.

### `additional/CONSTANTS.java`

Defines mutable public static values:

- application height: `850`;
- application width: `1350`;
- application name: `THIS IS APPLICAITON NAME`.

The application name is a placeholder and contains the existing spelling `APPLICAITON`.

### `Storage/APPLICATION.java`

Currently an empty placeholder class. It does not store application state and is not referenced by current code.

### CSS files

- `theme-base.css` defines shared semantic component styles and uses custom color variables.
- `light-theme.css` supplies the light palette.
- `dark-theme.css` supplies the dark palette.

Important style classes include `app-screen`, `surface-card`, `primary-button`, `theme-toggle`, `login-input`, `menu-button`, `menu-button-selected`, `stat-*`, `insight-*`, `subscription-card`, `prediction-item`, and probability-level classes.

### Image assets

- `home.png`: blue futuristic interface/technology artwork used on the public landing screen.
- `login.png`: blue open-padlock icon on a transparent background used on the login screen.

### Maven and IDE files

- `mvnw`, `mvnw.cmd`, and `.mvn/wrapper/*` are standard Maven wrapper support files.
- `.idea/*` contains IntelliJ configuration and is not application logic.
- `.gitignore` excludes build output and common IDE/editor artifacts.

## Theme behavior

The scene always has `theme-base.css` plus exactly one palette stylesheet. The active palette begins as light. Theme buttons display the target mode (`Dark mode` while light is active, and `Light mode` while dark is active).

Most colors have been moved into semantic CSS. Future UI components should reuse existing classes or add semantic classes to `theme-base.css`, with values supplied by both palette files. Avoid reintroducing inline hard-coded colors because inline styles can override theme rules.

Theme persistence is not currently implemented. If later required, store only the selected UI preference locally; do not mix it with prediction/backend domain state.

## Intended frontend/backend contract

No API contract is implemented yet. The following describes the expected responsibility boundary and a reasonable future direction; endpoint names and exact fields must be agreed with the backend team before implementation.

### Prediction input mode A: GitHub PR URL

The frontend should collect a URL such as:

```text
https://github.com/{owner}/{repository}/pull/{number}
```

The frontend should perform basic format/required-field validation, then send the URL to the backend. The backend should own GitHub access, authoritative PR retrieval, feature extraction, model inference, and persistence.

Conceptual request:

```json
{
  "inputType": "github_url",
  "pullRequestUrl": "https://github.com/owner/repository/pull/123"
}
```

### Prediction input mode B: manually supplied features

The frontend should render fields defined by the backend/model contract and send typed values. Feature names, units, required/optional status, allowed ranges, and model version must come from an agreed schema; they must not be guessed inside screen code.

Conceptual request:

```json
{
  "inputType": "features",
  "features": {
    "featureName": "value"
  }
}
```

### Conceptual prediction response

```json
{
  "predictionId": "backend-generated-id",
  "mergeProbability": 0.87,
  "qualityScore": 0.78,
  "qualityLabel": "good",
  "summary": "Human-readable backend explanation",
  "factors": [],
  "warnings": [],
  "modelVersion": "backend-model-version",
  "createdAt": "backend timestamp"
}
```

This JSON is illustrative, not an existing contract. In particular, the team must decide whether quality is a score, category, detailed report, or combination of these.

### Error behavior

The future frontend should distinguish at least:

- invalid user input;
- malformed or unsupported GitHub URL;
- GitHub repository/PR not found;
- private repository requiring authorization;
- GitHub rate limit or upstream failure;
- backend validation failure;
- prediction/model failure;
- network timeout/unavailable backend;
- expired/unauthorized session;
- usage-limit rejection.

Long-running network and prediction calls must run off the JavaFX Application Thread. UI controls should show loading state, prevent duplicate submissions, support retry where safe, and update UI through JavaFX-safe mechanisms.

## Data ownership expectations

The frontend should treat the backend as authoritative for:

- users and authentication;
- access/refresh tokens or sessions;
- GitHub-derived PR data;
- feature definitions and validation rules;
- prediction results and model versions;
- PR quality results;
- prediction history;
- dashboard aggregates and insights;
- account usage and quotas;
- subscription state and entitlements;
- audit/payment data, if those product features are implemented.

The frontend may hold short-lived presentation state and safe local preferences. Secrets, GitHub tokens, plain-text passwords, authoritative subscription state, and model logic must not be embedded in source code.

## Recommended future frontend structure

When backend integration begins, keep UI construction separate from networking and domain data. A reasonable evolution is:

```text
Screen/ or view/
  JavaFX nodes and user interactions

controller/ or viewmodel/
  validation, loading/error state, command coordination

service/
  AuthService, PredictionService, DashboardService

client/
  HTTP transport, endpoint calls, token handling

model/
  request/response DTOs and view data

session/
  current signed-in user/session state
```

Potential frontend models include:

- `UserSession`.
- `PredictionInput` with URL and feature variants.
- `PredictionResult`.
- `QualityAssessment`.
- `PredictionFactor`.
- `PredictionSummary`.
- `DashboardSummary`.
- `UsageSummary`.
- `SubscriptionSummary`.

These names are recommendations, not existing classes.

## Suggested feature implementation order

1. Agree on the backend API and prediction feature schema.
2. Add typed request/response models and one backend API client.
3. Replace hard-coded login with backend authentication and session handling.
4. Implement the New Prediction screen with GitHub URL and manual-feature modes.
5. Submit predictions asynchronously and display loading/error/result states.
6. Replace dashboard literals with a backend dashboard-summary response.
7. Implement prediction history and prediction-detail screens.
8. Implement profile/settings and subscription UI only when backend endpoints exist.
9. Add frontend unit tests and integration tests using a mock backend.
10. Add safe configuration for backend base URL and environment selection.

## Current design and code caveats

- Several identifiers contain spelling mistakes: `THEAME`, `COMPONETS`, `Loadimage`, and the `APPLICAITON` text. These are real current identifiers/values. Do not silently rename them as part of unrelated work because other files reference them.
- Package names use capitalized `Screen` and `Storage`, which is unconventional in Java but currently real.
- `DashboardBase` creates an empty center `ScrollPane`; `UserDashboard` immediately replaces it with a `VBox`. The center is therefore not currently scrollable through that initial pane.
- `UserDashboard` repeatedly constructs new `COMPONETS` instances. The class is stateless, so this works, although dependency reuse/refactoring may be desirable later.
- `COMPONETS` mixes reusable view construction, sample data, and event behavior. Backend integration should separate these concerns.
- `NAVIGATOR` constructs screens directly and has no route parameters, history, dependency injection, or retained screen state.
- The fixed, non-resizable 1350 × 850 window may not fit smaller displays and is not responsive.
- `LIB.Loadimage` assumes the resource exists; a missing resource may cause failure rather than a friendly fallback.
- `createPredictionItem` expects percentage text such as `87%` and parses it using string replacement plus `Integer.parseInt`.
- Login success sets a success message and immediately replaces the screen, so the message is unlikely to remain visible.
- Subscription status, user name (`Aniruddho`), initials (`AR`), statistics, insights, and predictions are all hard-coded.
- The application has no logging framework; placeholder actions use `System.out.println`.
- The generated Maven compiler input list under `target/` may be stale and must not be treated as documentation of the current source tree.

## Rules for future AI agents

1. Treat this repository as the **frontend only**.
2. Preserve the product purpose: users submit a GitHub PR URL or PR features; the backend predicts merge probability into the main repository and/or evaluates PR quality.
3. Before claiming a capability exists, verify whether it is real or only represented by hard-coded UI text.
4. Do not invent the backend language, framework, database, endpoints, feature names, or model algorithm. Confirm them with the user/backend contract.
5. Keep network/database/model logic out of JavaFX component constructors.
6. Never block the JavaFX Application Thread with network or model calls.
7. Never store real credentials, access tokens, or secrets in source files.
8. Use source under `src/` as authoritative; ignore `target/` for edits.
9. Preserve the current theme approach by using semantic CSS classes.
10. Keep unrelated user changes intact and avoid broad renames unless explicitly requested.
11. Add tests for new behavior; the project currently has no tests to protect regressions.
12. Update this file whenever architecture, screens, integration contracts, or project scope materially change.

## One-paragraph summary

This repository is a modular Java 22/JavaFX 22.0.1 desktop **frontend** for a PR prediction system. Its intended users provide either a GitHub pull-request URL or manually entered PR features, while a separate backend performs GitHub data retrieval, feature processing, prediction, quality analysis, persistence, authentication, and other core operations. The backend should return the probability that the PR will merge into the main repository and/or a PR quality result, which this frontend presents. Today the repository contains a themed home screen, temporary login, reusable dashboard layout, sample free/premium account views, and hard-coded prediction statistics/history, but it contains no backend, database, HTTP client, prediction model, real authentication, or prediction-input screen.
