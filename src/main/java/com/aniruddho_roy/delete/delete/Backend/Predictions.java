package com.aniruddho_roy.delete.delete.Backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Predictions extends Base {

    private final ObjectMapper mapper;

    public Predictions() {
        mapper = new ObjectMapper();
    }


    // =========================================================
    // GET /models
    // =========================================================

    public JsonNode getModels(String predictionType) {

        try {

            String url = "/models";

            if (
                    predictionType != null &&
                            !predictionType.isBlank()
            ) {

                url += "?predictionType=" +
                        URLEncoder.encode(
                                predictionType,
                                StandardCharsets.UTF_8
                        );
            }


            HttpResponse<String> response =
                    get_authenticated_request(url);


            checkResponse(response);


            return mapper.readTree(
                    response.body()
            );


        } catch (RuntimeException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to load prediction models.",
                    e
            );
        }
    }


    // =========================================================
    // POST /predict
    // =========================================================

    public JsonNode predict(
            String pullRequestUrl,
            String predictionType,
            String modelId
    ) {

        try {

            ObjectNode body =
                    mapper.createObjectNode();


            body.put(
                    "pullRequestUrl",
                    pullRequestUrl
            );


            body.put(
                    "predictionType",
                    predictionType
            );


            if (
                    modelId != null &&
                            !modelId.isBlank()
            ) {

                body.put(
                        "modelId",
                        modelId
                );
            }


            String jsonBody =
                    mapper.writeValueAsString(
                            body
                    );


            HttpResponse<String> response =
                    post_authenticated_request(
                            "/predict",
                            jsonBody
                    );


            checkResponse(response);


            return mapper.readTree(
                    response.body()
            );


        } catch (RuntimeException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Prediction request failed.",
                    e
            );
        }
    }


    // =========================================================
    // Check HTTP response
    // =========================================================

    private void checkResponse(
            HttpResponse<String> response
    ) {

        int statusCode =
                response.statusCode();


        if (
                statusCode >= 200 &&
                        statusCode < 300
        ) {

            return;
        }


        String errorMessage =
                extractErrorMessage(
                        response.body()
                );


        throw new RuntimeException(
                errorMessage
        );
    }


    // =========================================================
    // Extract FastAPI error message
    // =========================================================

    private String extractErrorMessage(
            String body
    ) {

        if (
                body == null ||
                        body.isBlank()
        ) {

            return "Server returned an error.";
        }


        try {

            JsonNode json =
                    mapper.readTree(body);


            JsonNode detail =
                    json.get("detail");


            if (detail == null) {

                return body;
            }


            // ---------------------------------------------
            // Normal FastAPI HTTPException
            //
            // {
            //   "detail": "Not enough credits..."
            // }
            // ---------------------------------------------

            if (detail.isTextual()) {

                return detail.asText();
            }


            // ---------------------------------------------
            // FastAPI validation error
            //
            // {
            //   "detail": [
            //      {
            //         "msg": "Field required"
            //      }
            //   ]
            // }
            // ---------------------------------------------

            if (
                    detail.isArray() &&
                            !detail.isEmpty()
            ) {

                JsonNode firstError =
                        detail.get(0);


                JsonNode message =
                        firstError.get("msg");


                if (message != null) {

                    return message.asText();
                }
            }


            return detail.toString();


        } catch (Exception e) {

            return body;
        }
    }
    // =========================================================
// GET /history?search=...&limit=...
// =========================================================

    public JsonNode getHistory(String search, int limit) {

        try {

            int safeLimit = Math.max(1, Math.min(limit, 100)); // API allows 1-100

            StringBuilder url = new StringBuilder("/history?limit=")
                    .append(safeLimit);

            if (search != null && !search.isBlank()) {
                url.append("&search=")
                        .append(URLEncoder.encode(search.trim(), StandardCharsets.UTF_8));
            }

            HttpResponse<String> response =
                    get_authenticated_request(url.toString());

            checkResponse(response);

            return mapper.readTree(response.body());

        } catch (RuntimeException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException("Unable to load prediction history.", e);
        }
    }
}