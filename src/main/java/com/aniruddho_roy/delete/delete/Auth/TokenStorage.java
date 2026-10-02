package com.aniruddho_roy.delete.delete.Auth;

import java.util.prefs.Preferences;

public class TokenStorage {

    private static final Preferences prefs =
            Preferences.userNodeForPackage(TokenStorage.class);

    private static final String TOKEN_KEY = "access_token";

    private TokenStorage() {
    }

    public static void saveToken(String token) {
        prefs.put(TOKEN_KEY, token);
    }

    public static String getToken() {
        return prefs.get(TOKEN_KEY, null);
    }

    public static void removeToken() {
        prefs.remove(TOKEN_KEY);
    }

    public static boolean hasToken() {
        String token = getToken();

        return token != null && !token.isBlank();
    }
}