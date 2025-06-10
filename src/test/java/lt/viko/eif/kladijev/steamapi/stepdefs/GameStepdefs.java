package lt.viko.eif.kladijev.steamapi.stepdefs;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import lt.viko.eif.kladijev.steamapi.context.TestContext;

/**
 * Класс шагов для игр.
 */
public class GameStepdefs extends BaseStepdefs
{
    @When("I request games of player with ID {string}")
    public void iRequestGamesOfPlayerWithID(String id)
    {
        TestContext.setResponse(getWithAuth("/admins/players/" + id + "/games"));
    }

    @When("I request games of player with name {string}")
    public void iRequestGamesOfPlayerWithName(String name)
    {
        TestContext.setResponse(getWithAuth("/shared/players/name/" + name + "/games"));
    }

    @When("I request my own games")
    public void iRequestMyOwnGames()
    {
        TestContext.setResponse(getWithAuth("/players/me/games"));
    }

    @Then("I should receive a list of games")
    public void iShouldReceiveAListOfGames()
    {
        Response response = TestContext.getResponse();

        response.then().statusCode(200);
        System.out.println("Games: " + response.getBody().asString());
    }
}
