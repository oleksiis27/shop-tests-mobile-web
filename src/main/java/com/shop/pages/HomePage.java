package com.shop.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class HomePage extends BaseMobilePage {

    // Search
    private final SelenideElement searchInput = $("input[name='search']");
    private final SelenideElement searchButton = $(By.xpath("//button[text()='Search']"));

    // Filters
    private final SelenideElement categorySelect = $(By.xpath("(//select)[1]"));
    private final SelenideElement sortSelect = $(By.xpath("(//select)[2]"));

    // Product grid
    private final ElementsCollection productCards = $$("a[href^='/products/']");
    private final SelenideElement emptyMessage = $(By.xpath("//p[text()='No products found.']"));

    // Pagination
    private final SelenideElement previousButton = $(By.xpath("//button[text()='Previous']"));
    private final SelenideElement nextButton = $(By.xpath("//button[text()='Next']"));
    private final SelenideElement pageIndicator = $(By.xpath("//span[contains(text(),'Page')]"));

    @Step("Open home page")
    public HomePage open() {
        navigateTo(config.baseUrl());
        searchInput.shouldBe(Condition.visible, TIMEOUT);
        return this;
    }

    @Step("Search for product: {query}")
    public HomePage searchProduct(String query) {
        searchInput.clear();
        searchInput.sendKeys(query);
        searchButton.click();
        return this;
    }

    @Step("Select category: {category}")
    public HomePage selectCategory(String category) {
        categorySelect.selectOptionContainingText(category);
        return this;
    }

    @Step("Select sort option: {sortOption}")
    public HomePage selectSort(String sortOption) {
        sortSelect.selectOptionContainingText(sortOption);
        return this;
    }

    @Step("Click on product at index: {index}")
    public void clickProductByIndex(int index) {
        productCards.get(index).shouldBe(Condition.visible, TIMEOUT).click();
    }

    @Step("Click on product by name: {name}")
    public void clickProductByName(String name) {
        $(By.xpath("//h3[contains(text(),'" + name + "')]/ancestor::a")).click();
    }

    @Step("Get number of displayed products")
    public int getProductCount() {
        return productCards.size();
    }

    @Step("Check if product catalog is displayed")
    public boolean isProductCatalogDisplayed() {
        return productCards.size() > 0;
    }

    @Step("Check if empty message is displayed")
    public boolean isEmptyMessageDisplayed() {
        return emptyMessage.isDisplayed();
    }

    @Step("Get product name at index: {index}")
    public String getProductNameByIndex(int index) {
        return productCards.get(index).$("h3").getText();
    }

    @Step("Click next page")
    public HomePage nextPage() {
        nextButton.click();
        return this;
    }

    @Step("Click previous page")
    public HomePage previousPage() {
        previousButton.click();
        return this;
    }

    @Step("Get current page indicator text")
    public String getPageIndicatorText() {
        return pageIndicator.getText();
    }

    @Step("Scroll through product list")
    public HomePage scrollProducts() {
        swipeUp();
        return this;
    }
}
