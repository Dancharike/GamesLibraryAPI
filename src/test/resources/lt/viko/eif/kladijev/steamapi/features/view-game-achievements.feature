Feature: Get view of Achievements stored in Game entity

  Background:
    Given the API is running

  Scenario: Admin view achievements by game's ID
    Given I am logged in as "admin1"
    When I request achievements of game with ID "1"
    Then I should receive a list of achievements

  Scenario: Admin view achievements by game's name
    Given I am logged in as "admin1"
    When I request achievements of game with name "Half-Life"
    Then I should receive a list of achievements

  Scenario: Player view achievements by game's name
    Given I am logged in as "playerOne"
    When I request achievements of game with name "Half-Life"
    Then I should receive a list of achievements