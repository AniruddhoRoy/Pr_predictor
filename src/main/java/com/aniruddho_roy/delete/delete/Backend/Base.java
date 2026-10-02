package com.aniruddho_roy.delete.delete.Backend;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class Base {

    private static final String API_URL =
            "http://127.0.0.1:8000";

    private final HttpClient client;

    protected Base() {

        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    protected HttpResponse<String> post_request(
            String url,
            String body
    ) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + url))
                .version(HttpClient.Version.HTTP_1_1)
                .header(
                        "Content-Type",
                        "application/json"
                )
                .header(
                        "Accept",
                        "application/json"
                )
                .POST(
                        HttpRequest.BodyPublishers.ofString(body)
                )
                .build();

        return execute_request(request);
    }

    private HttpResponse<String> execute_request(
            HttpRequest request
    ) {

        try {

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "HTTP Status: "
                            + response.statusCode()
            );

            System.out.println(
                    "Response: "
                            + response.body()
            );

            return response;

        } catch (Exception e) {

            throw new RuntimeException(
                    "HTTP request failed",
                    e
            );
        }
    }
}