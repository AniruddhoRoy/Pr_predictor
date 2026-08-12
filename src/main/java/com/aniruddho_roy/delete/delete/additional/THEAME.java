package com.aniruddho_roy.delete.delete.additional;

import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.Objects;

public final class THEAME {

    public enum Mode {
        LIGHT,
        DARK
    }

    private static final String BASE_STYLESHEET = stylesheet("/css/theme-base.css");
    private static final String LIGHT_STYLESHEET = stylesheet("/css/light-theme.css");
    private static final String DARK_STYLESHEET = stylesheet("/css/dark-theme.css");

    private static Mode currentMode = Mode.LIGHT;

    private THEAME() {
    }

    public static void apply(Scene scene) {
        Objects.requireNonNull(scene, "scene must not be null");

        if (!scene.getStylesheets().contains(BASE_STYLESHEET)) {
            scene.getStylesheets().add(BASE_STYLESHEET);
        }

        scene.getStylesheets().removeAll(
                LIGHT_STYLESHEET,
                DARK_STYLESHEET
        );

        scene.getStylesheets().add(
                currentMode == Mode.LIGHT
                        ? LIGHT_STYLESHEET
                        : DARK_STYLESHEET
        );
    }

    public static void toggle(Scene scene) {
        currentMode = currentMode == Mode.LIGHT
                ? Mode.DARK
                : Mode.LIGHT;

        apply(scene);
    }

    public static Button createToggleButton() {
        Button toggleButton = new Button();
        toggleButton.getStyleClass().add("theme-toggle");
        updateToggleButton(toggleButton);

        toggleButton.setOnAction(event -> {
            Scene scene = toggleButton.getScene();

            if (scene != null) {
                toggle(scene);
                updateToggleButton(toggleButton);
            }
        });

        return toggleButton;
    }

    private static void updateToggleButton(Button button) {
        boolean lightTheme = currentMode == Mode.LIGHT;
        button.setText(lightTheme ? "Dark mode" : "Light mode");
        button.setAccessibleText(
                lightTheme
                        ? "Switch to dark mode"
                        : "Switch to light mode"
        );
    }

    private static String stylesheet(String path) {
        URL resource = THEAME.class.getResource(path);
        return Objects.requireNonNull(
                resource,
                "Missing stylesheet: " + path
        ).toExternalForm();
    }
}
