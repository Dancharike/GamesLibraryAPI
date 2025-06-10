package lt.viko.eif.kladijev.steamapi.stepdefs;

import io.cucumber.java.en.When;

/**
 * Класс шагов для нахождения достижений, которые содержит игра.
 */
public class FindGameAchievementsStepdefs extends BaseStepdefs
{
    @When("I request achievements of game with ID {string}")
    public void iRequestAchievementsOfGameWithID(String id)
    {
        response = getWithAuth("/games/" + id + "/achievements");
    }

    @When("I request achievements of game with name {string}")
    public void iRequestAchievementsOfGameWithName(String name)
    {
        response = getWithAuth("/games/name/" + name + "/achievements");
    }
}
