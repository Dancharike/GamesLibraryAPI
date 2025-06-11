Feature: Player account management

  Background:
    Given the API is running

  Scenario: Player updates their nickname and email
    Given I am logged in as "playerOne"
    When I update my profile with nickname "CoolGamer" and email "coolgamer@example.com"
    Then I should see my updated profile with nickname "CoolGamer" and email "coolgamer@example.com"

  Scenario: Player deletes their own account
    Given I am logged in as "playerToDelete"
    When I delete my own account
    Then I should not be able to log in as "playerToDelete"