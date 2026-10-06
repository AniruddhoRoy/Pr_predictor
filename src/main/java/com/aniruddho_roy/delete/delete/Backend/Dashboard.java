package com.aniruddho_roy.delete.delete.Backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;


public class Dashboard extends Base {

    private final ObjectMapper mapper =
            new ObjectMapper();


    // =========================================================
    // GET /dashboard
    // =========================================================

    public JsonNode getDashboard() {

        try {

            HttpResponse<String> response =
                    get_authenticated_request(
                            "/dashboard"
                    );


            if (
                    response.statusCode() < 200 ||
                            response.statusCode() >= 300
            ) {

                throw new RuntimeException(
                        extractError(
                                response.body()
                        )
                );
            }


            return mapper.readTree(
                    response.body()
            );


        } catch (RuntimeException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to load dashboard.",
                    e
            );
        }
    }


    // =========================================================
    // Error extraction
    // =========================================================

    private String extractError(
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


            if (detail.isTextual()) {

                return detail.asText();
            }


            if (
                    detail.isArray() &&
                            !detail.isEmpty()
            ) {

                JsonNode first =
                        detail.get(0);


                JsonNode message =
                        first.get("msg");


                if (message != null) {

                    return message.asText();
                }
            }


            return detail.toString();


        } catch (Exception e) {

            return body;
        }
    }
}