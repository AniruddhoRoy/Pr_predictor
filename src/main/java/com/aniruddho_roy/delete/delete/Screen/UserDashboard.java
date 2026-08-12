package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.additional.CONSTANTS;
import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class UserDashboard extends BorderPane {

    private final NAVIGATOR navigator;
    private final boolean subscribed;

    private static final String PRIMARY = "#2563EB";
    private static final String DARK = "#0F172A";
    private static final String MUTED = "#64748B";
    private static final String BACKGROUND = "#F1F5F9";
    private static final String GREEN = "#16A34A";

    /*
     * Use this constructor for a free user.
     */
    public UserDashboard(NAVIGATOR navigator) {
        this(navigator, false);
    }

    /*
     * Pass true if the user has a premium subscription.
     */
    public UserDashboard(NAVIGATOR navigator, boolean subscribed) {
        this.navigator = navigator;
        this.subscribed = subscribed;

        setStyle("-fx-background-color: " + BACKGROUND + ";");
        setPadding(new Insets(20));

        HBox header = createHeader();
        VBox sidebar = createSidebar();
        ScrollPane dashboardContent = createDashboardContent();
        VBox recentPredictions = createPreviousPredictions();

        setTop(header);
        setLeft(sidebar);
        setCenter(dashboardContent);
        setRight(recentPredictions);

        BorderPane.setMargin(header, new Insets(0, 0, 20, 0));
        BorderPane.setMargin(sidebar, new Insets(0, 20, 0, 0));
        BorderPane.setMargin(recentPredictions, new Insets(0, 0, 0, 20));
    }

    /*
     * Header
     */
    private HBox createHeader() {

        VBox welcomeBox = new VBox(4);

        Label welcome = new Label("Hello, Aniruddho");
        welcome.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcome.setTextFill(Color.web(DARK));

        Label description = new Label(
                "Here is an overview of your pull-request predictions."
        );
        description.setFont(Font.font("Arial", 14));
        description.setTextFill(Color.web(MUTED));

        welcomeBox.getChildren().addAll(welcome, description);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        StackPane profilePicture = createProfilePicture();

        HBox header = new HBox(
                15,
                welcomeBox,
                spacer,
                profilePicture
        );

        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 20, 15, 20));

        header.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 14px;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-border-radius: 14px;"
        );

        return header;
    }

    private StackPane createProfilePicture() {

        Circle circle = new Circle(25);
        circle.setFill(Color.web("#DBEAFE"));
        circle.setStroke(Color.web(PRIMARY));
        circle.setStrokeWidth(2);

        Label initials = new Label("AR");
        initials.setTextFill(Color.web(PRIMARY));
        initials.setFont(
                Font.font("Arial", FontWeight.BOLD, 15)
        );

        StackPane profile = new StackPane(circle, initials);
        profile.setCursor(Cursor.HAND);

        profile.setOnMouseClicked(event -> {
            System.out.println("Open profile page");

            // Add your navigator method:
            // navigator.loadProfileScreen();
        });

        return profile;
    }

    /*
     * Sidebar
     */
    private VBox createSidebar() {

        Label applicationName = new Label(
                CONSTANTS.APPLICATION_NAME
        );

        applicationName.setFont(
                Font.font("Arial", FontWeight.BOLD, 19)
        );
        applicationName.setTextFill(Color.web(PRIMARY));
        applicationName.setWrapText(true);
        applicationName.setMaxWidth(170);

        Label applicationType = new Label("PR Intelligence");
        applicationType.setTextFill(Color.web(MUTED));
        applicationType.setFont(Font.font("Arial", 12));

        VBox logoArea = new VBox(
                3,
                applicationName,
                applicationType
        );

        logoArea.setPadding(new Insets(0, 0, 20, 5));

        Button dashboardButton =
                createMenuButton("Dashboard", true);

        Button predictionButton =
                createMenuButton("New Prediction", false);

        Button historyButton =
                createMenuButton("History", false);

        Button subscriptionButton =
                createMenuButton("Subscription", false);

        Button profileButton =
                createMenuButton("Profile", false);

        Button settingsButton =
                createMenuButton("Settings", false);

        dashboardButton.setOnAction(event ->
                System.out.println("Dashboard already open")
        );

        predictionButton.setOnAction(event ->
                openPredictionPage()
        );

        historyButton.setOnAction(event -> {
            System.out.println("Open history page");

            // navigator.loadHistoryScreen();
        });

        subscriptionButton.setOnAction(event -> {
            System.out.println("Open subscription page");

            // navigator.loadSubscriptionScreen();
        });

        profileButton.setOnAction(event -> {
            System.out.println("Open profile page");

            // navigator.loadProfileScreen();
        });

        settingsButton.setOnAction(event -> {
            System.out.println("Open settings page");

            // navigator.loadSettingsScreen();
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("Log out");
        logoutButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setPrefHeight(42);
        logoutButton.setAlignment(Pos.CENTER_LEFT);

        logoutButton.setStyle(
                "-fx-background-color: #FEF2F2;" +
                        "-fx-text-fill: #DC2626;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 0 15px;"
        );

        logoutButton.setOnAction(event -> {
            System.out.println("User logged out");

            navigator.loadLoginScreen();
        });

        VBox sidebar = new VBox(
                10,
                logoArea,
                dashboardButton,
                predictionButton,
                historyButton,
                subscriptionButton,
                profileButton,
                settingsButton,
                spacer,
                logoutButton
        );

        sidebar.setPrefWidth(190);
        sidebar.setMinWidth(190);
        sidebar.setPadding(new Insets(20));

        sidebar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 14px;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-border-radius: 14px;"
        );

        return sidebar;
    }

    private Button createMenuButton(
            String text,
            boolean selected
    ) {
        Button button = new Button(text);

        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(42);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setCursor(Cursor.HAND);

        if (selected) {
            button.setStyle(
                    "-fx-background-color: " + PRIMARY + ";" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8px;" +
                            "-fx-padding: 0 15px;"
            );
        } else {
            button.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: " + DARK + ";" +
                            "-fx-font-size: 14px;" +
                            "-fx-background-radius: 8px;" +
                            "-fx-padding: 0 15px;"
            );
        }

        return button;
    }

    /*
     * Main dashboard content
     */
    private ScrollPane createDashboardContent() {

        Label heading = new Label("Dashboard Overview");
        heading.setFont(
                Font.font("Arial", FontWeight.BOLD, 23)
        );
        heading.setTextFill(Color.web(DARK));

        Label subheading = new Label(
                "Monitor your prediction activity and account usage."
        );
        subheading.setFont(Font.font("Arial", 14));
        subheading.setTextFill(Color.web(MUTED));

        HBox summaryCards = new HBox(
                12,
                createStatCard(
                        "Total Predictions",
                        "48",
                        "All-time analyses",
                        "#EFF6FF",
                        "#2563EB"
                ),
                createStatCard(
                        "High Probability",
                        "29",
                        "Above 70%",
                        "#F0FDF4",
                        "#16A34A"
                ),
                createStatCard(
                        "Average Probability",
                        "72%",
                        "All predictions",
                        "#FFF7ED",
                        "#EA580C"
                ),
                createStatCard(
                        "Monthly Usage",
                        subscribed ? "Unlimited" : "48 / 100",
                        subscribed
                                ? "Premium account"
                                : "52 remaining",
                        "#FAF5FF",
                        "#9333EA"
                )
        );

        summaryCards.setAlignment(Pos.CENTER);
        summaryCards.setMaxWidth(Double.MAX_VALUE);

        VBox insightsCard = createInsightsCard();

        VBox quickActionCard = createQuickActionCard();
        VBox subscriptionCard =
                createSubscriptionCard(subscribed);

        HBox bottomCards = new HBox(
                15,
                quickActionCard,
                subscriptionCard
        );

        HBox.setHgrow(quickActionCard, Priority.ALWAYS);
        HBox.setHgrow(subscriptionCard, Priority.ALWAYS);

        quickActionCard.setMaxWidth(Double.MAX_VALUE);
        subscriptionCard.setMaxWidth(Double.MAX_VALUE);

        VBox content = new VBox(
                18,
                heading,
                subheading,
                summaryCards,
                insightsCard,
                bottomCards
        );

        content.setPadding(new Insets(25));
        content.setMaxWidth(Double.MAX_VALUE);

        content.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 14px;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-border-radius: 14px;"
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );
        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;" +
                        "-fx-border-color: transparent;"
        );

        return scrollPane;
    }

    /*
     * Summary statistic card
     */
    private VBox createStatCard(
            String title,
            String value,
            String description,
            String background,
            String accentColor
    ) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 12)
        );
        titleLabel.setTextFill(Color.web(MUTED));
        titleLabel.setWrapText(true);

        Label valueLabel = new Label(value);
        valueLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 24)
        );
        valueLabel.setTextFill(Color.web(accentColor));
        valueLabel.setWrapText(true);

        Label descriptionLabel = new Label(description);
        descriptionLabel.setFont(Font.font("Arial", 11));
        descriptionLabel.setTextFill(Color.web(MUTED));
        descriptionLabel.setWrapText(true);

        VBox card = new VBox(
                7,
                titleLabel,
                valueLabel,
                descriptionLabel
        );

        card.setPadding(new Insets(15));
        card.setMinWidth(125);
        card.setPrefHeight(120);
        card.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(card, Priority.ALWAYS);

        card.setStyle(
                "-fx-background-color: " + background + ";" +
                        "-fx-background-radius: 11px;" +
                        "-fx-border-color: " + accentColor + "35;" +
                        "-fx-border-radius: 11px;"
        );

        return card;
    }

    /*
     * Useful insights
     */
    private VBox createInsightsCard() {

        Label heading = new Label("Useful Insights");
        heading.setFont(
                Font.font("Arial", FontWeight.BOLD, 18)
        );
        heading.setTextFill(Color.web(DARK));

        Label description = new Label(
                "Highlights generated from your prediction history"
        );
        description.setFont(Font.font("Arial", 12));
        description.setTextFill(Color.web(MUTED));

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        ColumnConstraints firstColumn =
                new ColumnConstraints();

        ColumnConstraints secondColumn =
                new ColumnConstraints();

        firstColumn.setPercentWidth(50);
        secondColumn.setPercentWidth(50);

        firstColumn.setHgrow(Priority.ALWAYS);
        secondColumn.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(
                firstColumn,
                secondColumn
        );

        VBox languageInsight = createInsightItem(
                "Most Analyzed Language",
                "Java",
                "#EFF6FF",
                "#2563EB"
        );

        VBox repositoryInsight = createInsightItem(
                "Top Repository",
                "spring-projects/spring",
                "#F0FDF4",
                "#16A34A"
        );

        VBox highestInsight = createInsightItem(
                "Highest Prediction",
                "91% merge probability",
                "#FFF7ED",
                "#EA580C"
        );

        VBox trendInsight = createInsightItem(
                "Recent Trend",
                "Average score increased by 8%",
                "#FAF5FF",
                "#9333EA"
        );

        grid.add(languageInsight, 0, 0);
        grid.add(repositoryInsight, 1, 0);
        grid.add(highestInsight, 0, 1);
        grid.add(trendInsight, 1, 1);

        VBox card = new VBox(
                5,
                heading,
                description,
                grid
        );

        VBox.setMargin(grid, new Insets(10, 0, 0, 0));

        card.setPadding(new Insets(20));
        card.setMaxWidth(Double.MAX_VALUE);

        card.setStyle(
                "-fx-background-color: #F8FAFC;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-border-radius: 12px;"
        );

        return card;
    }

    private VBox createInsightItem(
            String title,
            String value,
            String background,
            String accentColor
    ) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 12)
        );
        titleLabel.setTextFill(Color.web(MUTED));

        Label valueLabel = new Label(value);
        valueLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 14)
        );
        valueLabel.setTextFill(Color.web(accentColor));
        valueLabel.setWrapText(true);

        VBox item = new VBox(
                5,
                titleLabel,
                valueLabel
        );

        item.setPadding(new Insets(13));
        item.setMaxWidth(Double.MAX_VALUE);
        item.setPrefHeight(70);

        item.setStyle(
                "-fx-background-color: " + background + ";" +
                        "-fx-background-radius: 9px;"
        );

        return item;
    }

    /*
     * Quick action card
     */
    private VBox createQuickActionCard() {

        Label heading = new Label("New Prediction");
        heading.setFont(
                Font.font("Arial", FontWeight.BOLD, 17)
        );
        heading.setTextFill(Color.web(DARK));

        Label description = new Label(
                "Analyze a GitHub pull request and estimate its merge probability."
        );
        description.setFont(Font.font("Arial", 13));
        description.setTextFill(Color.web(MUTED));
        description.setWrapText(true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button predictionButton =
                new Button("Start New Prediction");

        predictionButton.setPrefHeight(40);
        predictionButton.setMaxWidth(Double.MAX_VALUE);
        predictionButton.setCursor(Cursor.HAND);

        predictionButton.setStyle(
                "-fx-background-color: " + PRIMARY + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;"
        );

        predictionButton.setOnAction(event ->
                openPredictionPage()
        );

        VBox card = new VBox(
                10,
                heading,
                description,
                spacer,
                predictionButton
        );

        card.setPadding(new Insets(20));
        card.setPrefHeight(180);

        card.setStyle(
                "-fx-background-color: #EFF6FF;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-color: #BFDBFE;" +
                        "-fx-border-radius: 12px;"
        );

        return card;
    }

    /*
     * Subscription card
     */
    private VBox createSubscriptionCard(
            boolean subscribed
    ) {
        String accentColor =
                subscribed ? "#16A34A" : "#9333EA";

        String cardBackground =
                subscribed ? "#F0FDF4" : "#FAF5FF";

        String borderColor =
                subscribed ? "#BBF7D0" : "#E9D5FF";

        Label statusLabel = new Label(
                subscribed
                        ? "PREMIUM ACCOUNT"
                        : "FREE PLAN"
        );

        statusLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 11)
        );
        statusLabel.setTextFill(Color.web(accentColor));

        Label heading = new Label(
                subscribed
                        ? "Premium is active"
                        : "Upgrade to Premium"
        );

        heading.setFont(
                Font.font("Arial", FontWeight.BOLD, 17)
        );
        heading.setTextFill(Color.web(DARK));

        Label description = new Label(
                subscribed
                        ? "You have unlimited predictions and access to advanced insights."
                        : "Unlock more predictions, advanced insights and priority analysis."
        );

        description.setFont(Font.font("Arial", 13));
        description.setTextFill(Color.web(MUTED));
        description.setWrapText(true);

        VBox card = new VBox(
                7,
                statusLabel,
                heading,
                description
        );

        if (!subscribed) {
            ProgressBar usageBar = new ProgressBar(0.48);
            usageBar.setMaxWidth(Double.MAX_VALUE);
            usageBar.setPrefHeight(9);
            usageBar.setStyle("-fx-accent: #9333EA;");

            Label usageLabel =
                    new Label("48 of 100 predictions used");

            usageLabel.setFont(Font.font("Arial", 11));
            usageLabel.setTextFill(Color.web(MUTED));

            card.getChildren().addAll(
                    usageBar,
                    usageLabel
            );
        } else {
            Label premiumFeature =
                    new Label("Unlimited monthly usage");

            premiumFeature.setFont(
                    Font.font("Arial", FontWeight.BOLD, 12)
            );
            premiumFeature.setTextFill(Color.web(GREEN));

            card.getChildren().add(premiumFeature);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button planButton = new Button(
                subscribed
                        ? "Manage Premium"
                        : "Upgrade Now"
        );

        planButton.setPrefHeight(40);
        planButton.setMaxWidth(Double.MAX_VALUE);
        planButton.setCursor(Cursor.HAND);

        planButton.setStyle(
                "-fx-background-color: " + accentColor + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;"
        );

        planButton.setOnAction(event -> {
            if (subscribed) {
                System.out.println(
                        "Open premium management"
                );
            } else {
                System.out.println(
                        "Open subscription upgrade page"
                );
            }

            // navigator.loadSubscriptionScreen();
        });

        card.getChildren().addAll(
                spacer,
                planButton
        );

        card.setPadding(new Insets(20));
        card.setPrefHeight(180);

        card.setStyle(
                "-fx-background-color: " + cardBackground + ";" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-border-radius: 12px;"
        );

        return card;
    }

    /*
     * Recent predictions
     */
    private VBox createPreviousPredictions() {

        Label heading = new Label("Recent Predictions");
        heading.setFont(
                Font.font("Arial", FontWeight.BOLD, 18)
        );
        heading.setTextFill(Color.web(DARK));

        VBox predictionList = new VBox(10);

        List<String[]> predictions = List.of(
                new String[]{
                        "spring-projects/spring",
                        "#32541",
                        "87%"
                },
                new String[]{
                        "openjdk/jdk",
                        "#21430",
                        "74%"
                },
                new String[]{
                        "microsoft/vscode",
                        "#22801",
                        "63%"
                },
                new String[]{
                        "facebook/react",
                        "#30125",
                        "91%"
                },
                new String[]{
                        "tensorflow/tensorflow",
                        "#72940",
                        "58%"
                }
        );

        for (String[] prediction : predictions) {
            predictionList.getChildren().add(
                    createPredictionItem(
                            prediction[0],
                            prediction[1],
                            prediction[2]
                    )
            );
        }

        Button viewAllButton =
                new Button("View Full History");

        viewAllButton.setMaxWidth(Double.MAX_VALUE);
        viewAllButton.setPrefHeight(40);
        viewAllButton.setCursor(Cursor.HAND);

        viewAllButton.setStyle(
                "-fx-background-color: #EFF6FF;" +
                        "-fx-text-fill: " + PRIMARY + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;"
        );

        viewAllButton.setOnAction(event -> {
            System.out.println("Open complete history");

            // navigator.loadHistoryScreen();
        });

        VBox recentCard = new VBox(
                15,
                heading,
                predictionList,
                viewAllButton
        );

        recentCard.setPrefWidth(285);
        recentCard.setMinWidth(285);
        recentCard.setPadding(new Insets(20));

        recentCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 14px;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-border-radius: 14px;"
        );

        return recentCard;
    }

    private HBox createPredictionItem(
            String repository,
            String pullRequest,
            String probability
    ) {
        Label repositoryLabel = new Label(repository);
        repositoryLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 12)
        );
        repositoryLabel.setTextFill(Color.web(DARK));
        repositoryLabel.setMaxWidth(170);
        repositoryLabel.setWrapText(true);

        Label pullRequestLabel = new Label(pullRequest);
        pullRequestLabel.setFont(Font.font("Arial", 11));
        pullRequestLabel.setTextFill(Color.web(MUTED));

        VBox details = new VBox(
                3,
                repositoryLabel,
                pullRequestLabel
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label percentage = new Label(probability);
        percentage.setFont(
                Font.font("Arial", FontWeight.BOLD, 13)
        );

        int numericProbability = Integer.parseInt(
                probability.replace("%", "")
        );

        if (numericProbability >= 70) {
            percentage.setTextFill(Color.web("#16A34A"));
        } else if (numericProbability >= 50) {
            percentage.setTextFill(Color.web("#EA580C"));
        } else {
            percentage.setTextFill(Color.web("#DC2626"));
        }

        HBox item = new HBox(
                8,
                details,
                spacer,
                percentage
        );

        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12));

        item.setStyle(
                "-fx-background-color: #F8FAFC;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-border-radius: 8px;"
        );

        return item;
    }

    /*
     * Connect this method to your real prediction page.
     */
    private void openPredictionPage() {
        System.out.println("Open prediction page");

        /*
         * Replace the line above with your actual method:
         *
         * navigator.loadPredictionScreen();
         */
    }
}