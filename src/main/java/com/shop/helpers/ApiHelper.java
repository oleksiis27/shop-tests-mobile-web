package com.shop.helpers;

import com.shop.config.AppConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiHelper {

    private static final String API_URL = AppConfig.getInstance().apiUrl() + "/api";

    private ApiHelper() {
    }

    public static RequestSpecification baseRequest() {
        return RestAssured.given()
                .baseUri(API_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    public static RequestSpecification authenticatedRequest(String token) {
        return baseRequest()
                .header("Authorization", "Bearer " + token);
    }
}
