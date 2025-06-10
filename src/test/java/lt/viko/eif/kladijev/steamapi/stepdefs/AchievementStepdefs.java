package lt.viko.eif.kladijev.steamapi.stepdefs;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lt.viko.eif.kladijev.steamapi.context.TestContext;

/**
 * Класс шагов для достижений.
 */
public class AchievementStepdefs extends BaseStepdefs
{
    @When("I request achievements of player with ID {string}")
    public void iRequestAchievementsOfPlayerWithID(String id)
    {
        TestContext.setResponse(getWithAuth("/admins/players/" + id + "/achievements"));
    }

    @When("I request achievements of player with name {string}")
    public void iRequestAchievementsOfPlayerWithName(String name)
    {
        TestContext.setResponse(getWithAuth("/shared/players/name/" + name + "/achievements"));
    }

    @When("I request my own achievements")
    public void iRequestMyOwnAchievements()
    {
        TestContext.setResponse(getWithAuth("/players/me/achievements"));
    }

    @Then("I should receive a list of achievements")
    public void iShouldReceiveAListOfAchievements()
    {
        Response response = TestContext.getResponse();

        response.then().statusCode(200);
        System.out.println("Achievements: " + response.getBody().asString());
    }
}
