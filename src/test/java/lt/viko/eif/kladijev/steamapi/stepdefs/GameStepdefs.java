package lt.viko.eif.kladijev.steamapi.stepdefs;

import io.cucumber.java.en.*;

/**
 * Класс шагов для игр.
 */
public class GameStepdefs extends BaseStepdefs
{
    @When("I request games of player with ID {string}")
    public void iRequestGamesOfPlayerWithID(String id)
    {
        response = getWithAuth("/admins/players/" + id + "/games");
    }

    @When("I request games of player with name {string}")
    public void iRequestGamesOfPlayerWithName(String name)
    {
        response = getWithAuth("/admins/players/name/" + name + "/games");
    }

    @When("I request my own games")
    public void iRequestMyOwnGames()
    {
        response = getWithAuth("/players/me/games");
    }

    @Then("I should receive a list of games")
    public void iShouldReceiveAListOfGames()
    {
        response.then().statusCode(200);
        System.out.println("Games: " + response.getBody().asString());
    }
}
