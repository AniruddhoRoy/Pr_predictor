package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.Backend.Dashboard;
import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Locale;


public class UserDashboard extends DashboardBase {


    private final Dashboard dashboardApi =
            new Dashboard();


    private final VBox content =
            new VBox(18);


    // =========================================================
    // Constructors
    // =========================================================

    public UserDashboard(
            NAVIGATOR navigator
    ) {

        this(
                navigator,
                false
        );
    }


    public UserDashboard(
            NAVIGATOR navigator,
            boolean subscribed
    ) {

        super(
                navigator,
                subscribed
        );


        setCenter(
                createDashboard()
        );


        loadDashboard();
    }


    // =========================================================
    // Main layout
    // =========================================================

    private ScrollPane createDashboard() {


        Label heading =
                new Label(
                        "Dashboard"
                );


        heading.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        25
                )
        );


        heading.getStyleClass().add(
                "heading"
        );


        Label description =
                new Label(
                        "Monitor your prediction activity, monthly credits and recent analyses."
                );


        description.setWrapText(
                true
        );


        description.getStyleClass().add(
                "muted-text"
        );


        Label loading =
                new Label(
                        "Loading dashboard..."
                );


        loading.getStyleClass().add(
                "muted-text"
        );


        content.getChildren().addAll(
                heading,
                description,
                loading
        );


        content.setPadding(
                new Insets(25)
        );


        content.setMaxWidth(
                Double.MAX_VALUE
        );


        content.getStyleClass().add(
                "dashboard-content"
        );


        ScrollPane scrollPane =
                new ScrollPane(
                        content
                );


        scrollPane.setFitToWidth(
                true
        );


        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );


        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );


        scrollPane
                .getStyleClass()
                .add(
                        "transparent-scroll-pane"
                );


        return scrollPane;
    }


    // =========================================================
    // Load GET /dashboard
    // =========================================================

    private void loadDashboard() {


        Task<JsonNode> task =
                new Task<>() {

                    @Override
                    protected JsonNode call() {

                        return dashboardApi
                                .getDashboard();
                    }
                };


        task.setOnSucceeded(
                event -> {

                    JsonNode dashboard =
                            task.getValue();


                    displayDashboard(
                            dashboard
                    );
                }
        );


        task.setOnFailed(
                event -> {

                    Throwable exception =
                            task.getException();


                    String message =
                            exception == null ||
                                    exception.getMessage() == null
                                    ? "Unable to load dashboard."
                                    : exception.getMessage();


                    showDashboardError(
                            message
                    );
                }
        );


        Thread thread =
                new Thread(task);


        thread.setDaemon(
                true
        );


        thread.start();
    }


    // =========================================================
    // Display dashboard
    // =========================================================

    private void displayDashboard(
            JsonNode dashboard
    ) {


        // Preserve header

        while (
                content.getChildren().size() > 2
        ) {

            content
                    .getChildren()
                    .remove(2);
        }


        // =====================================================
        // API values
        // =====================================================

        int totalPredictions =
                dashboard
                        .path("totalPredictions")
                        .asInt(0);


        Double averageProbability =
                nullableDouble(
                        dashboard,
                        "averageMergeProbability"
                );


        JsonNode usage =
                dashboard.path(
                        "usage"
                );


        int predictionsUsed =
                usage
                        .path("predictionsUsed")
                        .asInt(0);


        int creditsUsed =
                usage
                        .path("creditsUsed")
                        .asInt(0);


        int creditLimit =
                usage
                        .path("creditLimit")
                        .asInt(0);


        int creditsRemaining =
                usage
                        .path("creditsRemaining")
                        .asInt(0);


        JsonNode subscription =
                dashboard.path(
                        "subscription"
                );


        JsonNode plan =
                subscription.path(
                        "plan"
                );


        String planName =
                text(
                        plan,
                        "name",
                        "Unknown"
                );


        String planCode =
                text(
                        plan,
                        "code",
                        ""
                );


        int monthlyCredits =
                plan
                        .path("monthlyCredits")
                        .asInt(0);


        // =====================================================
        // Overview KPI cards
        // =====================================================

        Label overviewHeading =
                createSectionHeading(
                        "Overview"
                );


        FlowPane summaryCards =
                new FlowPane(
                        12,
                        12
                );


        summaryCards.setAlignment(
                Pos.CENTER_LEFT
        );


        summaryCards.setPrefWrapLength(
                900
        );


        summaryCards.getChildren().addAll(

                createStatCard(
                        "Total Predictions",
                        String.valueOf(
                                totalPredictions
                        ),
                        "All saved predictions",
                        "stat-blue"
                ),

                createStatCard(
                        "Average Probability",
                        averageProbability == null
                                ? "—"
                                : String.format(
                                Locale.US,
                                "%.1f%%",
                                averageProbability
                        ),
                        averageProbability == null
                                ? "No merge scores yet"
                                : "Average merge probability",
                        "stat-green"
                ),

                createStatCard(
                        "Credits Used",
                        String.valueOf(
                                creditsUsed
                        ),
                        "Current month",
                        "stat-orange"
                ),

                createStatCard(
                        "Credits Remaining",
                        String.valueOf(
                                creditsRemaining
                        ),
                        "of " +
                                creditLimit +
                                " monthly credits",
                        "stat-purple"
                )
        );


        // =====================================================
// Usage + Plan cards
// =====================================================

        VBox usageCard =
                createUsageCard(
                        predictionsUsed,
                        creditsUsed,
                        creditLimit,
                        creditsRemaining
                );


        VBox planCard =
                createPlanCard(
                        planName,
                        planCode,
                        monthlyCredits,
                        subscription
                );


// Keep ONLY Monthly Usage and Current Plan a little smaller.
// Do not add them to summaryCards and do not allow HBox to stretch them.

        usageCard.setPrefWidth(360);
        usageCard.setMinWidth(330);
        usageCard.setMaxWidth(360);

        planCard.setPrefWidth(360);
        planCard.setMinWidth(330);
        planCard.setMaxWidth(360);


        HBox accountCards =
                new HBox(
                        15,
                        usageCard,
                        planCard
                );


        accountCards.setAlignment(
                Pos.CENTER_LEFT
        );

        // =====================================================
        // Recent predictions
        // =====================================================

//        VBox recentPredictions =
//                createRecentPredictions(
//                        dashboard.path(
//                                "recentPredictions"
//                        )
//                );


        // =====================================================
        // Quick actions
        // =====================================================

//        VBox quickActions =
//                createQuickActions();


        // =====================================================
        // Add everything
        // =====================================================

        content.getChildren().addAll(

                overviewHeading,
                summaryCards,

                accountCards

//                recentPredictions,

//                quickActions
        );
    }


    // =========================================================
    // KPI card
    // =========================================================

    private VBox createStatCard(
            String title,
            String value,
            String subtitle,
            String styleClass
    ) {


        Label titleLabel =
                new Label(
                        title
                );


        titleLabel.getStyleClass().add(
                "muted-text"
        );


        Label valueLabel =
                new Label(
                        value
                );


        valueLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        25
                )
        );


        valueLabel.getStyleClass().add(
                "heading"
        );


        Label subtitleLabel =
                new Label(
                        subtitle
                );


        subtitleLabel.setWrapText(
                true
        );


        subtitleLabel
                .getStyleClass()
                .add(
                        "muted-text"
                );


        VBox card =
                new VBox(
                        7,
                        titleLabel,
                        valueLabel,
                        subtitleLabel
                );


        card.setPadding(
                new Insets(18)
        );


        card.setPrefWidth(
                215
        );


        card.setMinWidth(
                190
        );


        card.getStyleClass().addAll(
                "stat-card",
                styleClass
        );


        return card;
    }


    // =========================================================
    // Monthly usage
    // =========================================================

    private VBox createUsageCard(
            int predictionsUsed,
            int creditsUsed,
            int creditLimit,
            int creditsRemaining
    ) {


        Label title =
                createSectionHeading(
                        "Monthly Usage"
                );


        Label creditText =
                new Label(
                        creditsUsed +
                                " / " +
                                creditLimit +
                                " credits"
                );


        creditText.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22
                )
        );


        double progress =
                creditLimit <= 0
                        ? 0
                        : (double) creditsUsed /
                        creditLimit;


        progress =
                Math.max(
                        0,
                        Math.min(
                                1,
                                progress
                        )
                );


        ProgressBar usageBar =
                new ProgressBar(
                        progress
                );


        usageBar.setMaxWidth(
                Double.MAX_VALUE
        );


        Label remaining =
                new Label(
                        creditsRemaining +
                                " credits remaining"
                );


        remaining.getStyleClass().add(
                "muted-text"
        );


        Label predictions =
                new Label(
                        "Predictions this month: " +
                                predictionsUsed
                );


        predictions.getStyleClass().add(
                "muted-text"
        );


        VBox card =
                new VBox(
                        12,
                        title,
                        creditText,
                        usageBar,
                        remaining,
                        predictions
                );


        card.setPadding(
                new Insets(20)
        );


        card.getStyleClass().add(
                "surface-card"
        );


        return card;
    }


    // =========================================================
    // Subscription plan
    // =========================================================

    private VBox createPlanCard(
            String planName,
            String planCode,
            int monthlyCredits,
            JsonNode subscription
    ) {


        Label title =
                createSectionHeading(
                        "Current Plan"
                );


        Label planLabel =
                new Label(
                        planName
                );


        planLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        24
                )
        );


        planLabel.getStyleClass().add(
                "heading"
        );


        Label codeLabel =
                new Label(
                        planCode
                );


        codeLabel.getStyleClass().add(
                "muted-text"
        );


        Label credits =
                new Label(
                        monthlyCredits +
                                " credits per month"
                );


        credits.getStyleClass().add(
                "muted-text"
        );


        String status =
                text(
                        subscription,
                        "status",
                        "Unknown"
                );


        Label statusLabel =
                new Label(
                        "Subscription: " +
                                status
                );


        statusLabel.getStyleClass().add(
                "muted-text"
        );


        VBox card =
                new VBox(
                        10,
                        title,
                        planLabel,
                        codeLabel,
                        credits,
                        statusLabel
                );


        card.setPadding(
                new Insets(20)
        );


        card.getStyleClass().add(
                "surface-card"
        );


        return card;
    }


    // =========================================================
    // Recent predictions
    // =========================================================

