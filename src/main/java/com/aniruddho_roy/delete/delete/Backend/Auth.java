package com.aniruddho_roy.delete.delete.Backend;

import com.aniruddho_roy.delete.delete.Auth.TokenStorage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;

public class Auth extends Base {

    private final ObjectMapper mapper;

    public Auth() {
        mapper = new ObjectMapper();
    }

    public boolean login(String username, String password) {

        try {

            String body = mapper.createObjectNode()
                    .put("username", username)
                    .put("password", password)
                    .toString();

            HttpResponse<String> response =
                    post_request("/login", body);

            if (response.statusCode() == 401) {
                System.out.println("Wrong username or password");
                return false;
            }

            if (response.statusCode() != 200) {
                System.out.println(
                        "Login failed: " + response.statusCode()
                );
                return false;
            }

            JsonNode json =
                    mapper.readTree(response.body());

            String accessToken =
                    json.get("accessToken").asText();

            String tokenType =
                    json.get("tokenType").asText();

            JsonNode user =
                    json.get("user");

            System.out.println("Token: " + accessToken);
            System.out.println(
                    "Username: "
                            + user.get("username").asText()
            );
            TokenStorage.saveToken(accessToken);
//            Session.saveToken(accessToken);

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
    public int register(
            String fullName,
            String username,
            String email,
            String password
    ) {

        try {

            String body = mapper.createObjectNode()
                    .put("username", username.trim())
                    .put("email", email.trim())
                    .put("password", password)
                    .put("fullName", fullName.trim())
                    .toString();

            HttpResponse<String> response =
                    post_request("/register", body);

            // Duplicate username/email
            if (response.statusCode() == 409) {
                return 409;
            }

            // Registration should return 201
            if (response.statusCode() != 201) {

                System.out.println(
                        "Registration failed: "
                                + response.statusCode()
                );

                System.out.println(response.body());

                return response.statusCode();
            }

            JsonNode json =
                    mapper.readTree(response.body());

            String accessToken =
                    json.get("accessToken").asText();

            JsonNode user =
                    json.get("user");

            // Save token because registration automatically signs user in
            TokenStorage.saveToken(accessToken);

            System.out.println(
                    "Registered as: "
                            + user.get("username").asText()
            );

            return 201;

        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }
}