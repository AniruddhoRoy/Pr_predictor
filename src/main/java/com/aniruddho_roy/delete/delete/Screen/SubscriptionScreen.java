package com.aniruddho_roy.delete.delete.Screen;

import com.aniruddho_roy.delete.delete.Backend.Subscription;
import com.aniruddho_roy.delete.delete.Backend.Subscription.CurrentSubscription;
import com.aniruddho_roy.delete.delete.Backend.Subscription.PlanInfo;
import com.aniruddho_roy.delete.delete.additional.NAVIGATOR;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class SubscriptionScreen extends DashboardBase {

    private final Subscription subscriptionApi = new Subscription();

    private final VBox plansBox = new VBox(18);
    private final Label status = new Label();

    public SubscriptionScreen(NAVIGATOR navigator) {
        this(navigator, false);
    }

    public SubscriptionScreen(
            NAVIGATOR navigator,
            boolean subscribed
    ) {
        super(navigator, subscribed);
        setCenter(createSubscriptionCenter());
        loadData("");
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

        status.getStyleClass().add("muted-text");
        status.setWrapText(true);

        VBox content = new VBox(
                18,
                heading,
                description,
                status,
                plansBox
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

    /** Loads plans and the current plan code, then rebuilds the cards. */
    private void loadData(String successMessage) {

        status.setText("Loading...");

        Thread thread = new Thread(() -> {

            List<PlanInfo> plans = subscriptionApi.getPlans();
            CurrentSubscription current =
                    subscriptionApi.getCurrentSubscription();

            Platform.runLater(() -> {

                if (plans == null || current == null) {
                    status.setText(
                            "Could not load subscription data. "
                                    + "Please check your connection or login again."
                    );
                    return;
                }

                status.setText(successMessage);

                plansBox.getChildren().clear();
                for (PlanInfo plan : plans) {
                    plansBox.getChildren().add(
                            createPlanCard(
                                    plan,
                                    plan.code().equals(current.planCode())
                            )
                    );
                }
            });
        }, "subscription-loader");

        thread.setDaemon(true);
        thread.start();
    }

    private VBox createPlanCard(PlanInfo plan, boolean isCurrent) {

        Label titleLabel = new Label(plan.code() + " PLAN");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titleLabel.getStyleClass().add("muted-text");

        Label heading = new Label(plan.name());
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        heading.getStyleClass().add("heading");

        Label descriptionLabel = new Label(plan.description());
        descriptionLabel.setWrapText(true);
        descriptionLabel.getStyleClass().add("muted-text");

        Label priceLabel = new Label(
                plan.monthlyCredits() + " credits / month"
        );
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        priceLabel.getStyleClass().add("stat-value");

        VBox featureList = new VBox(8);
        for (String modelName : plan.modelNames()) {
            Label featureLabel = new Label("✓ " + modelName);
            featureLabel.setWrapText(true);
            featureLabel.getStyleClass().add("muted-text");
            featureList.getChildren().add(featureLabel);
        }

        Button actionButton = new Button(
                isCurrent ? "Current plan" : "Switch to " + plan.name()
        );
        actionButton.setPrefHeight(40);
        actionButton.setMaxWidth(Double.MAX_VALUE);
        actionButton.setDisable(isCurrent);
        actionButton.getStyleClass().add(
                isCurrent ? "secondary-button" : "primary-button"
        );

        actionButton.setOnAction(event -> {

            actionButton.setDisable(true);
            status.setText("Switching to " + plan.name() + "...");

            Thread thread = new Thread(() -> {

                int result = subscriptionApi.changePlan(plan.code());

                Platform.runLater(() -> {
                    switch (result) {
                        case 200 -> loadData(
                                "Switched to the " + plan.name() + " plan."
                        );
                        case 404 -> {
                            status.setText("Plan not found.");
                            actionButton.setDisable(false);
                        }
                        case 401 -> {
                            status.setText(
                                    "Your session has expired. Please login again."
                            );
                            actionButton.setDisable(false);
                        }
                        default -> {
                            status.setText("Could not change plan.");
                            actionButton.setDisable(false);
                        }
                    }
                });
            }, "subscription-switch");

            thread.setDaemon(true);
            thread.start();
        });

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
        card.getStyleClass().add("surface-card");

        return card;
    }
}