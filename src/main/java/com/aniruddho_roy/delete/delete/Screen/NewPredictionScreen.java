package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.Backend.Predictions;
import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;


public class NewPredictionScreen
        extends DashboardBase {


    private final Predictions predictions =
            new Predictions();


    // =========================================================
    // Constructor
    // =========================================================

    public NewPredictionScreen(
            NAVIGATOR navigator
    ) {

        this(
                navigator,
                false
        );
    }


    public NewPredictionScreen(
            NAVIGATOR navigator,
            boolean subscribed
    ) {

        super(
                navigator,
                subscribed
        );


        setCenter(
                createPredictionCenter()
        );
    }


    // =========================================================
    // Main UI
    // =========================================================

    private ScrollPane createPredictionCenter() {


        // =====================================================
        // Heading
        // =====================================================

        Label heading =
                new Label(
                        "New Prediction"
                );


        heading.setFont(
                javafx.scene.text.Font.font(
                        "Arial",
                        javafx.scene.text.FontWeight.BOLD,
                        23
                )
        );


        heading
                .getStyleClass()
                .add(
                        "heading"
                );


        // =====================================================
        // Description
        // =====================================================

        Label description =
                new Label(
                        "Choose what you want to predict, select a model, and provide a GitHub pull-request URL."
                );


        description
                .getStyleClass()
                .add(
                        "muted-text"
                );


        description.setWrapText(
                true
        );


        // =====================================================
        // Prediction Type
        // =====================================================

        ComboBox<String> predictionType =
                new ComboBox<>(
                        FXCollections.observableArrayList(
//                                "Merge Probability",
//                                "PR Quality",
                                "Both"
                        )
                );


        predictionType
                .getSelectionModel()
                .selectFirst();


        predictionType.setMaxWidth(
                Double.MAX_VALUE
        );


        VBox predictionTypeBox =
                new VBox(
                        8,
                        new Label(
                                "Prediction type"
                        ),
                        predictionType
                );


        // =====================================================
        // Model Selector
        // =====================================================

        ComboBox<ModelItem> modelSelector =
                new ComboBox<>();


        modelSelector.setMaxWidth(
                Double.MAX_VALUE
        );


        modelSelector.setPromptText(
                "Loading models..."
        );


        VBox modelBox =
                new VBox(
                        8,
                        new Label(
                                "Model"
                        ),
                        modelSelector
                );


        // =====================================================
        // GitHub PR URL
        // =====================================================

        TextField githubUrlField =
                new TextField();


        githubUrlField.setPromptText(
                "https://github.com/owner/repository/pull/123"
        );


        githubUrlField
                .getStyleClass()
                .add(
                        "login-input"
                );


        VBox githubUrlBox =
                new VBox(
                        8,
                        new Label(
                                "Pull-request URL"
                        ),
                        githubUrlField
                );


        // =====================================================
        // Status
        // =====================================================

        Label statusLabel =
                new Label();


        statusLabel
                .getStyleClass()
                .add(
                        "muted-text"
                );


        statusLabel.setWrapText(
                true
        );


        // =====================================================
        // Analyze Button
        // =====================================================

        Button analyzeButton =
                new Button(
                        "Analyze Pull Request"
                );


        analyzeButton
                .getStyleClass()
                .add(
                        "primary-button"
                );


        analyzeButton.setMaxWidth(
                Double.MAX_VALUE
        );


        analyzeButton.setDisable(
                true
        );


        // =====================================================
        // Initial GET /models
        // =====================================================

        loadModels(
                toApiPredictionType(
                        predictionType.getValue()
                ),
                modelSelector,
                analyzeButton,
                statusLabel
        );


        // =====================================================
        // Prediction Type Changed
        // =====================================================

        predictionType
                .valueProperty()
                .addListener(
                        (
                                observable,
                                oldValue,
                                newValue
                        ) -> {

                            String apiType =
                                    toApiPredictionType(
                                            newValue
                                    );


                            loadModels(
                                    apiType,
                                    modelSelector,
                                    analyzeButton,
                                    statusLabel
                            );
                        }
                );


        // =====================================================
        // Analyze Click
        // =====================================================

        analyzeButton.setOnAction(
                event -> {


                    clearError(
                            statusLabel
                    );


                    // =========================================
                    // Get URL
                    // =========================================

                    String url =
                            githubUrlField
                                    .getText()
                                    .trim();


                    // =========================================
                    // Validate URL
                    // =========================================

                    boolean validUrl =
                            url.matches(
                                    "https?://github\\.com/[^/]+/[^/]+/pull/\\d+/?"
                            );


                    if (!validUrl) {

                        showError(
                                statusLabel,
                                "Please enter a valid GitHub pull-request URL."
                        );

                        return;
                    }


                    // =========================================
                    // Selected Model
                    // =========================================

                    ModelItem selectedModel =
                            modelSelector
                                    .getValue();


                    if (selectedModel == null) {

                        showError(
                                statusLabel,
                                "Please select a prediction model."
                        );

                        return;
                    }


                    // =========================================
                    // Prediction Type
                    // =========================================

                    String apiPredictionType =
                            toApiPredictionType(
                                    predictionType.getValue()
                            );


                    // =========================================
                    // Disable Form
                    // =========================================

                    setFormDisabled(
                            true,
                            analyzeButton,
                            modelSelector,
                            predictionType,
                            githubUrlField
                    );


                    statusLabel.setText(
                            "Analyzing pull request..."
                    );


                    // =========================================
                    // POST /predict
                    // =========================================

                    Task<JsonNode> predictionTask =
                            new Task<>() {

                                @Override
                                protected JsonNode call() {

                                    return predictions.predict(
                                            url,
                                            apiPredictionType,
                                            selectedModel.getModelId()
                                    );
                                }
                            };


                    // =========================================
                    // SUCCESS
                    // =========================================

                    predictionTask.setOnSucceeded(
                            workerStateEvent -> {


                                setFormDisabled(
                                        false,
                                        analyzeButton,
                                        modelSelector,
                                        predictionType,
                                        githubUrlField
                                );


                                clearError(
                                        statusLabel
                                );


                                JsonNode result =
                                        predictionTask
                                                .getValue();


                                // Safety check

                                if (
                                        result == null ||
                                                result.isNull()
                                ) {

                                    showError(
                                            statusLabel,
                                            "The prediction server returned an empty result."
                                    );

                                    return;
                                }


                                statusLabel.setText(
                                        "Prediction completed successfully."
                                );


                                // =================================
                                // Pass COMPLETE API result
                                // directly to result screen
                                // =================================

                                navigator
                                        .loadPredictionResultScreen(
                                                result
                                        );
                            }
                    );


                    // =========================================
                    // FAILED
                    // =========================================

                    predictionTask.setOnFailed(
                            workerStateEvent -> {


                                setFormDisabled(
                                        false,
                                        analyzeButton,
                                        modelSelector,
                                        predictionType,
                                        githubUrlField
                                );


                                Throwable exception =
                                        predictionTask
                                                .getException();


                                String error;


                                if (
                                        exception == null ||
                                                exception.getMessage() == null ||
                                                exception.getMessage().isBlank()
                                ) {

                                    error =
                                            "Prediction failed.";

                                } else {

                                    error =
                                            exception.getMessage();
                                }


                                showError(
                                        statusLabel,
                                        error
                                );
                            }
                    );


                    // =========================================
                    // Run Background Task
                    // =========================================

                    Thread thread =
                            new Thread(
                                    predictionTask
                            );


                    thread.setDaemon(
                            true
                    );


                    thread.start();
                }
        );


        // =====================================================
        // Main Content
        // =====================================================

        VBox centerContent =
                new VBox(
                        18,
                        heading,
                        description,
                        predictionTypeBox,
                        modelBox,
                        githubUrlBox,
                        analyzeButton,
                        statusLabel
                );


        centerContent.setPadding(
                new Insets(25)
        );


        centerContent.setMaxWidth(
                Double.MAX_VALUE
        );


        centerContent
                .getStyleClass()
                .addAll(
                        "surface-card",
                        "dashboard-content"
                );


        // =====================================================
        // ScrollPane
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane(
                        centerContent
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
    // GET /models
    // =========================================================

    private void loadModels(
            String predictionType,
            ComboBox<ModelItem> modelSelector,
            Button analyzeButton,
            Label statusLabel
    ) {


        modelSelector.setDisable(
                true
        );


        analyzeButton.setDisable(
                true
        );


        modelSelector
                .getItems()
                .clear();


        modelSelector.setPromptText(
                "Loading models..."
        );


        clearError(
                statusLabel
        );


        statusLabel.setText(
                "Loading available models..."
        );


        // =====================================================
        // GET /models in background
        // =====================================================

        Task<List<ModelItem>> task =
                new Task<>() {

                    @Override
                    protected List<ModelItem> call() {


                        JsonNode response =
                                predictions.getModels(
                                        predictionType
                                );


                        List<ModelItem> models =
                                new ArrayList<>();


                        if (
                                response != null &&
                                        response.isArray()
                        ) {


                            for (
                                    JsonNode model :
                                    response
                            ) {


                                String modelId =
                                        model
                                                .path("modelId")
                                                .asText();


                                String code =
                                        model
                                                .path("code")
                                                .asText();


                                String name =
                                        model
                                                .path("name")
                                                .asText();


                                int creditCost =
                                        model
                                                .path("creditCost")
                                                .asInt();


                                // Ignore malformed model objects

                                if (
                                        modelId == null ||
                                                modelId.isBlank() ||
                                                name == null ||
                                                name.isBlank()
                                ) {

                                    continue;
                                }


                                models.add(
                                        new ModelItem(
                                                modelId,
                                                code,
                                                name,
                                                creditCost
                                        )
                                );
                            }
                        }


                        return models;
                    }
                };


        // =====================================================
        // Model Loading Success
        // =====================================================

        task.setOnSucceeded(
                event -> {


                    List<ModelItem> models =
                            task.getValue();


                    modelSelector.setDisable(
                            false
                    );


                    modelSelector
                            .getItems()
                            .setAll(
                                    models
                            );


                    if (
                            models == null ||
                                    models.isEmpty()
                    ) {


                        modelSelector.setPromptText(
                                "No models available"
                        );


                        analyzeButton.setDisable(
                                true
                        );


                        showError(
                                statusLabel,
                                "Your current plan has no available model for this prediction type."
                        );


                        return;
                    }


                    modelSelector
                            .getSelectionModel()
                            .selectFirst();


                    modelSelector.setPromptText(
                            "Select model"
                    );


                    analyzeButton.setDisable(
                            false
                    );


                    clearError(
                            statusLabel
                    );


                    statusLabel.setText(
                            ""
                    );
                }
        );


        // =====================================================
        // Model Loading Failed
        // =====================================================

        task.setOnFailed(
                event -> {


                    modelSelector.setDisable(
                            false
                    );


                    analyzeButton.setDisable(
                            true
                    );


                    modelSelector.setPromptText(
                            "Unable to load models"
                    );


                    Throwable exception =
                            task.getException();


                    String error;


                    if (
                            exception == null ||
                                    exception.getMessage() == null ||
                                    exception.getMessage().isBlank()
                    ) {

                        error =
                                "Unable to load prediction models.";

                    } else {

                        error =
                                exception.getMessage();
                    }


                    showError(
                            statusLabel,
                            error
                    );
                }
        );


        // =====================================================
        // Start Background Task
        // =====================================================

        Thread thread =
                new Thread(
                        task
                );


        thread.setDaemon(
                true
        );


        thread.start();
    }


    // =========================================================
    // UI prediction type -> API prediction type
    // =========================================================

    private String toApiPredictionType(
            String value
    ) {


        if (
                value == null
        ) {

            return "BOTH";
        }


        return switch (value) {

            case "Merge Probability" ->
                    "MERGE_PROBABILITY";

            case "PR Quality" ->
                    "PR_QUALITY";

            case "Both" ->
                    "BOTH";

            default ->
                    "BOTH";
        };
    }


    // =========================================================
    // Error Handling
    // =========================================================

    private void showError(
            Label statusLabel,
            String message
    ) {


        clearError(
                statusLabel
        );


        statusLabel.setText(
                message
        );


        statusLabel
                .getStyleClass()
                .add(
                        "message-error"
                );
    }


    private void clearError(
            Label statusLabel
    ) {


        statusLabel
                .getStyleClass()
                .remove(
                        "message-error"
                );
    }


    // =========================================================
    // Disable / Enable Form
    // =========================================================

    private void setFormDisabled(
            boolean disabled,
            Button analyzeButton,
            ComboBox<ModelItem> modelSelector,
            ComboBox<String> predictionType,
            TextField githubUrlField
    ) {


        analyzeButton.setDisable(
                disabled
        );


        modelSelector.setDisable(
                disabled
        );


        predictionType.setDisable(
                disabled
        );


        githubUrlField.setDisable(
                disabled
        );
    }


    // =========================================================
    // UI-only Model Item
    // =========================================================

    private static class ModelItem {


        private final String modelId;

        private final String code;

        private final String name;

        private final int creditCost;


        public ModelItem(
                String modelId,
                String code,
                String name,
                int creditCost
        ) {

            this.modelId =
                    modelId;

            this.code =
                    code;

            this.name =
                    name;

            this.creditCost =
                    creditCost;
        }


        public String getModelId() {

            return modelId;
        }


        public String getCode() {

            return code;
        }


        public String getName() {

            return name;
        }


        public int getCreditCost() {

            return creditCost;
        }


        @Override
        public String toString() {


            String creditText;


            if (
                    creditCost == 1
            ) {

                creditText =
                        "1 credit";

            } else {

                creditText =
                        creditCost +
                                " credits";
            }


            return name +
                    " • " +
                    creditText;
        }
    }
}