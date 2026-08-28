package com.aniruddho_roy.delete.delete.additional;

import com.aniruddho_roy.delete.delete.Screen.Dashboard;
import com.aniruddho_roy.delete.delete.Screen.LoginScreen;
import com.aniruddho_roy.delete.delete.Screen.NewPredictionScreen;
import com.aniruddho_roy.delete.delete.Screen.UserDashboard;
import javafx.scene.layout.BorderPane;

public class NAVIGATOR {
    BorderPane root;
    public NAVIGATOR(BorderPane root){
        this.root = root;
    }
    public void loadDashboardScreen(){
        root.setCenter(new Dashboard(this));
    }
    public void loadLoginScreen(){
        root.setCenter(new LoginScreen(this));
    }
    public void loadUserDashboardScreen(){
        root.setCenter(new UserDashboard(this));
    }
    public void loadNewPredictionScreen() {
        root.setCenter(new NewPredictionScreen(this));
    }

}
