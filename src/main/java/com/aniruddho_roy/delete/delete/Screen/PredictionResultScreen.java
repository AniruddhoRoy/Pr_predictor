package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PredictionResultScreen extends DashboardBase {

    private final String repository;
    private final String pullRequest;
    private final double mergeProbability;
    private final String quality;

    public PredictionResultScreen(
            NAVIGATOR navigator,
            boolean subscribed,
            String repository,
            String pullRequest,
            double mergeProbability,
            String quality
    ) {
        super(navigator, subscribed);

        this.repository = repository;
        this.pullRequest = pullRequest;
        this.mergeProbability = mergeProbability;
        this.quality = quality;

        setCenter(createResultCenter(navigator));
    }


    private ScrollPane createResultCenter(NAVIGATOR navigator) {

        // =========================
        // Header
        // =========================

        Label heading = new Label("Prediction Result");
        heading.setFont(
                Font.font("Arial", FontWeight.BOLD, 23)
        );
        heading.getStyleClass().add("heading");


        Label description = new Label(
                "Analysis completed successfully for this pull request."
        );

        description.getStyleClass().add("muted-text");


        // =========================
        // Repository Information
        // =========================

        Label repositoryTitle = new Label("Repository");
        repositoryTitle.getStyleClass().add("muted-text");

        Label repositoryValue = new Label(repository);
        repositoryValue.setFont(
                Font.font("Arial", FontWeight.BOLD, 17)
        );
        repositoryValue.getStyleClass().add("heading");


        Label prTitle = new Label("Pull Request");
        prTitle.getStyleClass().add("muted-text");

        Label prValue = new Label(pullRequest);
        prValue.setFont(
                Font.font("Arial", FontWeight.BOLD, 15)
        );


        VBox repositoryInfo = new VBox(
                6,
                repositoryTitle,
                repositoryValue,
                prTitle,
                prValue
        );

        repositoryInfo.setPadding(new Insets(18));
        repositoryInfo.getStyleClass().add("surface-card");


        // =========================
        // Merge Probability Card
        // =========================

        Label probabilityTitle =
                new Label("Merge Probability");

        probabilityTitle.getStyleClass().add("muted-text");


        Label probabilityValue = new Label(
                String.format("%.0f%%", mergeProbability)
        );

        probabilityValue.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        34
                )
        );


        if (mergeProbability >= 70) {

            probabilityValue
                    .getStyleClass()
                    .add("probability-high");

        } else if (mergeProbability >= 50) {

            probabilityValue
                    .getStyleClass()
                    .add("probability-medium");

        } else {

            probabilityValue
                    .getStyleClass()
                    .add("probability-low");
        }


        ProgressBar probabilityBar =
                new ProgressBar(mergeProbability / 100.0);

        probabilityBar.setMaxWidth(Double.MAX_VALUE);


        Label probabilityMessage = new Label(
                getProbabilityMessage()
        );

        probabilityMessage.getStyleClass().add("muted-text");
        probabilityMessage.setWrapText(true);


        VBox probabilityCard = new VBox(
                12,
                probabilityTitle,
                probabilityValue,
                probabilityBar,
                probabilityMessage
        );

        probabilityCard.setPadding(new Insets(20));
        probabilityCard.setMaxWidth(Double.MAX_VALUE);
        probabilityCard.getStyleClass().add("stat-card");

        HBox.setHgrow(
                probabilityCard,
                Priority.ALWAYS
        );


        // =========================
        // PR Quality Card
        // =========================

        Label qualityTitle =
                new Label("PR Quality");

        qualityTitle.getStyleClass().add("muted-text");


        Label qualityValue =
                new Label(quality);

        qualityValue.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        30
                )
        );

        qualityValue.getStyleClass().add("heading");


        Label qualityDescription = new Label(
                getQualityDescription()
        );

        qualityDescription.getStyleClass().add("muted-text");
        qualityDescription.setWrapText(true);


        VBox qualityCard = new VBox(
                12,
                qualityTitle,
                qualityValue,
                qualityDescription
        );

        qualityCard.setPadding(new Insets(20));
        qualityCard.setMaxWidth(Double.MAX_VALUE);
        qualityCard.getStyleClass().add("stat-card");

        HBox.setHgrow(
                qualityCard,
                Priority.ALWAYS
        );


        // =========================
        // Main Results
        // =========================

        HBox resultCards = new HBox(
                15,
                probabilityCard,
                qualityCard
        );

        resultCards.setAlignment(Pos.CENTER);


        // =========================
        // Analysis / Factors
        // =========================

        Label analysisHeading =
                new Label("Analysis");

        analysisHeading.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18
                )
        );

        analysisHeading.getStyleClass().add("heading");


        VBox factors = new VBox(10);

        factors.getChildren().addAll(

                createFactor(
                        "Code Changes",
                        "The size of the pull request was considered during prediction."
                ),

                createFactor(
                        "Files Changed",
                        "The number of modified files contributed to the prediction."
                ),

                createFactor(
                        "Testing",
                        "Test coverage and added tests can improve pull-request quality."
                ),

                createFactor(
                        "Model Prediction",
                        "The machine-learning model analyzed the supplied PR features."
                )
        );


        VBox analysisCard = new VBox(
                15,
                analysisHeading,
                factors
        );

        analysisCard.setPadding(new Insets(20));
        analysisCard.setMaxWidth(Double.MAX_VALUE);
        analysisCard.getStyleClass().add("surface-card");


        // =========================
        // Recommendation
        // =========================

        Label recommendationHeading =
                new Label("Recommendation");

        recommendationHeading.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18
                )
        );

        recommendationHeading
                .getStyleClass()
                .add("heading");


        Label recommendationText =
                new Label(getRecommendation());

        recommendationText.setWrapText(true);

        recommendationText
                .getStyleClass()
                .add("muted-text");


        VBox recommendationCard = new VBox(
                10,
                recommendationHeading,
                recommendationText
        );

        recommendationCard.setPadding(new Insets(20));
        recommendationCard.setMaxWidth(Double.MAX_VALUE);
        recommendationCard.getStyleClass().add("surface-card");


        // =========================
        // Buttons
        // =========================

        Button newPredictionButton =
                new Button("New Prediction");

        newPredictionButton.setPrefHeight(42);
        newPredictionButton.setMaxWidth(Double.MAX_VALUE);

        newPredictionButton
                .getStyleClass()
                .add("primary-button");

        newPredictionButton.setOnAction(event ->
                navigator.loadNewPredictionScreen()
        );


        Button historyButton =
                new Button("View History");

        historyButton.setPrefHeight(42);
        historyButton.setMaxWidth(Double.MAX_VALUE);

        historyButton
                .getStyleClass()
                .add("secondary-button");

        historyButton.setOnAction(event ->
                navigator.loadHistoryScreen()
        );


        HBox buttons = new HBox(
                12,
                newPredictionButton,
                historyButton
        );

        HBox.setHgrow(
                newPredictionButton,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                historyButton,
                Priority.ALWAYS
        );


        // =========================
        // Main Content
        // =========================

        VBox content = new VBox(
                18,
                heading,
                description,
                repositoryInfo,
                resultCards,
                analysisCard,
                recommendationCard,
                buttons
        );

        content.setPadding(new Insets(25));
        content.setMaxWidth(Double.MAX_VALUE);

        content.getStyleClass().add(
                "dashboard-content"
        );


        ScrollPane scrollPane =
                new ScrollPane(content);

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.getStyleClass().add(
                "transparent-scroll-pane"
        );

        return scrollPane;
    }


    // =========================
    // Factor Component
    // =========================

    private VBox createFactor(
            String title,
            String description
    ) {

        Label titleLabel = new Label(title);

        titleLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        13
                )
        );

        titleLabel.getStyleClass().add("heading");


        Label descriptionLabel =
                new Label(description);

        descriptionLabel
                .getStyleClass()
                .add("muted-text");

        descriptionLabel.setWrapText(true);


        VBox box = new VBox(
                4,
                titleLabel,
                descriptionLabel
        );

        box.setPadding(
                new Insets(12)
        );

        box.setMaxWidth(
                Double.MAX_VALUE
        );

        box.getStyleClass().add(
                "prediction-item"
        );

        return box;
    }


    // =========================
    // Helper Methods
    // =========================

    private String getProbabilityMessage() {

        if (mergeProbability >= 70) {

            return "This pull request has a high predicted chance of being merged.";

        } else if (mergeProbability >= 50) {

            return "This pull request has a moderate predicted chance of being merged.";

        } else {

            return "This pull request currently has a low predicted chance of being merged.";
        }
    }


    private String getQualityDescription() {

        if (quality.equalsIgnoreCase("High")) {

            return "The pull request shows strong overall quality.";

        } else if (quality.equalsIgnoreCase("Medium")) {

            return "The pull request has acceptable quality but may need some improvements.";

        } else {

            return "The pull request may require additional improvements before merging.";
        }
    }


    private String getRecommendation() {

        if (mergeProbability >= 70) {

            return "The model indicates favorable conditions for this pull request. "
                    + "Review the code and test results before making the final merge decision.";

        } else if (mergeProbability >= 50) {

            return "Consider reviewing the changed files, tests and code quality "
                    + "before proceeding with the merge.";

        } else {

            return "The prediction indicates that improvements may be needed. "
                    + "Review testing, code changes and pull-request quality before merging.";
        }
    }
}