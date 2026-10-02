package com.aniruddho_roy.delete.delete.additional;

import com.aniruddho_roy.delete.delete.Auth.TokenStorage;
import com.aniruddho_roy.delete.delete.Backend.Auth;
import com.aniruddho_roy.delete.delete.Backend.Profile;
import com.aniruddho_roy.delete.delete.Backend.User;
import com.aniruddho_roy.delete.delete.Screen.*;
import com.aniruddho_roy.delete.delete.Storage.APPLICATION_STORE;
import javafx.scene.layout.BorderPane;

public class NAVIGATOR {

    public enum Page {
        DASHBOARD,
        REGISTER,
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
        if(TokenStorage.hasToken()){
            loadUserDashboardScreen();
        }else{
            root.setCenter(new LoginScreen(this));
        }

    }

    public void loadRegisterScreen() {
        activePage = Page.REGISTER;
        root.setCenter(new RegisterScreen(this));
    }

    public void loadUserDashboardScreen() {
        activePage = Page.DASHBOARD;
        Profile profile = new Profile();
        if(profile.getProfile_test()){
            root.setCenter(new UserDashboard(this));
        }
        else {
            TokenStorage.removeToken();
            loadLoginScreen();
        }

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
        root.setCenter(new SubscriptionScreen(this));
    }

    public void loadProfileScreen() {
        activePage = Page.PROFILE;
        root.setCenter(new ProfileScreen(this));
    }

    public void loadSettingsScreen() {
        activePage = Page.SETTINGS;
        root.setCenter(new SettingsScreen(this));
    }
    public void loadPredictionResultScreen(
            String repository,
            String pullRequest,
            double mergeProbability,
            String quality
    ){
//        activePage = Page.NEW_PREDICTION
        root.setCenter((new PredictionResultScreen(this, APPLICATION_STORE.isUserSubscribed,repository,pullRequest,mergeProbability,quality)));
    }
}
