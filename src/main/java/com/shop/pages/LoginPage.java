package com.shop.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BaseMobilePage {

    private final SelenideElement pageTitle = $(By.xpath("//h1[text()='Login']"));
    private final SelenideElement emailInput = $("input[name='email']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement loginButton = $(By.xpath("//button[text()='Login']"));
    private final SelenideElement errorMessage = $("p.text-red-600.text-sm");
    private final SelenideElement registerLink = $(By.xpath("//a[text()='Register']"));

    @Step("Open login page")
    public LoginPage open() {
        navigateTo(config.baseUrl() + "/login");
        pageTitle.shouldBe(Condition.visible, TIMEOUT);
        return this;
    }

    @Step("Enter email: {email}")
    public LoginPage enterEmail(String email) {
        emailInput.clear();
        emailInput.sendKeys(email);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
        return this;
    }

    @Step("Click Login button")
    public LoginPage clickLogin() {
        loginButton.click();
        return this;
    }

    @Step("Login with credentials: {email}")
    public void loginAs(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    @Step("Check if error message is displayed")
    public boolean isErrorDisplayed() {
        return errorMessage.shouldBe(Condition.visible, TIMEOUT).isDisplayed();
    }

    @Step("Get error message text")
    public String getErrorMessage() {
        return errorMessage.shouldBe(Condition.visible, TIMEOUT).getText();
    }

    @Step("Click Register link")
    public void clickRegisterLink() {
        registerLink.click();
    }

    @Step("Check if login page is displayed")
    public boolean isPageDisplayed() {
        return pageTitle.isDisplayed();
    }
}
