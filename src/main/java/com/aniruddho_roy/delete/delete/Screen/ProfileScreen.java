package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.Backend.Subscription;
import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.aniruddho_roy.delete.delete.Backend.Profile;
import com.aniruddho_roy.delete.delete.Backend.User;

public class ProfileScreen extends DashboardBase {
    private final Profile profileApi =
            new Profile();


    private final Subscription subscriptionApi = new Subscription();
    private final VBox currentBox = new VBox();

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
                currentBox,
                fields,
                saveButton,
                status,
                createPasswordCard()   // new
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

        loadSubscription();

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
    private void loadSubscription() {

        Label loading = new Label("Loading subscription...");
        loading.getStyleClass().add("muted-text");
        currentBox.getChildren().setAll(loading);

        Thread thread = new Thread(() -> {

            Subscription.CurrentSubscription current =
                    subscriptionApi.getCurrentSubscription();

            Platform.runLater(() -> {

                if (current == null) {
                    Label error = new Label(
                            "Could not load your current subscription."
                    );
                    error.getStyleClass().add("muted-text");
                    currentBox.getChildren().setAll(error);
                    return;
                }

                currentBox.getChildren().setAll(
                        createCurrentSubscriptionCard(current)
                );
            });
        }, "profile-subscription-loader");

        thread.setDaemon(true);
        thread.start();
    }

    private VBox createCurrentSubscriptionCard(Subscription.CurrentSubscription current) {

        // Left side: plan info
        Label planLabel = new Label("Current subscription: " + current.planName());
        planLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        planLabel.getStyleClass().add("stat-value");

        Label infoLabel = new Label(
                current.status() + " • Started " + current.startedAt()
        );
        infoLabel.setFont(Font.font("Arial", 11));
        infoLabel.getStyleClass().add("muted-text");

        VBox leftBox = new VBox(2, planLabel, infoLabel);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        // Right side: credits
        Label creditsLabel = new Label(
                current.creditsUsed() + " / " + current.monthlyCredits()
                        + " credits used"
        );
        creditsLabel.setFont(Font.font("Arial", 11));
        creditsLabel.getStyleClass().add("muted-text");

        Label remainingLabel = new Label(current.creditsRemaining() + " left");
        remainingLabel.setFont(Font.font("Arial", 11));
        remainingLabel.getStyleClass().add("muted-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox creditsRow = new HBox(6, creditsLabel, spacer, remainingLabel);

        ProgressBar creditsBar = new ProgressBar(
                current.monthlyCredits() == 0
                        ? 0
                        : (double) current.creditsUsed() / current.monthlyCredits()
        );
        creditsBar.setMaxWidth(Double.MAX_VALUE);
        creditsBar.setPrefHeight(8);

        VBox rightBox = new VBox(4, creditsRow, creditsBar);
        rightBox.setAlignment(Pos.CENTER_LEFT);
        rightBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(rightBox, Priority.ALWAYS);

        HBox row = new HBox(30, leftBox, rightBox);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(row);
        card.setPadding(new Insets(10, 16, 10, 16));
        card.setMaxWidth(Double.MAX_VALUE);
        card.getStyleClass().add("surface-card");

        return card;
    }
    private VBox createPasswordCard() {

        Label title = new Label("Change password");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        title.getStyleClass().add("heading");

        PasswordField currentField = new PasswordField();
        currentField.setPromptText("Current password");
        currentField.getStyleClass().add("login-input");

        PasswordField newField = new PasswordField();
        newField.setPromptText("New password (4+ chars)");
        newField.getStyleClass().add("login-input");

        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Confirm new password");
        confirmField.getStyleClass().add("login-input");

        HBox.setHgrow(currentField, Priority.ALWAYS);
        HBox.setHgrow(newField, Priority.ALWAYS);
        HBox.setHgrow(confirmField, Priority.ALWAYS);

        HBox fieldsRow = new HBox(8, currentField, newField, confirmField);
        fieldsRow.setAlignment(Pos.CENTER_LEFT);

        Button changeButton = new Button("Change Password");
        changeButton.setPrefHeight(40);
        changeButton.getStyleClass().add("primary-button");

        Label passwordStatus = new Label();
        passwordStatus.setWrapText(true);
        passwordStatus.getStyleClass().add("muted-text");

        HBox buttonRow = new HBox(12, changeButton, passwordStatus);
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        changeButton.setOnAction(event -> {

            String current = currentField.getText();
            String newPassword = newField.getText();
            String confirm = confirmField.getText();

            if (current.isEmpty()) {
                passwordStatus.setText("Please enter your current password.");
                return;
            }

            if (newPassword.length() < 4 || newPassword.length() > 128) {
                passwordStatus.setText(
                        "New password must be 4 to 128 characters."
                );
                return;
            }

            if (!newPassword.equals(confirm)) {
                passwordStatus.setText("New passwords do not match.");
                return;
            }

            changeButton.setDisable(true);
            passwordStatus.setText("Changing password...");

            Thread thread = new Thread(() -> {

                int result = profileApi.changePassword(current, newPassword);
//                int result = 1;
                Platform.runLater(() -> {

                    changeButton.setDisable(false);

                    switch (result) {
                        case 200 -> {
                            passwordStatus.setText(
                                    "Password changed successfully."
                            );
                            currentField.clear();
                            newField.clear();
                            confirmField.clear();
                        }
                        case 400 -> passwordStatus.setText(
                                "Current password is incorrect."
                        );
                        case 401 -> passwordStatus.setText(
                                "Your session has expired. Please login again."
                        );
                        case 422 -> passwordStatus.setText(
                                "Invalid password length."
                        );
                        default -> passwordStatus.setText(
                                "Could not change password."
                        );
                    }
                });
            }, "change-password");

            thread.setDaemon(true);
            thread.start();
        });

        VBox card = new VBox(8, title, fieldsRow, buttonRow);
        card.setPadding(new Insets(10, 16, 10, 16));
        card.getStyleClass().add("surface-card");

        return card;
    }
}