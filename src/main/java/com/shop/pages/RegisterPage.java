package com.shop.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BaseMobilePage {

    private final SelenideElement pageTitle = $(By.xpath("//h1[text()='Register']"));
    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement emailInput = $("input[name='email']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement registerButton = $(By.xpath("//button[text()='Register']"));
    private final SelenideElement errorMessage = $("p.text-red-600.text-sm");
    private final SelenideElement loginLink = $(By.xpath("//a[text()='Login']"));

    @Step("Open register page")
    public RegisterPage open() {
        navigateTo(config.baseUrl() + "/register");
        pageTitle.shouldBe(Condition.visible, TIMEOUT);
        return this;
    }

    @Step("Enter name: {name}")
    public RegisterPage enterName(String name) {
        nameInput.clear();
        nameInput.sendKeys(name);
        return this;
    }

    @Step("Enter email: {email}")
    public RegisterPage enterEmail(String email) {
        emailInput.clear();
        emailInput.sendKeys(email);
        return this;
    }

    @Step("Enter password")
    public RegisterPage enterPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
        return this;
    }

    @Step("Click Register button")
    public RegisterPage clickRegister() {
        registerButton.click();
        return this;
    }

    @Step("Register new user: {name}, {email}")
    public void registerAs(String name, String email, String password) {
        enterName(name);
        enterEmail(email);
        enterPassword(password);
        clickRegister();
    }

    @Step("Check if error message is displayed")
    public boolean isErrorDisplayed() {
        return errorMessage.shouldBe(Condition.visible, TIMEOUT).isDisplayed();
    }

    @Step("Get error message text")
    public String getErrorMessage() {
        return errorMessage.shouldBe(Condition.visible, TIMEOUT).getText();
    }

    @Step("Click Login link")
    public void clickLoginLink() {
        loginLink.click();
    }

    @Step("Check if register page is displayed")
    public boolean isPageDisplayed() {
        return pageTitle.isDisplayed();
    }
}
