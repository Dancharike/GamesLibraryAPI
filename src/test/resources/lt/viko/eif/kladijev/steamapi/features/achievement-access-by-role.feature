Feature: Access Achievements by Role

  Background:
    Given the API is running

  Scenario: Admin views achievements of player by ID
    Given I am logged in as "admin1"
    When I request achievements of player with ID "1"
    Then I should receive a list of achievements

  Scenario: Admin views achievements of player by name
    Given I am logged in as "admin1"
    When I request achievements of player with name "playerOne"
    Then I should receive a list of achievements

  Scenario: Player views their own achievements
    Given I am logged in as "playerOne"
    When I request my own achievements
    Then I should receive a list of achievements

  Scenario: Player views achievements of another player by name
    Given I am logged in as "playerOne"
    When I request achievements of player with name "playerTwo"
    Then I should receive a list of achievements