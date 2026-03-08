package com.shop.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.shop.config.AppConfig;
import com.shop.config.DriverFactory;
import com.shop.helpers.GestureHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public abstract class BaseMobilePage {

    protected static final AppConfig config = AppConfig.getInstance();
    protected static final Duration TIMEOUT = Duration.ofSeconds(
            AppConfig.getInstance().explicitTimeout()
    );

    // --- Navigation elements ---
    public final SelenideElement productsLink = $(By.linkText("Products"));
    public final SelenideElement cartLink = $(By.linkText("Cart"));
    public final SelenideElement ordersLink = $(By.linkText("Orders"));
    public final SelenideElement loginLink = $(By.linkText("Login"));
    public final SelenideElement registerLink = $(By.linkText("Register"));
    public final SelenideElement logoutButton = $(By.xpath("//button[text()='Logout']"));
    public final SelenideElement userNameSpan = $("span.text-sm.text-gray-500");

    // --- Common actions ---

    @Step("Navigate to URL: {url}")
    public void navigateTo(String url) {
        DriverFactory.getDriver().get(url);
    }

    @Step("Open home page")
    public void openHomePage() {
        navigateTo(config.baseUrl());
    }

    @Step("Wait for element to be visible")
    public SelenideElement waitForVisible(SelenideElement element) {
        return element.shouldBe(Condition.visible, TIMEOUT);
    }

    @Step("Wait for element to contain text: {text}")
    public SelenideElement waitForText(SelenideElement element, String text) {
        return element.shouldHave(Condition.text(text), TIMEOUT);
    }

    @Step("Scroll to element")
    public void scrollToElement(SelenideElement element) {
        int maxAttempts = 5;
        int attempt = 0;
        while (!element.isDisplayed() && attempt < maxAttempts) {
            GestureHelper.swipeUp();
            attempt++;
        }
    }

    @Step("Swipe up on page")
    public void swipeUp() {
        GestureHelper.swipeUp();
    }

    @Step("Swipe down on page")
    public void swipeDown() {
        GestureHelper.swipeDown();
    }

    @Step("Take screenshot")
    public byte[] takeScreenshot() {
        return ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    // --- Navigation shortcuts ---

    @Step("Navigate to Products page")
    public void goToProducts() {
        productsLink.click();
    }

    @Step("Navigate to Cart page")
    public void goToCart() {
        cartLink.click();
    }

    @Step("Navigate to Orders page")
    public void goToOrders() {
        ordersLink.click();
    }

    @Step("Navigate to Login page")
    public void goToLogin() {
        loginLink.click();
    }

    @Step("Click Logout")
    public void logout() {
        logoutButton.click();
    }

    @Step("Check if user is logged in")
    public boolean isLoggedIn() {
        return logoutButton.isDisplayed();
    }

    @Step("Get logged in user name")
    public String getLoggedInUserName() {
        return userNameSpan.shouldBe(Condition.visible, TIMEOUT).getText();
    }
}
