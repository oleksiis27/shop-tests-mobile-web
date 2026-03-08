package com.shop.api;

import com.shop.helpers.ApiHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class AuthApi {

    @Step("API: Register user with email {email}")
    public static Response register(String name, String email, String password) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("email", email);
        body.put("password", password);

        return ApiHelper.baseRequest()
                .body(body)
                .post("/auth/register");
    }

    @Step("API: Login with email {email}")
    public static Response login(String email, String password) {
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        return ApiHelper.baseRequest()
                .body(body)
                .post("/auth/login");
    }

    @Step("API: Register and get token for {email}")
    public static String registerAndGetToken(String name, String email, String password) {
        register(name, email, password);
        return getToken(email, password);
    }

    @Step("API: Get auth token for {email}")
    public static String getToken(String email, String password) {
        return login(email, password)
                .then()
                .statusCode(200)
                .extract()
                .path("access_token");
    }

    @Step("API: Get current user info")
    public static Response getCurrentUser(String token) {
        return ApiHelper.authenticatedRequest(token)
                .get("/auth/me");
    }
}
