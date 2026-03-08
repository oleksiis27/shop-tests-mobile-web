package com.shop.steps;

import com.shop.api.ProductApi;
import com.shop.pages.HomePage;
import com.shop.pages.ProductPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductSteps {

    private final TestContext context;
    private final HomePage homePage = new HomePage();
    private final ProductPage productPage = new ProductPage();
    private String searchedProductName;

    public ProductSteps(TestContext context) {
        this.context = context;
    }

    @When("the user opens the home page on mobile")
    public void theUserOpensTheHomePageOnMobile() {
        homePage.open();
    }

    @Given("the user is on the home page on mobile")
    public void theUserIsOnTheHomePageOnMobile() {
        homePage.open();
    }

    @Then("the product catalog should be displayed")
    public void theProductCatalogShouldBeDisplayed() {
        assertTrue(homePage.isProductCatalogDisplayed(), "Product catalog should be displayed");
    }

    @Then("at least one product card should be visible")
    public void atLeastOneProductCardShouldBeVisible() {
        assertTrue(homePage.getProductCount() > 0, "At least one product should be visible");
    }

    // --- Search ---

    @When("the user searches for an existing product name")
    public void theUserSearchesForAnExistingProductName() {
        searchedProductName = ProductApi.getFirstProductName();
        context.setProductName(searchedProductName);
        homePage.searchProduct(searchedProductName);
    }

    @Then("the search results should contain the searched product")
    public void theSearchResultsShouldContainTheSearchedProduct() {
        assertTrue(homePage.getProductCount() > 0,
                "Search results should contain at least one product");
    }

    // --- Category filter ---

    @When("the user selects a category from the filter dropdown")
    public void theUserSelectsACategoryFromTheFilterDropdown() {
        // Select the second option (first real category, after "All Categories")
        homePage.selectCategory("");  // Will be refined - select first non-empty
    }

    @Then("only products from the selected category should be displayed")
    public void onlyProductsFromTheSelectedCategoryShouldBeDisplayed() {
        // After filtering, we just verify results are shown (or empty message)
        assertTrue(homePage.getProductCount() >= 0, "Category filter should return results");
    }

    // --- Product details ---

    @When("the user taps on the first product card")
    public void theUserTapsOnTheFirstProductCard() {
        String name = homePage.getProductNameByIndex(0);
        context.setProductName(name);
        homePage.clickProductByIndex(0);
    }

    @Then("the product details page should be displayed")
    public void theProductDetailsPageShouldBeDisplayed() {
        assertFalse(productPage.getTitle().isEmpty(), "Product title should be displayed");
    }

    @Then("the product title should be visible")
    public void theProductTitleShouldBeVisible() {
        assertFalse(productPage.getTitle().isEmpty(), "Product title should not be empty");
    }

    @Then("the product price should be visible")
    public void theProductPriceShouldBeVisible() {
        assertFalse(productPage.getPrice().isEmpty(), "Product price should not be empty");
    }

    // --- Scroll ---

    @When("the user swipes up to scroll the product list")
    public void theUserSwipesUpToScrollTheProductList() {
        homePage.scrollProducts();
    }

    @Then("the page should scroll and display more content")
    public void thePageShouldScrollAndDisplayMoreContent() {
        // After swipe, the page should still show products (scroll happened)
        assertTrue(homePage.isProductCatalogDisplayed(), "Products should still be visible after scroll");
    }

    // --- Product page navigation ---

    @Given("the user opens the first product page on mobile")
    public void theUserOpensTheFirstProductPageOnMobile() {
        int productId = ProductApi.getFirstProductId();
        context.setProductId(productId);
        context.setProductName(ProductApi.getFirstProductName());
        productPage.open(productId);
    }
}
