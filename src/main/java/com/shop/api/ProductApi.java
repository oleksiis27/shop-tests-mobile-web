package com.shop.api;

import com.shop.helpers.ApiHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductApi {

    @Step("API: Get all products")
    public static Response getProducts() {
        return ApiHelper.baseRequest()
                .get("/products");
    }

    @Step("API: Search products by query: {query}")
    public static Response searchProducts(String query) {
        return ApiHelper.baseRequest()
                .queryParam("search", query)
                .get("/products");
    }

    @Step("API: Get product by ID: {productId}")
    public static Response getProduct(int productId) {
        return ApiHelper.baseRequest()
                .get("/products/" + productId);
    }

    @Step("API: Create product: {name}")
    public static Response createProduct(String token, String name, String description,
                                         double price, int stock, int categoryId) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("description", description);
        body.put("price", price);
        body.put("stock", stock);
        body.put("category_id", categoryId);

        return ApiHelper.authenticatedRequest(token)
                .body(body)
                .post("/products");
    }

    @Step("API: Delete product: {productId}")
    public static Response deleteProduct(String token, int productId) {
        return ApiHelper.authenticatedRequest(token)
                .delete("/products/" + productId);
    }

    @Step("API: Get categories")
    public static Response getCategories() {
        return ApiHelper.baseRequest()
                .get("/categories");
    }

    @Step("API: Get first category ID")
    public static int getFirstCategoryId() {
        List<Map<String, Object>> categories = getCategories()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("");

        if (categories.isEmpty()) {
            throw new RuntimeException("No categories found in the system");
        }
        return (int) categories.get(0).get("id");
    }

    @Step("API: Get first available product ID")
    public static int getFirstProductId() {
        return getProducts()
                .then()
                .statusCode(200)
                .extract()
                .path("items[0].id");
    }

    @Step("API: Get first available product name")
    public static String getFirstProductName() {
        return getProducts()
                .then()
                .statusCode(200)
                .extract()
                .path("items[0].name");
    }
}
