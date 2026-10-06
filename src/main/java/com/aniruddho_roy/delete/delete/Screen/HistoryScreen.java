package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.Backend.Predictions;
import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HistoryScreen extends DashboardBase {

    private static final int HISTORY_LIMIT = 50;

    private final Predictions predictions = new Predictions();

    private final VBox predictionList = new VBox(8);
    private final Label message = new Label();
    private final TextField searchField = new TextField();
    private final Button searchButton = new Button("Search");

    // guards against out-of-order responses when searching quickly
    private int requestCounter = 0;

    public HistoryScreen(NAVIGATOR navigator) {
        this(navigator, false);
    }

    public HistoryScreen(NAVIGATOR navigator, boolean subscribed) {
        super(navigator, subscribed);
        setCenter(createHistoryCenter());
        loadHistory();
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

        searchField.setPromptText("Search by PR title or URL");
        searchField.setOnAction(e -> loadHistory());
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchButton.setOnAction(e -> loadHistory());

        HBox searchBar = new HBox(10, searchField, searchButton);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        message.getStyleClass().add("muted-text");
        message.setWrapText(true);

        Label tableTitle = new Label("Recent predictions");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        tableTitle.getStyleClass().add("heading");

        VBox content = new VBox(
                18,
                heading,
                description,
                searchBar,
                message,
                tableTitle,
                predictionList
        );

        content.setPadding(new Insets(25));
        content.setMaxWidth(Double.MAX_VALUE);
        content.getStyleClass().addAll("surface-card", "dashboard-content");

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("transparent-scroll-pane");

        return scrollPane;
    }

    // =========================================================
    // Load history from GET /history (off the FX thread)
    // =========================================================

    private void loadHistory() {

        final int myRequest = ++requestCounter;
        final String search = searchField.getText();

        message.setText("Loading history...");
        searchButton.setDisable(true);

        Task<JsonNode> task = new Task<>() {
            @Override
            protected JsonNode call() {
                return predictions.getHistory(search, HISTORY_LIMIT);
            }
        };

        task.setOnSucceeded(e -> {
            if (myRequest != requestCounter) return; // stale response

            searchButton.setDisable(false);
            JsonNode items = task.getValue();

            predictionList.getChildren().clear();

            if (items == null || !items.isArray() || items.isEmpty()) {
                message.setText(
                        search == null || search.isBlank()
                                ? "No predictions yet. Run your first prediction to see it here."
                                : "No predictions match \"" + search.trim() + "\"."
                );
                return;
            }

            message.setText("");
            for (JsonNode item : items) {
                predictionList.getChildren().add(createPredictionRow(item));
            }
        });

        task.setOnFailed(e -> {
            if (myRequest != requestCounter) return;

            searchButton.setDisable(false);
            predictionList.getChildren().clear();

            Throwable error = task.getException();
            message.setText(
                    error != null && error.getMessage() != null
                            ? error.getMessage()
                            : "Unable to load prediction history."
            );
        });

        Thread thread = new Thread(task, "history-loader");
        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // Build one row from a prediction JSON object
    // =========================================================

    private HBox createPredictionRow(JsonNode item) {

        JsonNode repo = item.path("repository");
        JsonNode pr = item.path("pullRequest");

        String repository = repo.path("owner").asText("?")
                + "/" + repo.path("name").asText("?");

        String prText = "#" + pr.path("number").asText("?");
        String title = pr.path("title").asText("");
        if (!title.isBlank()) {
            prText += "  " + title;
        }

        // mergeProbability is null for PR_QUALITY requests -> fall back to qualityScore
        Double score = null;
        if (item.hasNonNull("mergeProbability")) {
            score = item.get("mergeProbability").asDouble();
        } else if (item.hasNonNull("qualityScore")) {
            score = item.get("qualityScore").asDouble();
        }

        String quality = item.path("qualityLabel").asText("-");
        String date = formatDate(item.path("createdAt").asText(null));

        return createPredictionRow(repository, prText, score, quality, date);
    }

    private HBox createPredictionRow(
            String repository,
            String pullRequest,
            Double score,
            String quality,
            String date
    ) {
        Label repositoryLabel = new Label(repository);
        repositoryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        repositoryLabel.getStyleClass().add("heading");

        Label pullRequestLabel = new Label(pullRequest);
        pullRequestLabel.setFont(Font.font("Arial", 11));
        pullRequestLabel.getStyleClass().add("muted-text");
        pullRequestLabel.setMaxWidth(420);

        VBox details = new VBox(3, repositoryLabel, pullRequestLabel);

        Region spacer = new Region();

        Label probabilityLabel = new Label(
                score == null ? "-" : String.format(Locale.US, "%.0f%%", score)
        );
        probabilityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        if (score == null) {
            probabilityLabel.getStyleClass().add("muted-text");
        } else if (score >= 70) {
            probabilityLabel.getStyleClass().add("probability-high");
        } else if (score >= 50) {
            probabilityLabel.getStyleClass().add("probability-medium");
        } else {
            probabilityLabel.getStyleClass().add("probability-low");
        }

        Label qualityLabel = new Label(quality);
        qualityLabel.getStyleClass().add("muted-text");

        Label dateLabel = new Label(date);
        dateLabel.getStyleClass().add("muted-text");

        HBox row = new HBox(
                15, details, spacer, probabilityLabel, qualityLabel, dateLabel
        );

        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12));
        row.setMaxWidth(Double.MAX_VALUE);
        row.getStyleClass().add("prediction-item");

        return row;
    }

    // =========================================================
    // API timestamps are UTC ISO strings without a "Z" suffix
    // =========================================================

    private String formatDate(String iso) {

        if (iso == null || iso.isBlank()) {
            return "-";
        }

        try {
            ZonedDateTime local = LocalDateTime.parse(iso)
                    .atOffset(ZoneOffset.UTC)
                    .atZoneSameInstant(ZoneId.systemDefault());

            LocalDate today = LocalDate.now();
            LocalDate day = local.toLocalDate();
            String time = local.format(DateTimeFormatter.ofPattern("HH:mm"));

            if (day.equals(today)) {
                return "Today, " + time;
            }
            if (day.equals(today.minusDays(1))) {
                return "Yesterday, " + time;
            }

            String pattern = day.getYear() == today.getYear()
                    ? "MMM d, HH:mm"
                    : "MMM d yyyy, HH:mm";

            return local.format(DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH));

        } catch (Exception e) {
            return iso; // show raw value rather than crash
        }
    }
}