package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.additional.CONSTANTS;
import com.aniruddho_roy.delete.delete.additional.LIB;
import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
        applicationName.setTextFill(Color.DARKBLUE);

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
        title.setTextFill(Color.DARKSLATEGRAY);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(320);
        usernameField.setPrefHeight(42);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(320);
        passwordField.setPrefHeight(42);

        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", 14));

        Button loginButton = new Button("Login");
        loginButton.setPrefSize(320, 42);
        loginButton.setDefaultButton(true);
        loginButton.setStyle(
                "-fx-background-color: #1565C0;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-cursor: hand;"
        );

        Button backButton = new Button("Back to Home");
        backButton.setPrefSize(320, 40);
        backButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #1565C0;" +
                        "-fx-font-size: 14px;" +
                        "-fx-border-color: #1565C0;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-cursor: hand;"
        );

        loginButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter username and password.");
                messageLabel.setTextFill(Color.RED);
                return;
            }

            // Temporary login validation
            if (username.equals("admin") &&
                    password.equals("1234")) {

                messageLabel.setText("Login successful!");
                messageLabel.setTextFill(Color.GREEN);

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
                messageLabel.setTextFill(Color.RED);
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
        this.setStyle("-fx-background-color: #F4F7FB;");
    }
}