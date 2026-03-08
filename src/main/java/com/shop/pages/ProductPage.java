package com.shop.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class ProductPage extends BaseMobilePage {

    private final SelenideElement productTitle = $("h1");
    private final SelenideElement productPrice = $("p.text-3xl.font-bold.text-gray-900");
    private final SelenideElement productDescription = $("p.text-gray-600");
    private final SelenideElement productCategory = $("p.text-sm.text-indigo-600");
    private final SelenideElement productImage = $("img.w-full.h-96");
    private final SelenideElement stockStatus = $("p.text-sm.mb-6");
    private final SelenideElement quantityInput = $("input[type='number']");
    private final SelenideElement addToCartButton = $(By.xpath("//button[text()='Add to Cart']"));
    private final SelenideElement successMessage = $(By.xpath("//p[text()='Added to cart!']"));

    @Step("Open product page by ID: {productId}")
    public ProductPage open(int productId) {
        navigateTo(config.baseUrl() + "/products/" + productId);
        productTitle.shouldBe(Condition.visible, TIMEOUT);
        return this;
    }

    @Step("Get product title")
    public String getTitle() {
        return productTitle.shouldBe(Condition.visible, TIMEOUT).getText();
    }

    @Step("Get product price")
    public String getPrice() {
        return productPrice.shouldBe(Condition.visible, TIMEOUT).getText();
    }

    @Step("Get product description")
    public String getDescription() {
        return productDescription.shouldBe(Condition.visible, TIMEOUT).getText();
    }

    @Step("Get product category")
    public String getCategory() {
        return productCategory.shouldBe(Condition.visible, TIMEOUT).getText();
    }

    @Step("Check if product image is displayed")
    public boolean isImageDisplayed() {
        return productImage.isDisplayed();
    }

    @Step("Get stock status text")
    public String getStockStatus() {
        return stockStatus.shouldBe(Condition.visible, TIMEOUT).getText();
    }

    @Step("Check if product is in stock")
    public boolean isInStock() {
        return stockStatus.getText().contains("in stock");
    }

    @Step("Set quantity to: {quantity}")
    public ProductPage setQuantity(int quantity) {
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(quantity));
        return this;
    }

    @Step("Click Add to Cart button")
    public ProductPage addToCart() {
        scrollToElement(addToCartButton);
        addToCartButton.click();
        return this;
    }

    @Step("Check if 'Added to cart!' message is displayed")
    public boolean isAddedToCartMessageDisplayed() {
        return successMessage.shouldBe(Condition.visible, TIMEOUT).isDisplayed();
    }

    @Step("Check if Add to Cart button is displayed")
    public boolean isAddToCartButtonDisplayed() {
        return addToCartButton.isDisplayed();
    }
}
