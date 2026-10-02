package com.aniruddho_roy.delete.delete;

import com.aniruddho_roy.delete.delete.Auth.TokenStorage;
import com.aniruddho_roy.delete.delete.additional.CONSTANTS;
import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import com.aniruddho_roy.delete.delete.additional.THEAME;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root= new BorderPane();
        NAVIGATOR navigator = new NAVIGATOR(root);
        navigator.loadDashboardScreen();
        Scene scene = new Scene(root, CONSTANTS.APPLICATION_WIDTH,CONSTANTS.APPLICATION_HEIGHT );
        THEAME.apply(scene);

        stage.setTitle("Home");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
