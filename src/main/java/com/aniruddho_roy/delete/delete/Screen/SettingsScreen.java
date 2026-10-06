package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import com.aniruddho_roy.delete.delete.additional.THEAME;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.aniruddho_roy.delete.delete.Backend.Settings;
import com.aniruddho_roy.delete.delete.Backend.DTO.UserSettings;

public class SettingsScreen extends DashboardBase {
    private final Settings settingsApi =
            new Settings();

    public SettingsScreen(NAVIGATOR navigator) {
        this(navigator, false);
    }

    public SettingsScreen(
            NAVIGATOR navigator,
            boolean subscribed
    ) {
        super(navigator, subscribed);
        setCenter(createSettingsCenter());
    }

    private ScrollPane createSettingsCenter() {

        Label heading = new Label("Settings");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 23));
        heading.getStyleClass().add("heading");

        Label description = new Label(
                "Configure appearance and prediction preferences."
        );
        description.setFont(Font.font("Arial", 14));
        description.getStyleClass().add("muted-text");
        /////////////////// Theame is not working ///////////////////////////
        Button themeButton = THEAME.createToggleButton();
        /////////////////// Theame is not working ///////////////////////////
        CheckBox notificationBox = new CheckBox(
                "Receive prediction notifications"
        );
        notificationBox.setSelected(true);

        ComboBox<String> predictionType = new ComboBox<>(
                FXCollections.observableArrayList(
                        "Merge Probability",
                        "PR Quality",
                        "Both"
                )
        );
        predictionType.getSelectionModel().select("Both");

        ComboBox<String> defaultInput = new ComboBox<>(
                FXCollections.observableArrayList(
                        "GitHub PR URL"
                )
        );
        defaultInput.getSelectionModel().select("GitHub PR URL");


        VBox settingsList = new VBox(
                createSettingRow(
                        "Appearance",
                        "Switch between light and dark application themes.",
                        themeButton
                ),
                createSettingRow(
                        "Notifications",
                        "Show notifications after a prediction is complete.",
                        notificationBox
                ),
                createSettingRow(
                        "Default prediction type",
                        "Choose the prediction selected by default.",
                        predictionType
                ),
                createSettingRow(
                        "Default input mode",
                        "Currently only GitHub PR URL is supported. "
                                + "Manual feature input is coming soon.",
                        defaultInput
                )
        );

        settingsList.getStyleClass().add("surface-card");

        Label status = new Label();
        status.getStyleClass().add("muted-text");



        UserSettings settings =
                settingsApi.getSettings();

        if (settings != null) {

            // Theme
//            if ("DARK".equals(settings.getThemeMode())) {
//                themeMode.getSelectionModel()
//                        .select("Dark");
//            } else {
//                themeMode.getSelectionModel()
//                        .select("Light");
//            }


            // Notifications
            notificationBox.setSelected(
                    settings.isNotificationsEnabled()
            );


            // Prediction type
            switch (settings.getDefaultPredictionType()) {

                case "MERGE_PROBABILITY" ->
                        predictionType
                                .getSelectionModel()
                                .select("Merge Probability");

                case "PR_QUALITY" ->
                        predictionType
                                .getSelectionModel()
                                .select("PR Quality");

                default ->
                        predictionType
                                .getSelectionModel()
                                .select("Both");
            }


            // Input mode

        }


        Button saveButton = new Button("Save Settings");
        saveButton.setPrefHeight(40);
        saveButton.getStyleClass().add("primary-button");

        saveButton.setOnAction(event -> {

            String selectedTheme = "DARK";

//            if ("Dark".equals(
//                    themeMode.getValue()
//            )) {
//                selectedTheme = "DARK";
//            } else {
//                selectedTheme = "LIGHT";
//            }


            String selectedPrediction;

            switch (predictionType.getValue()) {

                case "Merge Probability" ->
                        selectedPrediction =
                                "MERGE_PROBABILITY";

                case "PR Quality" ->
                        selectedPrediction =
                                "PR_QUALITY";

                default ->
                        selectedPrediction =
                                "BOTH";
            }


            String selectedInput = "GITHUB_URL";


            int result =
                    settingsApi.updateSettings(
                            selectedTheme,
                            notificationBox.isSelected(),
                            selectedPrediction,
                            selectedInput
                    );


            if (result == 200) {

                status.setText(
                        "Settings saved successfully."
                );

            } else if (result == 400) {

                status.setText(
                        "Invalid settings value."
                );

            } else if (result == 401) {

                status.setText(
                        "Your session has expired. Please login again."
                );

            } else {

                status.setText(
                        "Could not save settings."
                );
            }
        });
        VBox content = new VBox(
                18,
                heading,
                description,
                settingsList,

                saveButton,
                status
//                createPasswordCard()   // new
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

    private HBox createSettingRow(
            String title,
            String description,
            javafx.scene.Node control
    ) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 13)
        );
        titleLabel.getStyleClass().add("heading");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.getStyleClass().add("muted-text");

        VBox textBox = new VBox(
                4,
                titleLabel,
                descriptionLabel
        );

        HBox.setHgrow(textBox, Priority.ALWAYS);

        HBox row = new HBox(
                20,
                textBox,
                control
        );

        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16));

        return row;
    }


}