//    private VBox createRecentPredictions(
//            JsonNode recentPredictions
//    ) {
//
//
//        Label title =
//                createSectionHeading(
//                        "Recent Predictions"
//                );
//
//
//        VBox predictionsBox =
//                new VBox(10);
//
//
//        if (
//                recentPredictions == null ||
//                        !recentPredictions.isArray() ||
//                        recentPredictions.isEmpty()
//        ) {
//
//
//            Label empty =
//                    new Label(
//                            "You have not created any predictions yet."
//                    );
//
//
//            empty.getStyleClass().add(
//                    "muted-text"
//            );
//
//
//            predictionsBox
//                    .getChildren()
//                    .add(
//                            empty
//                    );
//
//
//        } else {
//
//
//            for (
//                    JsonNode prediction :
//                    recentPredictions
//            ) {
//
//
//                predictionsBox
//                        .getChildren()
//                        .add(
//                                createRecentPredictionItem(
//                                        prediction
//                                )
//                        );
//            }
//        }
//
//
//        Button historyButton =
//                new Button(
//                        "View Full History"
//                );
//
//
//        historyButton.setMaxWidth(
//                Double.MAX_VALUE
//        );
//
//
//        historyButton.setPrefHeight(
//                40
//        );
//
//
//        historyButton.getStyleClass().add(
//                "secondary-button"
//        );
//
//
//        historyButton.setOnAction(
//                event ->
//                        navigator.loadHistoryScreen()
//        );
//
//
//        VBox card =
//                new VBox(
//                        15,
//                        title,
//                        predictionsBox,
//                        historyButton
//                );
//
//
//        card.setPadding(
//                new Insets(20)
//        );
//
//
//        card.getStyleClass().add(
//                "surface-card"
//        );
//
//
//        return card;
//    }


    // =========================================================
    // Recent prediction item
    // =========================================================

