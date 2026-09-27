package com.aniruddho_roy.delete.delete.Backend;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public abstract class Base {
    private static final String API_URL = "http://127.0.0.1:8000";
    private final HttpClient client;
    Base(){
        client = HttpClient.newHttpClient();
    }
    protected String post_request(String url,String body){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL+url))
                .header("Content-Type", "application/json")
                .POST(
                        HttpRequest.BodyPublishers.ofString(body)
                )
                .build();
        return execute_request(request);
    }
    private String execute_request(HttpRequest request){
        try {
            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );


        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "Backend error: " + response.statusCode()
            );
        }

        return response.body();
        }catch (Exception e){
            System.out.println("Error while requesting http");
    }
        return "Execution Error";
    }

}

