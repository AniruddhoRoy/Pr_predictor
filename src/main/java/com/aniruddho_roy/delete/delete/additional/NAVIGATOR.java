package com.aniruddho_roy.delete.delete.additional;

import com.aniruddho_roy.delete.delete.Screen.Dashboard;
import com.aniruddho_roy.delete.delete.Screen.HistoryScreen;
import com.aniruddho_roy.delete.delete.Screen.LoginScreen;
import com.aniruddho_roy.delete.delete.Screen.NewPredictionScreen;
//import com.aniruddho_roy.delete.delete.Screen.ProfileScreen;
//import com.aniruddho_roy.delete.delete.Screen.SettingsScreen;
//import com.aniruddho_roy.delete.delete.Screen.SubscriptionScreen;
import com.aniruddho_roy.delete.delete.Screen.UserDashboard;
import javafx.scene.layout.BorderPane;

public class NAVIGATOR {

    public enum Page {
        DASHBOARD,
        NEW_PREDICTION,
        HISTORY,
        SUBSCRIPTION,
        PROFILE,
        SETTINGS
    }

    public static Page activePage = Page.DASHBOARD;

    private final BorderPane root;

    public NAVIGATOR(BorderPane root) {
        this.root = root;
    }

    public void loadDashboardScreen() {
        activePage = Page.DASHBOARD;
        root.setCenter(new Dashboard(this));
    }

    public void loadLoginScreen() {
        root.setCenter(new LoginScreen(this));
    }

    public void loadUserDashboardScreen() {
        activePage = Page.DASHBOARD;
        root.setCenter(new UserDashboard(this));
    }

    public void loadNewPredictionScreen() {
        activePage = Page.NEW_PREDICTION;
        root.setCenter(new NewPredictionScreen(this));
    }

    public void loadHistoryScreen() {
        activePage = Page.HISTORY;
        root.setCenter(new HistoryScreen(this));
    }

    public void loadSubscriptionScreen() {
        activePage = Page.SUBSCRIPTION;
//        root.setCenter(new SubscriptionScreen(this));
    }

    public void loadProfileScreen() {
        activePage = Page.PROFILE;
//        root.setCenter(new ProfileScreen(this));
    }

    public void loadSettingsScreen() {
        activePage = Page.SETTINGS;
//        root.setCenter(new SettingsScreen(this));
    }
}