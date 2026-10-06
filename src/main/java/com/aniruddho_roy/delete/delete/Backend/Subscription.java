package com.aniruddho_roy.delete.delete.Backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Subscription extends Base {

    private final ObjectMapper mapper = new ObjectMapper();

    public record PlanInfo(
            String code,
            String name,
            int monthlyCredits,
            String description,
            List<String> modelNames
    ) {}

    public record CurrentSubscription(
            String planCode,
            String planName,
            String status,
            String startedAt,
            int monthlyCredits,
            int creditsUsed,
            int creditsRemaining
    ) {}


    /** GET /plans. Returns null if the request fails. */
    public List<PlanInfo> getPlans() {
        try {
            HttpResponse<String> response =
                    get_authenticated_request("/plans");

            if (response.statusCode() != 200) {
                return null;
            }

            JsonNode array = mapper.readTree(response.body());

            List<PlanInfo> plans = new ArrayList<>();
            for (JsonNode node : array) {
                plans.add(parsePlan(node));
            }
            return plans;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * GET /subscription (status, startedAt)
     * + GET /active-plan (creditsUsed, creditsRemaining).
     * Returns null if either request fails.
     */
    public CurrentSubscription getCurrentSubscription() {
        try {
            HttpResponse<String> subResponse =
                    get_authenticated_request("/subscription");
            HttpResponse<String> planResponse =
                    get_authenticated_request("/active-plan");

            if (subResponse.statusCode() != 200
                    || planResponse.statusCode() != 200) {
                return null;
            }

            JsonNode sub = mapper.readTree(subResponse.body());
            JsonNode plan = mapper.readTree(planResponse.body());

            String startedAt = sub.path("startedAt").asText("");
            int t = startedAt.indexOf('T');
            if (t > 0) {
                startedAt = startedAt.substring(0, t);
            }

            return new CurrentSubscription(
                    plan.path("code").asText(""),
                    plan.path("name").asText(""),
                    sub.path("status").asText(""),
                    startedAt,
                    plan.path("monthlyCredits").asInt(0),
                    plan.path("creditsUsed").asInt(0),
                    plan.path("creditsRemaining").asInt(0)
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * POST /subscription.
     * Returns the HTTP status code (200 ok, 404 plan not found,
     * 401 session expired) or -1 if the request failed.
     */
    public int changePlan(String planCode) {
        try {
            ObjectNode body = mapper.createObjectNode();
            body.put("planCode", planCode);

            HttpResponse<String> response =
                    post_authenticated_request(
                            "/subscription",
                            body.toString()
                    );

            return response.statusCode();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    private PlanInfo parsePlan(JsonNode json) {
        List<String> modelNames = new ArrayList<>();
        for (JsonNode model : json.path("models")) {
            modelNames.add(model.path("name").asText(""));
        }

        return new PlanInfo(
                json.path("code").asText(""),
                json.path("name").asText(""),
                json.path("monthlyCredits").asInt(0),
                json.path("description").asText(""),
                modelNames
        );
    }
}