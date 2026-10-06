package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.additional.*;
import javafx.geometry.Insets;

import javafx.scene.control.ScrollPane;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;


public class DashboardBase extends BorderPane {

    protected final NAVIGATOR navigator;
    protected final boolean subscribed;

    public DashboardBase(NAVIGATOR navigator) {
        this(navigator, false);
    }
    public DashboardBase(NAVIGATOR navigator, boolean subscribed) {
        this.navigator = navigator;
        this.subscribed = subscribed;
        // there is no css called "Dashboard-screen"
        getStyleClass().addAll("app-screen", "dashboard-screen");
        setPadding(new Insets(20));

        HBox header = new COMPONETS().createHeader(navigator);
        VBox sidebar = new COMPONETS().createSidebar(navigator);
        ScrollPane centerContent = new ScrollPane();
        VBox recentPredictions = new COMPONETS().createPreviousPredictions(navigator);

        setTop(header);
        setLeft(sidebar);
        setCenter(centerContent);
        setRight(recentPredictions);

        BorderPane.setMargin(header, new Insets(0, 0, 20, 0));
        BorderPane.setMargin(sidebar, new Insets(0, 20, 0, 0));
        BorderPane.setMargin(recentPredictions, new Insets(0, 0, 0, 20));
    }
}
