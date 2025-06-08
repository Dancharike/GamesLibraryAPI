Feature: Access Items by Role

  Background:
    Given the API is running

  Scenario: Admin views items of player by ID
    Given I am logged in as "admin1"
    When I request items of player with ID "1"
    Then I should receive a list of items

  Scenario: Admin views items of player by name
    Given I am logged in as "admin1"
    When I request items of player with name "playerOne"
    Then I should receive a list of items

  Scenario: Player views their own items
    Given I am logged in as "playerOne"
    When I request my own items
    Then I should receive a list of items

  Scenario: Player views items of another player by name
    Given I am logged in as "playerOne"
    When I request items of player with name "playerTwo"
    Then I should receive a list of items
