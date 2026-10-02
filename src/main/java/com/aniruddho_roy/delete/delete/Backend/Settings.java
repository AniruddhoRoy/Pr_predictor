package com.aniruddho_roy.delete.delete.Backend;

import com.aniruddho_roy.delete.delete.Backend.DTO.UserSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.http.HttpResponse;

public class Settings extends Base {

    private final ObjectMapper mapper;

    public Settings() {
        mapper = new ObjectMapper();
    }


    public UserSettings getSettings() {

        try {

            HttpResponse<String> response =
                    get_authenticated_request("/settings");

            if (response.statusCode() == 401) {
                System.out.println("Authentication failed");
                return null;
            }

            if (response.statusCode() != 200) {

                System.out.println(
                        "Could not load settings: "
                                + response.statusCode()
                );

                return null;
            }

            return mapper.readValue(
                    response.body(),
                    UserSettings.class
            );

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }


    public int updateSettings(
            String themeMode,
            boolean notificationsEnabled,
            String predictionType,
            String inputMode
    ) {

        try {

            ObjectNode body =
                    mapper.createObjectNode();

            body.put(
                    "themeMode",
                    themeMode
            );

            body.put(
                    "notificationsEnabled",
                    notificationsEnabled
            );

            body.put(
                    "defaultPredictionType",
                    predictionType
            );

            body.put(
                    "defaultInputMode",
                    inputMode
            );


            HttpResponse<String> response =
                    patch_authenticated_request(
                            "/settings",
                            body.toString()
                    );

            return response.statusCode();

        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }
}