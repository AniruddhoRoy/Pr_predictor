package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ProfileScreen extends DashboardBase {

    public ProfileScreen(NAVIGATOR navigator) {
        this(navigator, false);
    }

    public ProfileScreen(
            NAVIGATOR navigator,
            boolean subscribed
    ) {
        super(navigator, subscribed);
        setCenter(createProfileCenter());
    }

    private ScrollPane createProfileCenter() {

        Label heading = new Label("Profile");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 23));
        heading.getStyleClass().add("heading");

        Label description = new Label(
                "Manage your personal information and account details."
        );
        description.setFont(Font.font("Arial", 14));
        description.getStyleClass().add("muted-text");

        TextField nameField = new TextField("Aniruddho Roy");
        nameField.getStyleClass().add("login-input");

        TextField emailField = new TextField("aniruddho@example.com");
        emailField.getStyleClass().add("login-input");

        TextField roleField = new TextField("Developer");
        roleField.getStyleClass().add("login-input");

        TextField githubField = new TextField("github.com/aniruddhoroy");
        githubField.getStyleClass().add("login-input");

        GridPane fields = new GridPane();
        fields.setHgap(15);
        fields.setVgap(15);

        fields.add(createField("Full name", nameField), 0, 0);
        fields.add(createField("Email", emailField), 1, 0);
        fields.add(createField("Role", roleField), 0, 1);
        fields.add(createField("GitHub profile", githubField), 1, 1);

        Label status = new Label();
        status.getStyleClass().add("muted-text");

        Button saveButton = new Button("Save Changes");
        saveButton.setPrefHeight(40);
        saveButton.getStyleClass().add("primary-button");

        saveButton.setOnAction(event ->
                status.setText(
                        "Profile changes are ready to send to the backend."
                )
        );

        VBox content = new VBox(
                18,
                heading,
                description,
                fields,
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

    private VBox createField(
            String labelText,
            TextField field
    ) {
        Label label = new Label(labelText);
        label.getStyleClass().add("muted-text");

        VBox box = new VBox(
                7,
                label,
                field
        );

        box.setPrefWidth(280);
        return box;
    }
}