package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HistoryScreen extends DashboardBase {

    public HistoryScreen(NAVIGATOR navigator) {
        this(navigator, false);
    }

    public HistoryScreen(
            NAVIGATOR navigator,
            boolean subscribed
    ) {
        super(navigator, subscribed);
        setCenter(createHistoryCenter());
    }

    private ScrollPane createHistoryCenter() {

        Label heading = new Label("Prediction History");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 23));
        heading.getStyleClass().add("heading");

        Label description = new Label(
                "Review your previous pull-request predictions."
        );
        description.setFont(Font.font("Arial", 14));
        description.getStyleClass().add("muted-text");

        TextField searchField = new TextField();
        searchField.setPromptText("Search repository or pull request");
        searchField.setPrefWidth(300);
        searchField.getStyleClass().add("login-input");

        Button exportButton = new Button("Export History");
        exportButton.getStyleClass().add("secondary-button");

        Label message = new Label();
        message.getStyleClass().add("muted-text");

        exportButton.setOnAction(event ->
                message.setText("History export will be connected to the backend.")
        );

        HBox toolbar = new HBox(
                12,
                searchField,
                exportButton
        );
        toolbar.setAlignment(Pos.CENTER_LEFT);

        Label tableTitle = new Label("Recent predictions");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        tableTitle.getStyleClass().add("heading");

        VBox predictionList = new VBox(8);

        predictionList.getChildren().addAll(
                createPredictionRow(
                        "spring-projects/spring",
                        "#32541",
                        "87%",
                        "Excellent",
                        "Today, 10:42"
                ),
                createPredictionRow(
                        "openjdk/jdk",
                        "#21430",
                        "74%",
                        "Good",
                        "Yesterday, 16:20"
                ),
                createPredictionRow(
                        "microsoft/vscode",
                        "#22801",
                        "63%",
                        "Good",
                        "Aug 27, 09:15"
                ),
                createPredictionRow(
                        "facebook/react",
                        "#30125",
                        "91%",
                        "Excellent",
                        "Aug 25, 14:03"
                ),
                createPredictionRow(
                        "tensorflow/tensorflow",
                        "#72940",
                        "58%",
                        "Needs review",
                        "Aug 23, 11:38"
                )
        );

        VBox content = new VBox(
                18,
                heading,
                description,
                toolbar,
                message,
                tableTitle,
                predictionList
        );

        content.setPadding(new Insets(25));
        content.setMaxWidth(Double.MAX_VALUE);
        content.getStyleClass().addAll(
                "surface-card",
                "dashboard-content"
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );
        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );
        scrollPane.getStyleClass().add(
                "transparent-scroll-pane"
        );

        return scrollPane;
    }

    private HBox createPredictionRow(
            String repository,
            String pullRequest,
            String probability,
            String quality,
            String date
    ) {
        Label repositoryLabel = new Label(repository);
        repositoryLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 13)
        );
        repositoryLabel.getStyleClass().add("heading");

        Label pullRequestLabel = new Label(pullRequest);
        pullRequestLabel.setFont(Font.font("Arial", 11));
        pullRequestLabel.getStyleClass().add("muted-text");

        VBox details = new VBox(
                3,
                repositoryLabel,
                pullRequestLabel
        );

        RegionSpacer spacer = new RegionSpacer();

        Label probabilityLabel = new Label(probability);
        probabilityLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 13)
        );

        int numericProbability = Integer.parseInt(
                probability.replace("%", "")
        );

        if (numericProbability >= 70) {
            probabilityLabel.getStyleClass().add("probability-high");
        } else if (numericProbability >= 50) {
            probabilityLabel.getStyleClass().add("probability-medium");
        } else {
            probabilityLabel.getStyleClass().add("probability-low");
        }

        Label qualityLabel = new Label(quality);
        qualityLabel.getStyleClass().add("muted-text");

        Label dateLabel = new Label(date);
        dateLabel.getStyleClass().add("muted-text");

        HBox row = new HBox(
                15,
                details,
                spacer,
                probabilityLabel,
                qualityLabel,
                dateLabel
        );

        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12));
        row.setMaxWidth(Double.MAX_VALUE);
        row.getStyleClass().add("prediction-item");

        return row;
    }

    private static class RegionSpacer extends javafx.scene.layout.Region {
    }
}