@regression
Feature: Shopping Cart on Mobile
  As a logged-in mobile user
  I want to manage my shopping cart
  So that I can prepare items for checkout

  Background:
    Given a registered user exists
    And the user is logged in on mobile
    And the cart is empty via API

  @smoke
  Scenario: Add product to cart from product page
    Given the user opens the first product page on mobile
    When the user taps the Add to Cart button
    Then the "Added to cart!" success message should appear
    And the cart should contain 1 item via API

  Scenario: Update quantity in cart
    Given a product is added to the cart via API
    And the user opens the cart page on mobile
    When the user taps the increase quantity button for the first item
    Then the item quantity should be updated

  Scenario: Remove item from cart
    Given a product is added to the cart via API
    And the user opens the cart page on mobile
    When the user taps the Remove button for the first item
    Then the cart should be empty on the page

  Scenario: Clear entire cart
    Given 2 products are added to the cart via API
    And the user opens the cart page on mobile
    When the user taps the Clear Cart button
    Then the cart should be empty on the page

  Scenario: Cart persists after page refresh
    Given a product is added to the cart via API
    And the user opens the cart page on mobile
    When the user refreshes the page
    Then the cart should still contain 1 item on the page
