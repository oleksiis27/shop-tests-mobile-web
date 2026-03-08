package com.shop.steps;

import com.shop.pages.CartPage;
import com.shop.pages.OrdersPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckoutSteps {

    private final TestContext context;
    private final CartPage cartPage = new CartPage();
    private final OrdersPage ordersPage = new OrdersPage();

    public CheckoutSteps(TestContext context) {
        this.context = context;
    }

    @When("the user taps the Checkout button")
    public void theUserTapsTheCheckoutButton() {
        cartPage.checkout();
    }

    @Then("the user should be redirected to the orders page")
    public void theUserShouldBeRedirectedToTheOrdersPage() {
        assertTrue(ordersPage.isPageDisplayed(), "User should be on the orders page");
    }

    @Then("at least one order should be displayed")
    public void atLeastOneOrderShouldBeDisplayed() {
        assertTrue(ordersPage.getOrderCount() > 0, "At least one order should be displayed");
    }

    @Then("the empty cart message {string} should be displayed")
    public void theEmptyCartMessageShouldBeDisplayed(String message) {
        assertTrue(cartPage.isCartEmpty(), "Empty cart message should be displayed");
    }

    @Then("the Checkout button should not be visible")
    public void theCheckoutButtonShouldNotBeVisible() {
        assertFalse(cartPage.isCheckoutButtonDisplayed(),
                "Checkout button should not be visible for empty cart");
    }

    @When("the user opens the orders page on mobile")
    public void theUserOpensTheOrdersPageOnMobile() {
        ordersPage.open();
    }

    @Then("the latest order should have status {string}")
    public void theLatestOrderShouldHaveStatus(String expectedStatus) {
        String actualStatus = ordersPage.getOrderStatus(0);
        assertTrue(actualStatus.equalsIgnoreCase(expectedStatus),
                "Latest order status should be '" + expectedStatus + "', but was '" + actualStatus + "'");
    }
}
