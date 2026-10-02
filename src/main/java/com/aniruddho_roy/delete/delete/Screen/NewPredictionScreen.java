package com.aniruddho_roy.delete.delete.Screen;

//import com.aniruddho_roy.delete.delete.Backend.Predictions;
import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;



public class NewPredictionScreen extends DashboardBase {

    public NewPredictionScreen(NAVIGATOR navigator) {
        this(navigator, false);
    }

    public NewPredictionScreen(
            NAVIGATOR navigator,
            boolean subscribed
    ) {
        super(navigator, subscribed);

        // Only the center area changes
        setCenter(createPredictionCenter());

    }

    private ScrollPane createPredictionCenter() {

        Label heading = new Label("New Prediction");
        heading.setFont(
                javafx.scene.text.Font.font(
                        "Arial",
                        javafx.scene.text.FontWeight.BOLD,
                        23
                )
        );
        heading.getStyleClass().add("heading");

        Label description = new Label(
                "Choose what you want to predict and provide pull-request data."
        );
        description.getStyleClass().add("muted-text");
        description.setWrapText(true);

        ComboBox<String> predictionType = new ComboBox<>(
                FXCollections.observableArrayList(
                        "Merge Probability",
                        "PR Quality",
                        "Both"
                )
        );
        predictionType.getSelectionModel().selectFirst();
        predictionType.setMaxWidth(Double.MAX_VALUE);

        Label predictionTypeLabel =
                new Label("Prediction type");

        VBox predictionTypeBox = new VBox(
                8,
                predictionTypeLabel,
                predictionType
        );

        RadioButton githubUrlOption =
                new RadioButton("GitHub PR URL");

        RadioButton manualFeatureOption =
                new RadioButton("Manual Features");

        ToggleGroup inputGroup = new ToggleGroup();

        githubUrlOption.setToggleGroup(inputGroup);
        manualFeatureOption.setToggleGroup(inputGroup);
        githubUrlOption.setSelected(true);

        HBox inputOptions = new HBox(
                20,
                githubUrlOption,
                manualFeatureOption
        );
        inputOptions.setAlignment(Pos.CENTER_LEFT);

        TextField githubUrlField = new TextField();
        githubUrlField.setPromptText(
                "https://github.com/owner/repository/pull/123"
        );
        githubUrlField.getStyleClass().add("login-input");

        VBox githubUrlBox = new VBox(
                8,
                new Label("Pull-request URL"),
                githubUrlField
        );

        TextArea featureField = new TextArea();
        featureField.setPromptText(
                "Enter one feature per line:\n" +
                        "changedFiles=12\n" +
                        "additions=150\n" +
                        "deletions=25\n" +
                        "testsAdded=true"
        );
        featureField.setPrefRowCount(8);
        featureField.setWrapText(true);
        featureField.getStyleClass().add("login-input");

        VBox manualFeatureBox = new VBox(
                8,
                new Label("Pull-request features"),
                featureField
        );

        manualFeatureBox.setVisible(false);
        manualFeatureBox.setManaged(false);

        inputGroup.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) -> {

                    boolean urlSelected =
                            newValue == githubUrlOption;

                    githubUrlBox.setVisible(urlSelected);
                    githubUrlBox.setManaged(urlSelected);

                    manualFeatureBox.setVisible(!urlSelected);
                    manualFeatureBox.setManaged(!urlSelected);
                }
        );

        Label statusLabel = new Label();
        statusLabel.getStyleClass().add("muted-text");
        statusLabel.setWrapText(true);

        Button analyzeButton =
                new Button("Analyze Pull Request");

        analyzeButton.getStyleClass().add("primary-button");
        analyzeButton.setMaxWidth(Double.MAX_VALUE);

        analyzeButton.setOnAction(event -> {


            String url = githubUrlField
                    .getText()
                    .trim();

            if (githubUrlOption.isSelected()) {



                boolean validUrl = url.matches(
                        "https?://github\\.com/[^/]+/[^/]+/pull/\\d+/?"
                );

                if (!validUrl) {
                    statusLabel.setText(
                            "Please enter a valid GitHub PR URL."
                    );
                    statusLabel.getStyleClass().add("message-error");
                    return;
                }

                statusLabel.setText(
                        "GitHub URL is valid. Ready for backend analysis."
                );

            } else {

                String features = featureField
                        .getText()
                        .trim();

                if (features.isEmpty()) {
                    statusLabel.setText(
                            "Please enter pull-request features."
                    );
                    statusLabel.getStyleClass().add("message-error");
                    return;
                }

                statusLabel.setText(
                        "Features are valid. Ready for backend analysis."
                );
            }
//            String probability = new Predictions().Predict(url);
                String probability = "89.4";
            navigator.loadPredictionResultScreen("test","test",0.5,probability);
        });

        VBox centerContent = new VBox(
                18,
                heading,
                description,
                predictionTypeBox,
                inputOptions,
                githubUrlBox,
                manualFeatureBox,
                analyzeButton,
                statusLabel
        );

        centerContent.setPadding(new Insets(25));
        centerContent.setMaxWidth(Double.MAX_VALUE);
        centerContent.getStyleClass().addAll(
                "surface-card",
                "dashboard-content"
        );

        ScrollPane scrollPane =
                new ScrollPane(centerContent);

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
}