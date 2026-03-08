package com.shop.steps;

import com.shop.api.CartApi;
import com.shop.config.DriverFactory;
import com.shop.pages.CartPage;
import com.shop.pages.ProductPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartSteps {

    private final TestContext context;
    private final CartPage cartPage = new CartPage();
    private final ProductPage productPage = new ProductPage();
    private String initialQuantity;

    public CartSteps(TestContext context) {
        this.context = context;
    }

    // --- Add to cart ---

    @When("the user taps the Add to Cart button")
    public void theUserTapsTheAddToCartButton() {
        productPage.addToCart();
    }

    @Then("the {string} success message should appear")
    public void theSuccessMessageShouldAppear(String message) {
        assertTrue(productPage.isAddedToCartMessageDisplayed(),
                "'" + message + "' message should be displayed");
    }

    @Then("the cart should contain {int} item(s) via API")
    public void theCartShouldContainItemsViaApi(int expectedCount) {
        int actualCount = CartApi.getCart(context.getAuthToken())
                .then().statusCode(200)
                .extract().jsonPath()
                .getList("items").size();
        assertEquals(expectedCount, actualCount,
                "Cart should contain " + expectedCount + " item(s)");
    }

    // --- Cart page operations ---

    @Given("the user opens the cart page on mobile")
    public void theUserOpensTheCartPageOnMobile() {
        cartPage.open();
    }

    @When("the user taps the increase quantity button for the first item")
    public void theUserTapsTheIncreaseQuantityButtonForTheFirstItem() {
        initialQuantity = cartPage.getItemQuantity(0);
        cartPage.increaseQuantity(0);
    }

    @Then("the item quantity should be updated")
    public void theItemQuantityShouldBeUpdated() {
        int expected = Integer.parseInt(initialQuantity) + 1;
        String expectedStr = String.valueOf(expected);

        // Wait up to 5 seconds for quantity to update in UI
        int attempts = 10;
        String newQuantity = "";
        while (attempts-- > 0) {
            newQuantity = cartPage.getItemQuantity(0);
            if (expectedStr.equals(newQuantity)) break;
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }

        assertEquals(expectedStr, newQuantity,
                "Quantity should be increased by 1");
    }

    @When("the user taps the Remove button for the first item")
    public void theUserTapsTheRemoveButtonForTheFirstItem() {
        cartPage.removeItem(0);
    }

    @When("the user taps the Clear Cart button")
    public void theUserTapsTheClearCartButton() {
        cartPage.clearCart();
    }

    @Then("the cart should be empty on the page")
    public void theCartShouldBeEmptyOnThePage() {
        assertTrue(cartPage.isCartEmpty(), "Cart should display empty message");
    }

    // --- Refresh ---

    @When("the user refreshes the page")
    public void theUserRefreshesThePage() {
        DriverFactory.getDriver().navigate().refresh();
    }

    @Then("the cart should still contain {int} item(s) on the page")
    public void theCartShouldStillContainItemsOnThePage(int expectedCount) {
        // Wait for page reload
        cartPage.waitForVisible(cartPage.pageTitle);
        assertEquals(expectedCount, cartPage.getCartItemCount(),
                "Cart should contain " + expectedCount + " item(s) after refresh");
    }
}
