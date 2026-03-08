package com.shop.api;

import com.shop.helpers.ApiHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class CartApi {

    @Step("API: Get cart")
    public static Response getCart(String token) {
        return ApiHelper.authenticatedRequest(token)
                .get("/cart");
    }

    @Step("API: Add product {productId} to cart, quantity: {quantity}")
    public static Response addItem(String token, int productId, int quantity) {
        Map<String, Object> body = new HashMap<>();
        body.put("product_id", productId);
        body.put("quantity", quantity);

        return ApiHelper.authenticatedRequest(token)
                .body(body)
                .post("/cart/items");
    }

    @Step("API: Add product {productId} to cart")
    public static Response addItem(String token, int productId) {
        return addItem(token, productId, 1);
    }

    @Step("API: Update cart item {itemId} quantity to {quantity}")
    public static Response updateItem(String token, int itemId, int quantity) {
        Map<String, Object> body = new HashMap<>();
        body.put("quantity", quantity);

        return ApiHelper.authenticatedRequest(token)
                .body(body)
                .put("/cart/items/" + itemId);
    }

    @Step("API: Remove cart item {itemId}")
    public static Response removeItem(String token, int itemId) {
        return ApiHelper.authenticatedRequest(token)
                .delete("/cart/items/" + itemId);
    }

    @Step("API: Clear cart")
    public static Response clearCart(String token) {
        return ApiHelper.authenticatedRequest(token)
                .delete("/cart");
    }
}