//    private HBox createRecentPredictionItem(
//            JsonNode prediction
//    ) {
//
//
//        JsonNode repository =
//                prediction.path(
//                        "repository"
//                );
//
//
//        JsonNode pullRequest =
//                prediction.path(
//                        "pullRequest"
//                );
//
//
//        String repositoryName =
//                text(
//                        repository,
//                        "owner",
//                        ""
//                )
//                        +
//                        "/"
//                        +
//                        text(
//                                repository,
//                                "name",
//                                ""
//                        );
//
//
//        String prTitle =
//                text(
//                        pullRequest,
//                        "title",
//                        "Pull Request"
//                );
//
//
//        int prNumber =
//                pullRequest
//                        .path("number")
//                        .asInt(0);
//
//
//        Double probability =
//                nullableDouble(
//                        prediction,
//                        "mergeProbability"
//                );
//
//
//        String quality =
//                text(
//                        prediction,
//                        "qualityLabel",
//                        "N/A"
//                );
//
//
//        // -----------------------------------------------------
//        // Left
//        // -----------------------------------------------------
//
//        Label repositoryLabel =
//                new Label(
//                        repositoryName
//                );
//
//
//        repositoryLabel.setFont(
//                Font.font(
//                        "Arial",
//                        FontWeight.BOLD,
//                        13
//                )
//        );
//
//
//        Label prLabel =
//                new Label(
//                        "#" +
//                                prNumber +
//                                " " +
//                                prTitle
//                );
//
//
//        prLabel.setWrapText(
//                true
//        );
//
//
//        prLabel.getStyleClass().add(
//                "muted-text"
//        );
//
//
//        VBox left =
//                new VBox(
//                        4,
//                        repositoryLabel,
//                        prLabel
//                );
//
//
//        HBox.setHgrow(
//                left,
//                Priority.ALWAYS
//        );
//
//
//        // -----------------------------------------------------
//        // Right
//        // -----------------------------------------------------
//
//        String probabilityText =
//                probability == null
//                        ? "—"
//                        : String.format(
//                        Locale.US,
//                        "%.1f%%",
//                        probability
//                );
//
//
//        Label probabilityLabel =
//                new Label(
//                        probabilityText
//                );
//
//
//        probabilityLabel.setFont(
//                Font.font(
//                        "Arial",
//                        FontWeight.BOLD,
//                        16
//                )
//        );
//
//
//        Label qualityLabel =
//                new Label(
//                        quality
//                );
//
//
//        qualityLabel.getStyleClass().add(
//                "muted-text"
//        );
//
//
//        VBox right =
//                new VBox(
//                        3,
//                        probabilityLabel,
//                        qualityLabel
//                );
//
//
//        right.setAlignment(
//                Pos.CENTER_RIGHT
//        );
//
//
//        // -----------------------------------------------------
//        // Row
//        // -----------------------------------------------------
//
//        HBox row =
//                new HBox(
//                        15,
//                        left,
//                        right
//                );
//
//
//        row.setAlignment(
//                Pos.CENTER_LEFT
//        );
//
//
//        row.setPadding(
//                new Insets(12)
//        );
//
//
//        row.setMaxWidth(
//                Double.MAX_VALUE
//        );
//
//
//        row.getStyleClass().add(
//                "prediction-item"
//        );
//
//
//        // Important:
//        // recentPredictions contains COMPLETE prediction objects,
//        // so we can reuse PredictionResultScreen directly.
//
//        row.setOnMouseClicked(
//                event -> {
//
//                    navigator
//                            .loadPredictionResultScreen(
//                                    prediction
//                            );
//                }
//        );
//
//
//        return row;
//    }
//

    // =========================================================
    // Quick actions
    // =========================================================

