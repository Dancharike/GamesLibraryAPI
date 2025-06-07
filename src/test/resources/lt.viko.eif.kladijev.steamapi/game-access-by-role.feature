Feature: Access Games by Role

  Background:
    Given the API is running

  Scenario: Admin views games of player by ID
    Given I am logged in as "admin"
    When I request games of player with ID "1"
    Then I should receive a list of games

  Scenario: Admin views games of player by name
    Given I am logged in as "admin"
    When I request games of player with name "playerOne"
    Then I should receive a list of games

  Scenario: Player views their own games
    Given I am logged in as "playerOne"
    When I request my own games
    Then I should receive a list of games

  Scenario: Player views games of another player by name
    Given I am logged in as "playerOne"
    When I request games of player with name "playerTwo"
    Then I should receive a list of games