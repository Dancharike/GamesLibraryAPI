package lt.viko.eif.kladijev.steamapi.stepdefs;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Класс шагов для достижений.
 */
public class AchievementStepdefs extends BaseStepdefs
{
    @When("I request achievements of player with ID {string}")
    public void iRequestAchievementsOfPlayerWithID(String arg0) {
    }

    @Then("I should receive a list of achievements")
    public void iShouldReceiveAListOfAchievements() {
    }

    @When("I request achievements of player with name {string}")
    public void iRequestAchievementsOfPlayerWithName(String arg0) {
    }

    @When("I request my own achievements")
    public void iRequestMyOwnAchievements() {
    }
}
