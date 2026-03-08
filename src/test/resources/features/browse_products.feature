@regression
Feature: Browse Products on Mobile
  As a mobile user
  I want to browse and search products
  So that I can find items I want to purchase

  @smoke
  Scenario: View product catalog on mobile
    When the user opens the home page on mobile
    Then the product catalog should be displayed
    And at least one product card should be visible

  Scenario: Search products by name
    Given the user is on the home page on mobile
    When the user searches for an existing product name
    Then the search results should contain the searched product

  Scenario: Filter products by category
    Given the user is on the home page on mobile
    When the user selects a category from the filter dropdown
    Then only products from the selected category should be displayed

  @smoke
  Scenario: View product details page
    Given the user is on the home page on mobile
    When the user taps on the first product card
    Then the product details page should be displayed
    And the product title should be visible
    And the product price should be visible

  Scenario: Scroll through product list
    Given the user is on the home page on mobile
    When the user swipes up to scroll the product list
    Then the page should scroll and display more content
