package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class PredictionResultScreen extends DashboardBase {

    private final JsonNode result;


    // =========================================================
    // Constructor
    // =========================================================

    public PredictionResultScreen(
            NAVIGATOR navigator,
            boolean subscribed,
            JsonNode result
    ) {

        super(navigator, subscribed);

        this.result = result;

        setCenter(
                createResultCenter(navigator)
        );
    }


    // =========================================================
    // Main Screen
    // =========================================================

    private ScrollPane createResultCenter(
            NAVIGATOR navigator
    ) {

        VBox content = new VBox(18);

        content.setPadding(
                new Insets(25)
        );

        content.setMaxWidth(
                Double.MAX_VALUE
        );

        content.getStyleClass().add(
                "dashboard-content"
        );


        // =====================================================
        // Header
        // =====================================================

        Label heading =
                new Label("Prediction Result");

        heading.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        24
                )
        );

        heading.getStyleClass().add(
                "heading"
        );


        Label subtitle =
                new Label(
                        "Machine-learning analysis of the selected GitHub pull request."
                );

        subtitle.setWrapText(true);

        subtitle.getStyleClass().add(
                "muted-text"
        );


        content.getChildren().addAll(
                heading,
                subtitle
        );


        // =====================================================
        // PR Overview
        // =====================================================

        content.getChildren().add(
                createPullRequestOverview()
        );


        // =====================================================
        // Main Prediction Summary
        // =====================================================

        content.getChildren().add(
                createPredictionSummary()
        );


        // =====================================================
        // Change Statistics
        // =====================================================

        content.getChildren().add(
                createChangeStatistics()
        );


        // =====================================================
        // Repository / Model Context
        // =====================================================

        content.getChildren().add(
                createContextSection()
        );


        // =====================================================
        // Analysis Factors
        // =====================================================

        content.getChildren().add(
                createAnalysisSection()
        );


        // =====================================================
        // Recommendation
        // =====================================================

        content.getChildren().add(
                createRecommendationSection()
        );


        // =====================================================
        // Buttons
        // =====================================================

        Button newPredictionButton =
                new Button("New Prediction");

        newPredictionButton.setPrefHeight(42);

        newPredictionButton.setMaxWidth(
                Double.MAX_VALUE
        );

        newPredictionButton
                .getStyleClass()
                .add("primary-button");

        newPredictionButton.setOnAction(
                event ->
                        navigator.loadNewPredictionScreen()
        );


        Button historyButton =
                new Button("View History");

        historyButton.setPrefHeight(42);

        historyButton.setMaxWidth(
                Double.MAX_VALUE
        );

        historyButton
                .getStyleClass()
                .add("secondary-button");

        historyButton.setOnAction(
                event ->
                        navigator.loadHistoryScreen()
        );


        HBox buttons =
                new HBox(
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


        content.getChildren().add(
                buttons
        );


        // =====================================================
        // ScrollPane
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane(content);

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane
                .getStyleClass()
                .add("transparent-scroll-pane");


        return scrollPane;
    }


    // =========================================================
    // Pull Request Overview
    // =========================================================

    private VBox createPullRequestOverview() {

        JsonNode repository =
                result.path("repository");

        JsonNode pullRequest =
                result.path("pullRequest");


        String owner =
                text(
                        repository,
                        "owner",
                        ""
                );

        String repositoryName =
                text(
                        repository,
                        "name",
                        "Unknown repository"
                );


        String fullRepositoryName;

        if (owner.isBlank()) {

            fullRepositoryName =
                    repositoryName;

        } else {

            fullRepositoryName =
                    owner +
                            "/" +
                            repositoryName;
        }


        int number =
                pullRequest
                        .path("number")
                        .asInt(-1);


        String title =
                text(
                        pullRequest,
                        "title",
                        "Pull Request"
                );


        String state =
                text(
                        pullRequest,
                        "state",
                        "Unknown"
                );


        String author =
                text(
                        pullRequest,
                        "authorLogin",
                        "Unknown"
                );


        Label sectionTitle =
                createSectionTitle(
                        "Pull Request"
                );


        Label repositoryLabel =
                new Label(
                        fullRepositoryName
                );

        repositoryLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        18
                )
        );

        repositoryLabel
                .getStyleClass()
                .add("heading");


        String prText;

        if (number > 0) {

            prText =
                    "#" +
                            number +
                            "  " +
                            title;

        } else {

            prText =
                    title;
        }


        Label prTitle =
                new Label(prText);

        prTitle.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        15
                )
        );

        prTitle.setWrapText(true);


        FlowPane metadata =
                new FlowPane();

        metadata.setHgap(18);
        metadata.setVgap(8);


        metadata.getChildren().addAll(

                createInlineInfo(
                        "State",
                        state
                ),

                createInlineInfo(
                        "Author",
                        "@" + author
                )
        );


        VBox card =
                new VBox(
                        10,
                        sectionTitle,
                        repositoryLabel,
                        prTitle,
                        metadata
                );


        card.setPadding(
                new Insets(20)
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.getStyleClass().add(
                "surface-card"
        );


        return card;
    }


    // =========================================================
    // Prediction Summary
    // =========================================================

    private VBox createPredictionSummary() {

        Label title =
                createSectionTitle(
                        "Prediction Summary"
                );


        FlowPane cards =
                new FlowPane();

        cards.setHgap(15);
        cards.setVgap(15);

        cards.setPrefWrapLength(
                850
        );


        Double mergeProbability =
                nullableDouble(
                        result,
                        "mergeProbability"
                );


        Double qualityScore =
                nullableDouble(
                        result,
                        "qualityScore"
                );


        String qualityLabel =
                text(
                        result,
                        "qualityLabel",
                        "N/A"
                );


        String modelId =
                text(
                        result,
                        "modelId",
                        "Unknown"
                );


        String predictionType =
                formatPredictionType(
                        text(
                                result,
                                "predictionType",
                                ""
                        )
                );


        if (mergeProbability != null) {

            cards.getChildren().add(
                    createScoreCard(
                            "Merge Probability",
                            mergeProbability
                    )
            );
        }


        if (qualityScore != null) {

            cards.getChildren().add(
                    createQualityCard(
                            qualityLabel,
                            qualityScore
                    )
            );
        }


        cards.getChildren().add(
                createTextStatCard(
                        "Model",
                        modelId,
                        "Selected prediction model"
                )
        );


        cards.getChildren().add(
                createTextStatCard(
                        "Prediction Type",
                        predictionType,
                        "Requested analysis type"
                )
        );


        VBox section =
                new VBox(
                        12,
                        title,
                        cards
                );


        return section;
    }


    // =========================================================
    // Merge Probability Card
    // =========================================================

    private VBox createScoreCard(
            String title,
            double value
    ) {

        Label titleLabel =
                new Label(title);

        titleLabel
                .getStyleClass()
                .add("muted-text");


        Label valueLabel =
                new Label(
                        String.format(
                                Locale.US,
                                "%.1f%%",
                                value
                        )
                );


        valueLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        32
                )
        );


        applyProbabilityStyle(
                valueLabel,
                value
        );


        ProgressBar progressBar =
                new ProgressBar(
                        clampProbability(
                                value
                        )
                );

        progressBar.setMaxWidth(
                Double.MAX_VALUE
        );


        Label description =
                new Label(
                        getProbabilityDescription(
                                value
                        )
                );

        description.setWrapText(true);

        description
                .getStyleClass()
                .add("muted-text");


        VBox card =
                new VBox(
                        10,
                        titleLabel,
                        valueLabel,
                        progressBar,
                        description
                );


        card.setPadding(
                new Insets(18)
        );

        card.setPrefWidth(240);

        card.getStyleClass().add(
                "stat-card"
        );


        return card;
    }


    // =========================================================
    // Quality Card
    // =========================================================

    private VBox createQualityCard(
            String qualityLabel,
            double qualityScore
    ) {

        Label title =
                new Label("PR Quality");

        title.getStyleClass().add(
                "muted-text"
        );


        Label label =
                new Label(
                        qualityLabel
                );

        label.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        23
                )
        );


        applyProbabilityStyle(
                label,
                qualityScore
        );


        Label score =
                new Label(
                        String.format(
                                Locale.US,
                                "%.1f%% score",
                                qualityScore
                        )
                );

        score.getStyleClass().add(
                "muted-text"
        );


        ProgressBar progress =
                new ProgressBar(
                        clampProbability(
                                qualityScore
                        )
                );

        progress.setMaxWidth(
                Double.MAX_VALUE
        );


        VBox card =
                new VBox(
                        10,
                        title,
                        label,
                        score,
                        progress
                );


        card.setPadding(
                new Insets(18)
        );

        card.setPrefWidth(240);

        card.getStyleClass().add(
                "stat-card"
        );


        return card;
    }


    // =========================================================
    // Simple Summary Card
    // =========================================================

    private VBox createTextStatCard(
            String title,
            String value,
            String description
    ) {

        Label titleLabel =
                new Label(title);

        titleLabel
                .getStyleClass()
                .add("muted-text");


        Label valueLabel =
                new Label(value);

        valueLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        20
                )
        );

        valueLabel.setWrapText(true);

        valueLabel
                .getStyleClass()
                .add("heading");


        Label descriptionLabel =
                new Label(description);

        descriptionLabel.setWrapText(true);

        descriptionLabel
                .getStyleClass()
                .add("muted-text");


        VBox card =
                new VBox(
                        10,
                        titleLabel,
                        valueLabel,
                        descriptionLabel
                );


        card.setPadding(
                new Insets(18)
        );

        card.setPrefWidth(240);

        card.getStyleClass().add(
                "stat-card"
        );


        return card;
    }


    // =========================================================
    // Change Statistics
    // =========================================================

    private VBox createChangeStatistics() {

        JsonNode features =
                result.path("features");


        Label title =
                createSectionTitle(
                        "Code Change Statistics"
                );


        GridPane grid =
                new GridPane();

        grid.setHgap(12);
        grid.setVgap(12);


        ColumnConstraints column =
                new ColumnConstraints();

        column.setPercentWidth(25);

        grid.getColumnConstraints().addAll(
                column,
                column,
                column,
                column
        );


        grid.add(
                createMetricBox(
                        "Lines Added",
                        "+" + features
                                .path("total_lines_added")
                                .asInt(0)
                ),
                0,
                0
        );


        grid.add(
                createMetricBox(
                        "Lines Deleted",
                        "-" + features
                                .path("total_lines_deleted")
                                .asInt(0)
                ),
                1,
                0
        );


        grid.add(
                createMetricBox(
                        "Total Changes",
                        String.valueOf(
                                features
                                        .path("total_lines_changed")
                                        .asInt(0)
                        )
                ),
                2,
                0
        );


        grid.add(
                createMetricBox(
                        "Files Touched",
                        String.valueOf(
                                features
                                        .path("total_files_touched")
                                        .asInt(0)
                        )
                ),
                3,
                0
        );


        grid.add(
                createMetricBox(
                        "Files Added",
                        String.valueOf(
                                features
                                        .path("files_added")
                                        .asInt(0)
                        )
                ),
                0,
                1
        );


        grid.add(
                createMetricBox(
                        "Files Modified",
                        String.valueOf(
                                features
                                        .path("files_modified")
                                        .asInt(0)
                        )
                ),
                1,
                1
        );


        grid.add(
                createMetricBox(
                        "Files Deleted",
                        String.valueOf(
                                features
                                        .path("files_deleted")
                                        .asInt(0)
                        )
                ),
                2,
                1
        );


        grid.add(
                createMetricBox(
                        "Commits",
                        String.valueOf(
                                features
                                        .path("total_commits")
                                        .asInt(0)
                        )
                ),
                3,
                1
        );


        VBox card =
                new VBox(
                        15,
                        title,
                        grid
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
    // Metric Box
    // =========================================================

    private VBox createMetricBox(
            String title,
            String value
    ) {

        Label valueLabel =
                new Label(value);

        valueLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        22
                )
        );

        valueLabel
                .getStyleClass()
                .add("heading");


        Label titleLabel =
                new Label(title);

        titleLabel.setWrapText(true);

        titleLabel
                .getStyleClass()
                .add("muted-text");


        VBox box =
                new VBox(
                        4,
                        valueLabel,
                        titleLabel
                );


        box.setAlignment(
                Pos.CENTER
        );

        box.setPadding(
                new Insets(14)
        );

        box.setMaxWidth(
                Double.MAX_VALUE
        );

        box.getStyleClass().add(
                "prediction-item"
        );


        return box;
    }


    // =========================================================
    // Repository + Feature Context
    // =========================================================

    private VBox createContextSection() {

        JsonNode features =
                result.path("features");


        Label title =
                createSectionTitle(
                        "Prediction Context"
                );


        GridPane details =
                new GridPane();

        details.setHgap(25);
        details.setVgap(10);


        ColumnConstraints left =
                new ColumnConstraints();

        left.setPercentWidth(50);


        ColumnConstraints right =
                new ColumnConstraints();

        right.setPercentWidth(50);


        details.getColumnConstraints().addAll(
                left,
                right
        );


        details.add(
                createContextItem(
                        "Language",
                        text(
                                features,
                                "language",
                                "Unknown"
                        )
                ),
                0,
                0
        );


        details.add(
                createContextItem(
                        "Agent",
                        text(
                                features,
                                "agent",
                                "Unknown"
                        )
                ),
                1,
                0
        );


        details.add(
                createContextItem(
                        "Task Type",
                        text(
                                features,
                                "task_type",
                                "Unknown"
                        )
                ),
                0,
                1
        );


        details.add(
                createContextItem(
                        "Repository Stars",
                        String.valueOf(
                                features
                                        .path("stars")
                                        .asInt(0)
                        )
                ),
                1,
                1
        );


        details.add(
                createContextItem(
                        "Repository Forks",
                        String.valueOf(
                                features
                                        .path("forks")
                                        .asInt(0)
                        )
                ),
                0,
                2
        );


        details.add(
                createContextItem(
                        "Title Words",
                        String.valueOf(
                                features
                                        .path("title_word_count")
                                        .asInt(0)
                        )
                ),
                1,
                2
        );


        details.add(
                createContextItem(
                        "PR Body Words",
                        String.valueOf(
                                features
                                        .path("body_word_count")
                                        .asInt(0)
                        )
                ),
                0,
                3
        );


        details.add(
                createContextItem(
                        "Status",
                        text(
                                result,
                                "status",
                                "Unknown"
                        )
                ),
                1,
                3
        );


        VBox card =
                new VBox(
                        15,
                        title,
                        details
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
    // Context Item
    // =========================================================

    private VBox createContextItem(
            String title,
            String value
    ) {

        Label titleLabel =
                new Label(title);

        titleLabel.getStyleClass().add(
                "muted-text"
        );


        Label valueLabel =
                new Label(value);

        valueLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );

        valueLabel.setWrapText(true);


        return new VBox(
                3,
                titleLabel,
                valueLabel
        );
    }


    // =========================================================
    // Analysis Factors
    // =========================================================

    private VBox createAnalysisSection() {

        Label heading =
                createSectionTitle(
                        "Why the Model Produced This Result"
                );


        VBox factorsContainer =
                new VBox(10);


        JsonNode factorsNode =
                result.path("factors");


        if (
                factorsNode.isArray() &&
                        !factorsNode.isEmpty()
        ) {

            List<JsonNode> factors =
                    new ArrayList<>();


            for (
                    JsonNode factor :
                    factorsNode
            ) {

                factors.add(factor);
            }


            factors.sort(
                    Comparator.comparingInt(
                            factor ->
                                    factor
                                            .path("order")
                                            .asInt(
                                                    Integer.MAX_VALUE
                                            )
                    )
            );


            for (
                    JsonNode factor :
                    factors
            ) {

                factorsContainer
                        .getChildren()
                        .add(
                                createFactorCard(
                                        factor
                                )
                        );
            }

        } else {

            Label empty =
                    new Label(
                            "No analysis factors were returned."
                    );

            empty.getStyleClass().add(
                    "muted-text"
            );

            factorsContainer
                    .getChildren()
                    .add(empty);
        }


        VBox card =
                new VBox(
                        15,
                        heading,
                        factorsContainer
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
    // Factor Card
    // =========================================================

    private VBox createFactorCard(
            JsonNode factor
    ) {

        String name =
                text(
                        factor,
                        "name",
                        "Factor"
                );


        String impact =
                text(
                        factor,
                        "impact",
                        "NEUTRAL"
                );


        String description =
                text(
                        factor,
                        "description",
                        ""
                );


        Label nameLabel =
                new Label(name);

        nameLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        14
                )
        );


        Label impactLabel =
                new Label(
                        "Impact: " +
                                formatImpact(
                                        impact
                                )
                );

        impactLabel.getStyleClass().add(
                "muted-text"
        );


        Label descriptionLabel =
                new Label(description);

        descriptionLabel.setWrapText(true);

        descriptionLabel
                .getStyleClass()
                .add("muted-text");


        VBox box =
                new VBox(
                        5,
                        nameLabel,
                        impactLabel,
                        descriptionLabel
                );


        box.setPadding(
                new Insets(14)
        );

        box.setMaxWidth(
                Double.MAX_VALUE
        );

        box.getStyleClass().add(
                "prediction-item"
        );


        return box;
    }


    // =========================================================
    // Recommendation
    // =========================================================

    private VBox createRecommendationSection() {

        Label heading =
                createSectionTitle(
                        "Recommendation"
                );


        String recommendation =
                text(
                        result,
                        "recommendation",
                        "No recommendation was returned."
                );


        Label message =
                new Label(
                        recommendation
                );

        message.setWrapText(true);

        message.setFont(
                Font.font(
                        "Arial",
                        14
                )
        );


        VBox card =
                new VBox(
                        12,
                        heading,
                        message
                );


        card.setPadding(
                new Insets(20)
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.getStyleClass().add(
                "surface-card"
        );


        return card;
    }


    // =========================================================
    // Inline Metadata
    // =========================================================

    private HBox createInlineInfo(
            String title,
            String value
    ) {

        Label titleLabel =
                new Label(
                        title + ":"
                );

        titleLabel.getStyleClass().add(
                "muted-text"
        );


        Label valueLabel =
                new Label(value);

        valueLabel.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12
                )
        );


        HBox box =
                new HBox(
                        5,
                        titleLabel,
                        valueLabel
                );

        box.setAlignment(
                Pos.CENTER_LEFT
        );


        return box;
    }


    // =========================================================
    // Section Heading
    // =========================================================

    private Label createSectionTitle(
            String text
    ) {

        Label label =
                new Label(text);

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
    // Styling Helpers
    // =========================================================

    private void applyProbabilityStyle(
            Label label,
            double score
    ) {

        if (score >= 80) {

            label
                    .getStyleClass()
                    .add(
                            "probability-high"
                    );

        } else if (score >= 50) {

            label
                    .getStyleClass()
                    .add(
                            "probability-medium"
                    );

        } else {

            label
                    .getStyleClass()
                    .add(
                            "probability-low"
                    );
        }
    }


    private String getProbabilityDescription(
            double probability
    ) {

        if (probability >= 80) {

            return "The model predicts a strong likelihood that this pull request will be merged.";

        } else if (probability >= 65) {

            return "The model predicts a reasonably favorable merge likelihood.";

        } else if (probability >= 50) {

            return "The prediction is uncertain and the pull request should be reviewed carefully.";

        } else {

            return "The model predicts a relatively low merge likelihood.";
        }
    }


    private double clampProbability(
            double probability
    ) {

        return Math.max(
                0.0,
                Math.min(
                        1.0,
                        probability / 100.0
                )
        );
    }


    // =========================================================
    // JSON Helpers
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


        String text =
                value.asText();


        if (text == null) {

            return defaultValue;
        }


        return text;
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


    // =========================================================
    // Formatting Helpers
    // =========================================================

    private String formatPredictionType(
            String type
    ) {

        if (type == null) {

            return "";
        }


        return switch (type) {

            case "MERGE_PROBABILITY" ->
                    "Merge Probability";

            case "PR_QUALITY" ->
                    "PR Quality";

            case "BOTH" ->
                    "Both";

            default ->
                    type;
        };
    }


    private String formatImpact(
            String impact
    ) {

        if (
                impact == null ||
                        impact.isBlank()
        ) {

            return "Neutral";
        }


        String lower =
                impact.toLowerCase(
                        Locale.ROOT
                );


        return Character.toUpperCase(
                lower.charAt(0)
        ) +
                lower.substring(1);
    }
}