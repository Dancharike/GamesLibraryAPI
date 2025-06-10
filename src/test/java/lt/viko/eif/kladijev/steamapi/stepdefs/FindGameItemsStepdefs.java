package lt.viko.eif.kladijev.steamapi.stepdefs;

import io.cucumber.java.en.When;
import lt.viko.eif.kladijev.steamapi.context.TestContext;

/**
 * Класс шагов для нахождения предметов, которые содержит игра.
 */
public class FindGameItemsStepdefs extends BaseStepdefs
{
    @When("I request items of game with ID {string}")
    public void iRequestItemsOfGameWithID(String id)
    {
        TestContext.setResponse(getWithAuth("/games/" + id + "/items"));
    }

    @When("I request items of game with name {string}")
    public void iRequestItemsOfGameWithName(String name)
    {
        TestContext.setResponse(getWithAuth("/games/name/" + name + "/items"));
    }
}
