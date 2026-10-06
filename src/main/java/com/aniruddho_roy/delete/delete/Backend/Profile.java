package com.aniruddho_roy.delete.delete.Backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.http.HttpResponse;

public class Profile extends Base {

    private final ObjectMapper mapper;

    public Profile() {
        mapper = new ObjectMapper();
    }
    public boolean getProfile_test() {

        try {

            HttpResponse<String> response =
                    get_authenticated_request("/me");

            if (response.statusCode() == 401) {

                System.out.println(
                        "Authentication failed"
                );

                return false;
            }

            if (response.statusCode() != 200) {

                System.out.println(
                        "Could not load profile: "
                                + response.statusCode()
                );

                return false;
            }

            return  true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }


    public User getProfile() {

        try {

            HttpResponse<String> response =
                    get_authenticated_request("/me");

            if (response.statusCode() == 401) {

                System.out.println(
                        "Authentication failed"
                );

                return null;
            }

            if (response.statusCode() != 200) {

                System.out.println(
                        "Could not load profile: "
                                + response.statusCode()
                );

                return null;
            }

            return mapper.readValue(
                    response.body(),
                    User.class
            );

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }


    public int updateProfile(
            String fullName,
            String email,
            String githubProfileUrl
    ) {

        try {

            ObjectNode body =
                    mapper.createObjectNode();

            body.put(
                    "fullName",
                    fullName.trim()
            );

            body.put(
                    "email",
                    email.trim()
            );

            if (githubProfileUrl == null
                    || githubProfileUrl.isBlank()) {

                body.putNull(
                        "githubProfileUrl"
                );

            } else {

                body.put(
                        "githubProfileUrl",
                        githubProfileUrl.trim()
                );
            }


            HttpResponse<String> response =
                    patch_authenticated_request(
                            "/profile",
                            body.toString()
                    );

            return response.statusCode();

        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }
    public int changePassword(
            String currentPassword,
            String newPassword
    ) {

        try {

            // Passwords are sent exactly as typed (no trim)
            ObjectNode body = mapper.createObjectNode();
            body.put("currentPassword", currentPassword);
            body.put("newPassword", newPassword);

            HttpResponse<String> response =
                    post_authenticated_request(
                            "/change-password",
                            body.toString()
                    );

            return response.statusCode();

        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }
    // import com.fasterxml.jackson.databind.JsonNode;

    public String getFullName() {

        try {

            HttpResponse<String> response =
                    get_authenticated_request("/me");

            if (response.statusCode() != 200) {
                return null; // 401 = expired token, other = server problem
            }

            JsonNode json = mapper.readTree(response.body());
            String fullName = json.path("fullName").asText("").trim();

            return fullName.isEmpty() ? null : fullName;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }
}