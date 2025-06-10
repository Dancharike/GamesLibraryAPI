package lt.viko.eif.kladijev.steamapi.stepdefs;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Класс шагов для предметов.
 */
public class ItemStepdefs extends BaseStepdefs
{
    @When("I request items of player with ID {string}")
    public void iRequestItemsOfPlayerWithID(String id)
    {
        response = getWithAuth("/admins/players/" + id + "/items");
    }

    @When("I request items of player with name {string}")
    public void iRequestItemsOfPlayerWithName(String name)
    {
        response = getWithAuth("/shared/players/name/" + name + "/items");
    }

    @When("I request my own items")
    public void iRequestMyOwnItems()
    {
        response = getWithAuth("/players/me/items");
    }

    @Then("I should receive a list of items")
    public void iShouldReceiveAListOfItems()
    {
        response.then().statusCode(200);
        System.out.println("Items: " + response.getBody().asString());
    }
}
