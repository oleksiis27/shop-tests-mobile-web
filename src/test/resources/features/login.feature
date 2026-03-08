@regression
Feature: User Authentication on Mobile
  As a mobile user
  I want to login and register on the shop
  So that I can access my account and make purchases

  @smoke
  Scenario: Successful login with valid credentials
    Given a registered user exists
    When the user opens the login page on mobile
    And the user enters valid email and password
    And the user taps the Login button
    Then the user should be logged in successfully
    And the Logout button should be visible

  Scenario: Login with invalid credentials shows error
    When the user opens the login page on mobile
    And the user enters email "wrong@test.com" and password "wrongpass"
    And the user taps the Login button
    Then an error message should be displayed on login page

  @smoke
  Scenario: Register a new user on mobile
    When the user opens the register page on mobile
    And the user enters a random name, email and password
    And the user taps the Register button
    Then the user should be logged in successfully
    And the Logout button should be visible

  Scenario: Logout from mobile
    Given a registered user exists
    And the user is logged in on mobile
    When the user taps the Logout button
    Then the Login link should be visible in navigation
