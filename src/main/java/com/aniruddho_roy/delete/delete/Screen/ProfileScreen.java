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

import com.aniruddho_roy.delete.delete.Backend.Profile;
import com.aniruddho_roy.delete.delete.Backend.User;

public class ProfileScreen extends DashboardBase {
    private final Profile profileApi =
            new Profile();

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

        TextField userNameField =
                new TextField("Error");

        TextField nameField =
                new TextField("ERROR");

        TextField emailField =
                new TextField("ERROR");

        TextField roleField =
                new TextField("ERROR");

        TextField githubField =
                new TextField("ERROR");

        userNameField.getStyleClass().add("login-input");
        nameField.getStyleClass().add("login-input");
        emailField.getStyleClass().add("login-input");
        roleField.getStyleClass().add("login-input");
        githubField.getStyleClass().add("login-input");

        roleField.setEditable(false);
        userNameField.setEditable(false);
        User user =
                profileApi.getProfile();

        if (user != null) {
            userNameField.setText(
                    user.getUsername()
            );

            nameField.setText(
                    user.getFullName()
            );

            emailField.setText(
                    user.getEmail()
            );

            roleField.setText(
                    user.getRole()
            );

            if (user.getGithubProfileUrl() != null) {

                githubField.setText(
                        user.getGithubProfileUrl()
                );
            }
        }


        GridPane fields = new GridPane();
        fields.setHgap(15);
        fields.setVgap(15);

        fields.add(createField("Full name", nameField), 0, 0);
        fields.add(createField("Email", emailField), 1, 0);
        fields.add(createField("Role (read-only)", roleField), 0, 1);
        fields.add(createField("GitHub profile", githubField), 1, 1);
        fields.add(createField("user Name (read-only):",userNameField),0,2);

        Label status = new Label();
        status.getStyleClass().add("muted-text");

        Button saveButton = new Button("Save Changes");
        saveButton.setPrefHeight(40);
        saveButton.getStyleClass().add("primary-button");

        saveButton.setOnAction(event -> {

            String fullName =
                    nameField.getText().trim();

            String email =
                    emailField.getText().trim();

            String github =
                    githubField.getText().trim();


            if (fullName.isEmpty()) {

                status.setText(
                        "Full name cannot be empty."
                );

                return;
            }


            if (email.isEmpty()) {

                status.setText(
                        "Email cannot be empty."
                );

                return;
            }


            int result =
                    profileApi.updateProfile(
                            fullName,
                            email,
                            github
                    );


            if (result == 200) {

                status.setText(
                        "Profile updated successfully."
                );

            } else if (result == 409) {

                status.setText(
                        "This email is already being used."
                );

            } else if (result == 401) {

                status.setText(
                        "Your login session has expired."
                );

            } else {

                status.setText(
                        "Could not update profile."
                );
            }
        });

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