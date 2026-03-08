package com.shop.steps;

import com.shop.api.AuthApi;
import com.shop.api.CartApi;
import com.shop.api.ProductApi;
import com.shop.config.AppConfig;
import com.shop.config.DriverFactory;
import com.shop.helpers.TestDataHelper;
import com.shop.pages.HomePage;
import io.cucumber.java.en.Given;

public class CommonSteps {

    private final TestContext context;
    private final HomePage homePage = new HomePage();

    public CommonSteps(TestContext context) {
        this.context = context;
    }

    @Given("a registered user exists")
    public void aRegisteredUserExists() {
        String name = TestDataHelper.randomName();
        String email = TestDataHelper.randomEmail();
        String password = TestDataHelper.randomPassword();

        AuthApi.register(name, email, password)
                .then().statusCode(201);

        context.setUserName(name);
        context.setUserEmail(email);
        context.setUserPassword(password);
        context.setAuthToken(AuthApi.getToken(email, password));
    }

    @Given("the user is logged in on mobile")
    public void theUserIsLoggedInOnMobile() {
        String baseUrl = AppConfig.getInstance().baseUrl();
        DriverFactory.getDriver().get(baseUrl + "/login");

        var loginPage = new com.shop.pages.LoginPage();
        loginPage.loginAs(context.getUserEmail(), context.getUserPassword());
        loginPage.waitForVisible(loginPage.logoutButton);
    }

    @Given("the cart is empty via API")
    public void theCartIsEmptyViaApi() {
        CartApi.clearCart(context.getAuthToken());
    }

    @Given("a product is added to the cart via API")
    public void aProductIsAddedToTheCartViaApi() {
        int productId = ProductApi.getFirstProductId();
        context.setProductId(productId);
        context.setProductName(ProductApi.getFirstProductName());

        CartApi.addItem(context.getAuthToken(), productId)
                .then().statusCode(201);
    }

    @Given("{int} products are added to the cart via API")
    public void productsAreAddedToTheCartViaApi(int count) {
        var products = ProductApi.getProducts()
                .then().statusCode(200)
                .extract().jsonPath()
                .getList("items.id", Integer.class);

        for (int i = 0; i < count && i < products.size(); i++) {
            CartApi.addItem(context.getAuthToken(), products.get(i))
                    .then().statusCode(201);
        }
    }

    @Given("the user completes checkout via API")
    public void theUserCompletesCheckoutViaApi() {
        com.shop.api.OrderApi.createOrder(context.getAuthToken())
                .then().statusCode(201);
    }
}
