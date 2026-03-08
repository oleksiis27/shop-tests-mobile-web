@regression
Feature: Checkout on Mobile
  As a logged-in mobile user
  I want to complete the checkout process
  So that I can place orders from my mobile device

  Background:
    Given a registered user exists
    And the user is logged in on mobile

  @smoke
  Scenario: Complete checkout flow on mobile
    Given a product is added to the cart via API
    And the user opens the cart page on mobile
    When the user taps the Checkout button
    Then the user should be redirected to the orders page
    And at least one order should be displayed

  Scenario: Checkout with empty cart shows appropriate message
    Given the cart is empty via API
    And the user opens the cart page on mobile
    Then the empty cart message "Your cart is empty." should be displayed
    And the Checkout button should not be visible

  Scenario: Verify order appears in order history after checkout
    Given a product is added to the cart via API
    And the user completes checkout via API
    When the user opens the orders page on mobile
    Then at least one order should be displayed
    And the latest order should have status "pending"
