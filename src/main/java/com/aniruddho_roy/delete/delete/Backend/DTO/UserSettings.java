package com.aniruddho_roy.delete.delete.Backend.DTO;

public class UserSettings {

    private String themeMode;
    private boolean notificationsEnabled;
    private String defaultPredictionType;
    private String defaultInputMode;

    public UserSettings() {
    }

    public String getThemeMode() {
        return themeMode;
    }

    public void setThemeMode(String themeMode) {
        this.themeMode = themeMode;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public String getDefaultPredictionType() {
        return defaultPredictionType;
    }

    public void setDefaultPredictionType(String defaultPredictionType) {
        this.defaultPredictionType = defaultPredictionType;
    }

    public String getDefaultInputMode() {
        return defaultInputMode;
    }

    public void setDefaultInputMode(String defaultInputMode) {
        this.defaultInputMode = defaultInputMode;
    }
}