Feature: Get view of Items stored in Game entity

  Background:
    Given the API is running

  Scenario: Admin view items by game's ID
    Given I am logged in as "admin1"
    When I request items of game with ID "1"
    Then I should receive a list of items

  Scenario: Admin view items by game's name
    Given I am logged in as "admin1"
    When I request items of game with name "Half-Life"
    Then I should receive a list of items

  Scenario: Player view items by game's name
    Given I am logged in as "playerOne"
    When I request items of game with name "Half-Life"
    Then I should receive a list of items