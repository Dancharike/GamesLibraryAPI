package lt.viko.eif.kladijev.steamapi.stepdefs;

import io.cucumber.java.en.*;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class GameStepdefs
{
    private final String baseUrl = "http://localhost:8080";
    private Response response;
    private String currentUser;

    @Given("the API is running")
    public void theAPIIsRunning()
    {
        System.out.println("Assuming the API is running at: " + baseUrl);
    }

    @Given("I am logged in as {string}")
    public void iAmLoggedInAs(String role)
    {
        this.currentUser = role;
        System.out.println("Logged in as: " + role);
    }

    @When("I request games of player with ID {string}")
    public void iRequestGamesOfPlayerWithID(String id)
    {
        response = given()
                .auth().preemptive().basic(currentUser, "password")
                .get(baseUrl + "/players/" + id + "/games");
    }

    @When("I request games of player with name {string}")
    public void iRequestGamesOfPlayerWithName(String name)
    {
        response = given()
                .auth().preemptive().basic(currentUser, "password")
                .get(baseUrl + "/players/name/" + name + "/games");
    }

    @When("I request my own games")
    public void iRequestMyOwnGames()
    {
        response = given()
                .auth().preemptive().basic(currentUser, "password")
                .get(baseUrl + "/players/me/games");
    }

    @Then("I should receive a list of games")
    public void iShouldReceiveAListOfGames()
    {
        response.then().statusCode(200);
        System.out.println("Games: " + response.getBody().asString());
    }
}
