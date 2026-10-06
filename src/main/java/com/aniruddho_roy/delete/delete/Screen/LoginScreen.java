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

public class LoginScreen extends VBox {

    private final NAVIGATOR navigator;

    public LoginScreen(NAVIGATOR navigator) {
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
        authenticationShell.setMinHeight(545);
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
                140,
                140,
                true
        );

        Label title = new Label("Welcome back");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 29));
        title.getStyleClass().add("auth-hero-title");

        Label description = new Label(
                "Continue reviewing your pull-request intelligence "
                        + "workspace from one focused dashboard."
        );
        description.setWrapText(true);
        description.setFont(Font.font("Arial", 15));
        description.getStyleClass().add("auth-hero-text");

        VBox benefits = new VBox(
                12,
                createBenefit("Analyze merge probability"),
                createBenefit("Review PR quality insights"),
                createBenefit("Keep your prediction history organized")
        );

        VBox visualPanel = new VBox(
                25,
                imageView,
                title,
                description,
                benefits
        );
        visualPanel.setAlignment(Pos.TOP_LEFT);
        visualPanel.setPadding(new Insets(42));
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

        Label title = new Label("Sign in to your account");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        title.getStyleClass().add("auth-form-title");

        Label description = new Label(
                "Enter your credentials to open the prediction workspace."
        );
        description.setWrapText(true);
        description.getStyleClass().add("auth-form-subtitle");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        styleInput(usernameField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleInput(passwordField);

        Label messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add("auth-status");

        Button loginButton = new Button("Login");
        loginButton.setPrefHeight(44);
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setDefaultButton(true);
        loginButton.getStyleClass().add("primary-button");

        loginButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                setMessageStyle(
                        messageLabel,
                        "Please enter your username and password.",
                        "message-error"
                );
                return;
            }

            // Temporary local validation until backend authentication is added.
            Auth auth = new Auth();
            if(auth.login(username,password)){
                navigator.loadUserDashboardScreen();
            } else {
                setMessageStyle(
                        messageLabel,
                        "Incorrect username or password.",
                        "message-error"
                );
            }
        });

//        Label demoLabel = new Label("Demo access: admin / 1234");
//        demoLabel.getStyleClass().add("auth-demo");

        Label registerPrompt = new Label("New to the workspace?");
        registerPrompt.getStyleClass().add("auth-form-subtitle");

        Hyperlink registerLink = new Hyperlink("Create an account");
        registerLink.getStyleClass().add("auth-link");
        registerLink.setOnAction(event -> navigator.loadRegisterScreen());

        HBox registerRow = new HBox(
                5,
                registerPrompt,
                registerLink
        );
        registerRow.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back to Home");
        backButton.setPrefHeight(40);
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.getStyleClass().add("outline-button");
        backButton.setOnAction(event -> navigator.loadDashboardScreen());

        VBox formPanel = new VBox(
                14,
                applicationName,
                title,
                description,
                createField("Username", usernameField),
                createField("Password", passwordField),
                loginButton,
                messageLabel,
//                demoLabel,
                registerRow,
                backButton
        );
        formPanel.setPadding(new Insets(38));
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
        field.setPrefHeight(42);
        field.setMaxWidth(Double.MAX_VALUE);
        field.getStyleClass().add("login-input");
    }

    private void setMessageStyle(
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
