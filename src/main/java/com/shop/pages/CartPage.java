package com.shop.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage extends BaseMobilePage {

    public final SelenideElement pageTitle = $(By.xpath("//h1[text()='Shopping Cart']"));
    private final SelenideElement emptyCartMessage = $(By.xpath("//p[text()='Your cart is empty.']"));
    private final ElementsCollection cartItems = $$("div.bg-white.p-4.rounded-lg.shadow");
    private final SelenideElement clearCartButton = $(By.xpath("//button[text()='Clear Cart']"));
    private final SelenideElement totalPrice = $("p.text-2xl.font-bold");
    private final SelenideElement checkoutButton = $(By.xpath("//button[text()='Checkout']"));

    @Step("Open cart page")
    public CartPage open() {
        navigateTo(config.baseUrl() + "/cart");
        pageTitle.shouldBe(Condition.visible, TIMEOUT);
        return this;
    }

    @Step("Check if cart is empty")
    public boolean isCartEmpty() {
        return emptyCartMessage.isDisplayed();
    }

    @Step("Get empty cart message text")
    public String getEmptyCartMessage() {
        return emptyCartMessage.shouldBe(Condition.visible, TIMEOUT).getText();
    }

    @Step("Get number of items in cart")
    public int getCartItemCount() {
        return cartItems.size();
    }

    @Step("Get item name at index: {index}")
    public String getItemName(int index) {
        return cartItems.get(index).$("h3").getText();
    }

    @Step("Get item price at index: {index}")
    public String getItemPrice(int index) {
        return cartItems.get(index).$("p.text-gray-500").getText();
    }

    @Step("Get item quantity at index: {index}")
    public String getItemQuantity(int index) {
        return cartItems.get(index).$("span.w-8.text-center").getText();
    }

    @Step("Increase quantity for item at index: {index}")
    public CartPage increaseQuantity(int index) {
        cartItems.get(index).$(By.xpath(".//button[text()='+']")).click();
        return this;
    }

    @Step("Decrease quantity for item at index: {index}")
    public CartPage decreaseQuantity(int index) {
        cartItems.get(index).$(By.xpath(".//button[text()='-']")).click();
        return this;
    }

    @Step("Remove item at index: {index}")
    public CartPage removeItem(int index) {
        cartItems.get(index).$(By.xpath(".//button[text()='Remove']")).click();
        return this;
    }

    @Step("Clear entire cart")
    public CartPage clearCart() {
        clearCartButton.click();
        return this;
    }

    @Step("Get total price text")
    public String getTotalPrice() {
        return totalPrice.shouldBe(Condition.visible, TIMEOUT).getText();
    }

    @Step("Click Checkout button")
    public void checkout() {
        scrollToElement(checkoutButton);
        checkoutButton.click();
    }

    @Step("Check if Checkout button is displayed")
    public boolean isCheckoutButtonDisplayed() {
        return checkoutButton.isDisplayed();
    }
}
