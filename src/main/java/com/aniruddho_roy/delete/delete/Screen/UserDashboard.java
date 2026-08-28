package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.additional.COMPONETS;

import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Label;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;



public class UserDashboard extends DashboardBase {

    public UserDashboard(NAVIGATOR navigator) {
        this(navigator, false);
    }

    /*
     * Pass true if the user has a premium subscription.
     */
    public UserDashboard(NAVIGATOR navigator, boolean subscribed) {
        super(navigator,subscribed);
//        this.centerContent = createDashboardContent();
        this.setCenter(createDashboardContent());
    }



    private VBox createDashboardContent() {

        Label heading = new Label("Dashboard Overview");
        heading.setFont(
                Font.font("Arial", FontWeight.BOLD, 23)
        );
        heading.getStyleClass().add("heading");


        Label subheading = new Label(
                "Monitor your prediction activity and account usage."
        );
        subheading.setFont(Font.font("Arial", 14));
        subheading.getStyleClass().add("muted-text");


        HBox summaryCards = new HBox(
                12,
                new COMPONETS().createStatCard(
                        "Total Predictions",
                        "48",
                        "All-time analyses",
                        "stat-blue"
                ),
                new COMPONETS().createStatCard(
                        "High Probability",
                        "29",
                        "Above 70%",
                        "stat-green"
                ),
                new COMPONETS().createStatCard(
                        "Average Probability",
                        "72%",
                        "All predictions",
                        "stat-orange"
                ),
                new COMPONETS().createStatCard(
                        "Monthly Usage",
                        this.subscribed ? "Unlimited" : "48 / 100",
                        this.subscribed
                                ? "Premium account"
                                : "52 remaining",
                        "stat-purple"
                )
        );


        summaryCards.setAlignment(Pos.CENTER);
        summaryCards.setMaxWidth(Double.MAX_VALUE);


        VBox insightsCard = new COMPONETS().createInsightsCard();

        VBox quickActionCard =
                new COMPONETS().createQuickActionCard(navigator);

        VBox subscriptionCard =
                new COMPONETS().createSubscriptionCard(subscribed);


        HBox bottomCards = new HBox(
                15,
                quickActionCard,
                subscriptionCard
        );


        HBox.setHgrow(quickActionCard, Priority.ALWAYS);
        HBox.setHgrow(subscriptionCard, Priority.ALWAYS);


        quickActionCard.setMaxWidth(Double.MAX_VALUE);
        subscriptionCard.setMaxWidth(Double.MAX_VALUE);


        VBox content = new VBox(
                18,
                heading,
                subheading,
                summaryCards,
                insightsCard,
                bottomCards
        );


        content.setPadding(new Insets(25));
        content.setMaxWidth(Double.MAX_VALUE);


        content.getStyleClass().addAll(
                "surface-card",
                "dashboard-content"
        );


        return content;
    }

}
