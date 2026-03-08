package com.shop.api;

import com.shop.helpers.ApiHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class OrderApi {

    @Step("API: Create order from cart")
    public static Response createOrder(String token) {
        return ApiHelper.authenticatedRequest(token)
                .post("/orders");
    }

    @Step("API: Get all orders for current user")
    public static Response getOrders(String token) {
        return ApiHelper.authenticatedRequest(token)
                .get("/orders");
    }

    @Step("API: Get order by ID: {orderId}")
    public static Response getOrder(String token, int orderId) {
        return ApiHelper.authenticatedRequest(token)
                .get("/orders/" + orderId);
    }
}
