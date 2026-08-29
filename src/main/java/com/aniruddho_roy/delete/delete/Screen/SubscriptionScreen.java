package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SubscriptionScreen extends DashboardBase {

    public SubscriptionScreen(NAVIGATOR navigator) {
        this(navigator, false);
    }

    public SubscriptionScreen(
            NAVIGATOR navigator,
            boolean subscribed
    ) {
        super(navigator, subscribed);
        setCenter(createSubscriptionCenter());
    }

    private ScrollPane createSubscriptionCenter() {

        Label heading = new Label("Subscription");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 23));
        heading.getStyleClass().add("heading");

        Label description = new Label(
                "Manage your prediction limits and account plan."
        );
        description.setFont(Font.font("Arial", 14));
        description.getStyleClass().add("muted-text");

        Label status = new Label();
        status.getStyleClass().add("muted-text");

        VBox freePlan = createPlanCard(
                "FREE PLAN",
                "For exploring the prediction workflow",
                "100",
                new String[]{
                        "100 predictions per month",
                        "Merge probability prediction",
                        "Basic PR quality score",
                        "Recent prediction history"
                },
                "Current plan",
                false,
                status
        );

        VBox premiumPlan = createPlanCard(
                "PREMIUM PLAN",
                "For teams that need deeper insights",
                "Unlimited",
                new String[]{
                        "Unlimited predictions",
                        "Merge probability and PR quality",
                        "Advanced explanations",
                        "Priority processing",
                        "Exportable prediction history"
                },
                "Upgrade to Premium",
                true,
                status
        );

        HBox plans = new HBox(
                18,
                freePlan,
                premiumPlan
        );

        HBox.setHgrow(freePlan, Priority.ALWAYS);
        HBox.setHgrow(premiumPlan, Priority.ALWAYS);

        VBox content = new VBox(
                18,
                heading,
                description,
                status,
                plans
        );

        content.setPadding(new Insets(25));
        content.setMaxWidth(Double.MAX_VALUE);
        content.getStyleClass().addAll(
                "surface-card",
                "dashboard-content"
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );
        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );
        scrollPane.getStyleClass().add(
                "transparent-scroll-pane"
        );

        return scrollPane;
    }

    private VBox createPlanCard(
            String title,
            String description,
            String price,
            String[] features,
            String buttonText,
            boolean premium,
            Label status
    ) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 12)
        );
        titleLabel.getStyleClass().add(
                premium ? "premium-feature" : "muted-text"
        );

        Label heading = new Label(
                premium ? "Premium" : "Free"
        );
        heading.setFont(
                Font.font("Arial", FontWeight.BOLD, 20)
        );
        heading.getStyleClass().add("heading");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.getStyleClass().add("muted-text");

        Label priceLabel = new Label(
                premium ? price + " / month" : price + " predictions"
        );
        priceLabel.setFont(
                Font.font("Arial", FontWeight.BOLD, 25)
        );
        priceLabel.getStyleClass().add("stat-value");

        VBox featureList = new VBox(8);

        for (String feature : features) {
            Label featureLabel = new Label("✓ " + feature);
            featureLabel.getStyleClass().add("muted-text");
            featureList.getChildren().add(featureLabel);
        }

        Button actionButton = new Button(buttonText);
        actionButton.setPrefHeight(40);
        actionButton.setMaxWidth(Double.MAX_VALUE);
        actionButton.getStyleClass().add(
                premium
                        ? "primary-button"
                        : "secondary-button"
        );

        actionButton.setOnAction(event ->
                status.setText(
                        premium
                                ? "Premium upgrade will be connected to the backend."
                                : "You are currently using the free plan."
                )
        );

        VBox card = new VBox(
                12,
                titleLabel,
                heading,
                descriptionLabel,
                priceLabel,
                featureList,
                actionButton
        );

        card.setPadding(new Insets(20));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setMinHeight(350);
        card.getStyleClass().addAll(
                "surface-card",
                premium ? "premium-plan" : "free-plan"
        );

        return card;
    }
}