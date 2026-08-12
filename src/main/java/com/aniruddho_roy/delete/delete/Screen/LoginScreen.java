package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.additional.CONSTANTS;
import com.aniruddho_roy.delete.delete.additional.LIB;
import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import com.aniruddho_roy.delete.delete.additional.THEAME;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginScreen extends VBox {

    private final NAVIGATOR navigator;

    public LoginScreen(NAVIGATOR navigator) {
        this.navigator = navigator;

        Label applicationName = new Label(CONSTANTS.APPLICATION_NAME);
        applicationName.setFont(
                Font.font("Arial", FontWeight.BOLD, 28)
        );
        applicationName.getStyleClass().add("app-title");

        ImageView imageView = new LIB().Loadimage(
                "/images/login.png",
                130,
                130,
                true
        );

        Label title = new Label("Login to Your Account");
        title.setFont(
                Font.font("Arial", FontWeight.BOLD, 20)
        );
        title.getStyleClass().add("heading");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(320);
        usernameField.setPrefHeight(42);
        usernameField.getStyleClass().add("login-input");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(320);
        passwordField.setPrefHeight(42);
        passwordField.getStyleClass().add("login-input");

        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", 14));

        Button loginButton = new Button("Login");
        loginButton.setPrefSize(320, 42);
        loginButton.setDefaultButton(true);
        loginButton.getStyleClass().add("primary-button");

        Button backButton = new Button("Back to Home");
        backButton.setPrefSize(320, 40);
        backButton.getStyleClass().add("outline-button");

        HBox themeBar = new HBox(THEAME.createToggleButton());
        themeBar.setAlignment(Pos.CENTER_RIGHT);
        themeBar.setMaxWidth(320);

        loginButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter username and password.");
                setMessageStyle(messageLabel, "message-error");
                return;
            }

            // Temporary login validation
            if (username.equals("admin") &&
                    password.equals("1234")) {

                messageLabel.setText("Login successful!");
                setMessageStyle(messageLabel, "message-success");

                /*
                 * Navigate to the next screen here.
                 * Change this according to your NAVIGATOR methods.
                 *
                 * Example:
                 * navigator.showDashboard();
                 */
                    navigator.loadUserDashboardScreen();
            } else {
                messageLabel.setText(
                        "Incorrect username or password."
                );
                setMessageStyle(messageLabel, "message-error");
            }
        });

        backButton.setOnAction(event -> {
            /*
             * Navigate back to the home screen here.
             *
             * Example:
             * navigator.showDashboard();
             */

            navigator.loadDashboardScreen();
        });

        this.getChildren().addAll(
                themeBar,
                applicationName,
                imageView,
                title,
                usernameField,
                passwordField,
                messageLabel,
                loginButton,
                backButton
        );

        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(35));
        this.getStyleClass().addAll("app-screen", "simple-screen");
    }

    private void setMessageStyle(Label label, String styleClass) {
        label.getStyleClass().removeAll(
                "message-error",
                "message-success"
        );
        label.getStyleClass().add(styleClass);
    }
}
