package com.shop.steps;

import com.shop.helpers.TestDataHelper;
import com.shop.pages.LoginPage;
import com.shop.pages.RegisterPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSteps {

    private final TestContext context;
    private final LoginPage loginPage = new LoginPage();
    private final RegisterPage registerPage = new RegisterPage();

    public LoginSteps(TestContext context) {
        this.context = context;
    }

    // --- Login steps ---

    @When("the user opens the login page on mobile")
    public void theUserOpensTheLoginPageOnMobile() {
        loginPage.open();
    }

    @When("the user enters valid email and password")
    public void theUserEntersValidEmailAndPassword() {
        loginPage.enterEmail(context.getUserEmail());
        loginPage.enterPassword(context.getUserPassword());
    }

    @When("the user enters email {string} and password {string}")
    public void theUserEntersEmailAndPassword(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
    }

    @When("the user taps the Login button")
    public void theUserTapsTheLoginButton() {
        loginPage.clickLogin();
    }

    @Then("the user should be logged in successfully")
    public void theUserShouldBeLoggedInSuccessfully() {
        assertTrue(loginPage.isLoggedIn(), "User should be logged in (Logout button visible)");
    }

    @Then("the Logout button should be visible")
    public void theLogoutButtonShouldBeVisible() {
        assertTrue(loginPage.isLoggedIn(), "Logout button should be visible");
    }

    @Then("an error message should be displayed on login page")
    public void anErrorMessageShouldBeDisplayedOnLoginPage() {
        assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed");
    }

    // --- Register steps ---

    @When("the user opens the register page on mobile")
    public void theUserOpensTheRegisterPageOnMobile() {
        registerPage.open();
    }

    @When("the user enters a random name, email and password")
    public void theUserEntersARandomNameEmailAndPassword() {
        String name = TestDataHelper.randomName();
        String email = TestDataHelper.randomEmail();
        String password = TestDataHelper.randomPassword();

        context.setUserName(name);
        context.setUserEmail(email);
        context.setUserPassword(password);

        registerPage.enterName(name);
        registerPage.enterEmail(email);
        registerPage.enterPassword(password);
    }

    @When("the user taps the Register button")
    public void theUserTapsTheRegisterButton() {
        registerPage.clickRegister();
    }

    // --- Logout steps ---

    @When("the user taps the Logout button")
    public void theUserTapsTheLogoutButton() {
        loginPage.logout();
    }

    @Then("the Login link should be visible in navigation")
    public void theLoginLinkShouldBeVisibleInNavigation() {
        loginPage.waitForVisible(loginPage.loginLink);
        assertTrue(loginPage.loginLink.isDisplayed(), "Login link should be visible after logout");
    }
}
