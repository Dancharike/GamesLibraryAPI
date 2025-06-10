package lt.viko.eif.kladijev.steamapi.stepdefs;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Класс шагов для достижений.
 */
public class AchievementStepdefs extends BaseStepdefs
{
    @When("I request achievements of player with ID {string}")
    public void iRequestAchievementsOfPlayerWithID(String id)
    {
        response = getWithAuth("/admins/players/" + id + "/achievements");
    }

    @When("I request achievements of player with name {string}")
    public void iRequestAchievementsOfPlayerWithName(String name)
    {
        response = getWithAuth("/shared/players/name/" + name + "/achievements");
    }

    @When("I request my own achievements")
    public void iRequestMyOwnAchievements()
    {
        response = getWithAuth("/players/me/achievements");
    }

    @Then("I should receive a list of achievements")
    public void iShouldReceiveAListOfAchievements()
    {
        response.then().statusCode(200);
        System.out.println("Achievements: " + response.getBody().asString());
    }
}