//    private VBox createQuickActions() {
//
//
//        Label title =
//                createSectionHeading(
//                        "Quick Actions"
//                );
//
//
//        Label description =
//                new Label(
//                        "Run a new pull-request analysis or review your previous predictions."
//                );
//
//
//        description.setWrapText(
//                true
//        );
//
//
//        description.getStyleClass().add(
//                "muted-text"
//        );
//
//
//        Button newPrediction =
//                new Button(
//                        "New Prediction"
//                );
//
//
//        newPrediction.setPrefHeight(
//                42
//        );
//
//
//        newPrediction.setMaxWidth(
//                Double.MAX_VALUE
//        );
//
//
//        newPrediction.getStyleClass().add(
//                "primary-button"
//        );
//
//
//        newPrediction.setOnAction(
//                event ->
//                        navigator
//                                .loadNewPredictionScreen()
//        );
//
//
//        Button history =
//                new Button(
//                        "Prediction History"
//                );
//
//
//        history.setPrefHeight(
//                42
//        );
//
//
//        history.setMaxWidth(
//                Double.MAX_VALUE
//        );
//
//
//        history.getStyleClass().add(
//                "secondary-button"
//        );
//
//
//        history.setOnAction(
//                event ->
//                        navigator
//                                .loadHistoryScreen()
//        );
//
//
//        HBox buttons =
//                new HBox(
//                        12,
//                        newPrediction,
//                        history
//                );
//
//
//        HBox.setHgrow(
//                newPrediction,
//                Priority.ALWAYS
//        );
//
//
//        HBox.setHgrow(
//                history,
//                Priority.ALWAYS
//        );
//
//
//        VBox card =
//                new VBox(
//                        12,
//                        title,
//                        description,
//                        buttons
//                );
//
//
//        card.setPadding(
//                new Insets(20)
//        );
//
//
//        card.getStyleClass().add(
//                "surface-card"
//        );
//
//
//        return card;
//    }
//

    // =========================================================
    // Error
    // =========================================================

    private void showDashboardError(
            String message
    ) {


        while (
                content.getChildren().size() > 2
        ) {

            content
                    .getChildren()
                    .remove(2);
        }


        Label error =
                new Label(
                        message
                );


        error.setWrapText(
                true
        );


        error.getStyleClass().add(
                "message-error"
        );


        Button retry =
                new Button(
                        "Retry"
                );


        retry.getStyleClass().add(
                "primary-button"
        );


        retry.setOnAction(
                event -> {

                    retry.setDisable(
                            true
                    );


                    error.setText(
                            "Loading dashboard..."
                    );


                    loadDashboard();
                }
        );


        VBox errorCard =
                new VBox(
                        12,
                        error,
                        retry
                );


        errorCard.setPadding(
                new Insets(20)
        );


        errorCard.getStyleClass().add(
                "surface-card"
        );


        content
                .getChildren()
                .add(
                        errorCard
                );
    }


    // =========================================================
    // Section heading
    // =========================================================

    private Label createSectionHeading(
            String text
    ) {


        Label label =
                new Label(
                        text
                );


        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18
                )
        );


        label.getStyleClass().add(
                "heading"
        );


        return label;
    }


    // =========================================================
    // JSON helpers
    // =========================================================

    private String text(
            JsonNode node,
            String field,
            String defaultValue
    ) {


        if (
                node == null ||
                        node.isNull() ||
                        node.isMissingNode()
        ) {

            return defaultValue;
        }


        JsonNode value =
                node.get(field);


        if (
                value == null ||
                        value.isNull()
        ) {

            return defaultValue;
        }


        return value.asText();
    }


    private Double nullableDouble(
            JsonNode node,
            String field
    ) {


        if (
                node == null ||
                        node.isNull() ||
                        node.isMissingNode()
        ) {

            return null;
        }


        JsonNode value =
                node.get(field);


        if (
                value == null ||
                        value.isNull()
        ) {

            return null;
        }


        return value.asDouble();
    }
}
