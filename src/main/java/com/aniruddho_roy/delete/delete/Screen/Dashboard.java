package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.additional.CONSTANTS;
import com.aniruddho_roy.delete.delete.additional.LIB;
import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Dashboard extends VBox {
    private final NAVIGATOR navigator;
    public Dashboard(NAVIGATOR navigator){
        this.navigator = navigator;
        Label title = new Label(CONSTANTS.APPLICATION_NAME);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.DARKBLUE);

        // Load an image from:
        // src/main/resources/images/home.png


        ImageView imageView = new LIB().Loadimage("/images/home.png",300,200,true);


        Label information = new Label(
                "This application allows users to manage their account,\n" +
                        "view information, and access available services."
        );
        information.setFont(Font.font("Arial", 16));
        information.setTextFill(Color.DIMGRAY);
        information.setWrapText(true);
        information.setMaxWidth(450);
        information.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Login");
        loginButton.setPrefSize(140, 42);
        loginButton.setStyle(
                "-fx-background-color: #1565C0;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-cursor: hand;"
        );

        loginButton.setOnAction(event -> {
            System.out.println("Login button clicked");
            // Add your login-screen code here.
            navigator.loadLoginScreen();
        });
        this.getChildren().addAll(
                title,
                imageView,
                information,
                loginButton);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-color: #F4F7FB;");
    }

}
