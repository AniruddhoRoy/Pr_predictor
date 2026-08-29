package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import com.aniruddho_roy.delete.delete.additional.THEAME;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SettingsScreen extends DashboardBase {

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

        Button themeButton = THEAME.createToggleButton();

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
                        "GitHub PR URL",
                        "Manual Features"
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
                        "Choose URL or manual feature input.",
                        defaultInput
                )
        );

        settingsList.getStyleClass().add("surface-card");

        Label status = new Label();
        status.getStyleClass().add("muted-text");

        Button saveButton = new Button("Save Settings");
        saveButton.setPrefHeight(40);
        saveButton.getStyleClass().add("primary-button");

        saveButton.setOnAction(event ->
                status.setText(
                        "Settings are ready to be saved through the backend."
                )
        );

        VBox content = new VBox(
                18,
                heading,
                description,
                settingsList,
                saveButton,
                status
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