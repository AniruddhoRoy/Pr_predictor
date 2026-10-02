package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.Backend.Auth;
import com.aniruddho_roy.delete.delete.additional.CONSTANTS;
import com.aniruddho_roy.delete.delete.additional.LIB;
import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import com.aniruddho_roy.delete.delete.additional.THEAME;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RegisterScreen extends VBox {

    private final NAVIGATOR navigator;
    private final Auth auth = new Auth();

    public RegisterScreen(NAVIGATOR navigator) {
        this.navigator = navigator;

        HBox themeBar = new HBox(THEAME.createToggleButton());
        themeBar.setAlignment(Pos.CENTER_RIGHT);
        themeBar.setMaxWidth(980);

        VBox visualPanel = createVisualPanel();
        VBox formPanel = createFormPanel();

        HBox authenticationShell = new HBox(
                0,
                visualPanel,
                formPanel
        );
        authenticationShell.setPrefWidth(980);
        authenticationShell.setMaxWidth(980);
        authenticationShell.setMinHeight(575);
        authenticationShell.getStyleClass().addAll(
                "surface-card",
                "auth-shell"
        );

        HBox.setHgrow(visualPanel, Priority.ALWAYS);
        HBox.setHgrow(formPanel, Priority.ALWAYS);

        getChildren().addAll(
                themeBar,
                authenticationShell
        );

        setAlignment(Pos.CENTER);
        setSpacing(18);
        setPadding(new Insets(26));
        setFillWidth(true);
        getStyleClass().addAll(
                "app-screen",
                "simple-screen",
                "auth-page"
        );
    }

    private VBox createVisualPanel() {
        ImageView imageView = new LIB().Loadimage(
                "/images/login.png",
                120,
                120,
                true
        );

        Label title = new Label("Join the PR Intelligence workspace");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        title.setWrapText(true);
        title.getStyleClass().add("auth-hero-title");

        Label description = new Label(
                "Create an account to organize your pull-request predictions, "
                        + "track usage, and review your analysis history."
        );
        description.setWrapText(true);
        description.setFont(Font.font("Arial", 14));
        description.getStyleClass().add("auth-hero-text");

        VBox benefits = new VBox(
                12,
                createBenefit("Save prediction history in one place"),
                createBenefit("Monitor merge probability and PR quality"),
                createBenefit("Manage your plan and prediction preferences")
        );

        VBox visualPanel = new VBox(
                22,
                imageView,
                title,
                description,
                benefits
        );
        visualPanel.setAlignment(Pos.TOP_LEFT);
        visualPanel.setPadding(new Insets(38));
        visualPanel.setPrefWidth(470);
        visualPanel.setMinWidth(380);
        visualPanel.getStyleClass().add("auth-hero");

        return visualPanel;
    }

    private Label createBenefit(String text) {
        Label benefit = new Label("✓  " + text);
        benefit.setWrapText(true);
        benefit.getStyleClass().add("auth-hero-point");
        return benefit;
    }

    private VBox createFormPanel() {
        Label applicationName = new Label(
                CONSTANTS.APPLICATION_NAME
        );
        applicationName.setFont(
                Font.font("Arial", FontWeight.BOLD, 14)
        );
        applicationName.getStyleClass().add("app-title");

        Label title = new Label("Create your account");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.getStyleClass().add("auth-form-title");

        Label description = new Label(
                "Use your account to access the prediction workspace."
        );
        description.setWrapText(true);
        description.getStyleClass().add("auth-form-subtitle");

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Full name");
        styleInput(fullNameField);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        styleInput(usernameField);

        TextField emailField = new TextField();
        emailField.setPromptText("Email address");
        styleInput(emailField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("At least 6 characters");
        styleInput(passwordField);

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Repeat your password");
        styleInput(confirmPasswordField);

        Label statusLabel = new Label();
        statusLabel.setWrapText(true);
        statusLabel.getStyleClass().add("auth-status");

        Button registerButton = new Button("Create Account");
        registerButton.setPrefHeight(42);
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.getStyleClass().add("primary-button");

        registerButton.setOnAction(event -> {
            String fullName = fullNameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (fullName.isEmpty()
                    || username.isEmpty()
                    || email.isEmpty()
                    || password.isEmpty()
                    || confirmPassword.isEmpty()) {
                setMessage(
                        statusLabel,
                        "Please complete all fields.",
                        "message-error"
                );
                return;
            }

            if (!email.matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
            )) {
                setMessage(
                        statusLabel,
                        "Please enter a valid email address.",
                        "message-error"
                );
                return;
            }

            if (password.length() < 6) {
                setMessage(
                        statusLabel,
                        "Password must contain at least 6 characters.",
                        "message-error"
                );
                return;
            }

            if (!password.equals(confirmPassword)) {
                setMessage(
                        statusLabel,
                        "Passwords do not match.",
                        "message-error"
                );
                return;
            }

            int result = auth.register(
                    fullName,
                    username,
                    email,
                    password
            );

            if (result == 201) {

                setMessage(
                        statusLabel,
                        "Account created successfully.",
                        "message-success"
                );

                navigator.loadUserDashboardScreen();

            } else if (result == 409) {

                setMessage(
                        statusLabel,
                        "Username or email is already registered.",
                        "message-error"
                );

            } else {

                setMessage(
                        statusLabel,
                        "Could not create account. Please try again.",
                        "message-error"
                );
            }
        });

        Label accountPrompt = new Label("Already have an account?");
        accountPrompt.getStyleClass().add("auth-form-subtitle");

        Hyperlink loginLink = new Hyperlink("Sign in");
        loginLink.getStyleClass().add("auth-link");
        loginLink.setOnAction(event -> navigator.loadLoginScreen());

        HBox loginRow = new HBox(
                5,
                accountPrompt,
                loginLink
        );
        loginRow.setAlignment(Pos.CENTER);

        Button homeButton = new Button("Back to Home");
        homeButton.setPrefHeight(38);
        homeButton.setMaxWidth(Double.MAX_VALUE);
        homeButton.getStyleClass().add("outline-button");
        homeButton.setOnAction(event -> navigator.loadDashboardScreen());

        VBox formPanel = new VBox(
                9,
                applicationName,
                title,
                description,
                createField("Full name", fullNameField),
                createField("Username", usernameField),
                createField("Email", emailField),
                createField("Password", passwordField),
                createField("Confirm password", confirmPasswordField),
                registerButton,
                statusLabel,
                loginRow,
                homeButton
        );
        formPanel.setPadding(new Insets(30));
        formPanel.setPrefWidth(510);
        formPanel.setMinWidth(440);
        formPanel.getStyleClass().add("auth-form");

        return formPanel;
    }

    private VBox createField(
            String labelText,
            TextField field
    ) {
        Label label = new Label(labelText);
        label.getStyleClass().add("auth-field-label");

        VBox fieldBox = new VBox(
                6,
                label,
                field
        );
        fieldBox.setMaxWidth(Double.MAX_VALUE);
        return fieldBox;
    }

    private void styleInput(TextField field) {
        field.setPrefHeight(38);
        field.setMaxWidth(Double.MAX_VALUE);
        field.getStyleClass().add("login-input");
    }

    private void setMessage(
            Label label,
            String message,
            String styleClass
    ) {
        label.setText(message);
        label.getStyleClass().removeAll(
                "message-error",
                "message-success"
        );
        label.getStyleClass().add(styleClass);
    }
}
