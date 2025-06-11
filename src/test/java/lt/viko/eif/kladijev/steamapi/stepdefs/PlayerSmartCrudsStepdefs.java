package lt.viko.eif.kladijev.steamapi.stepdefs;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lt.viko.eif.kladijev.steamapi.context.TestContext;
import lt.viko.eif.kladijev.steamapi.dto.RegisterRequest;
import org.json.JSONException;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

/**
 * Класс шагов для умного управления аккаунтом игрока.
 */
public class PlayerSmartCrudsStepdefs extends BaseStepdefs
{
    @When("I register a new account with nickname {string} and email {string} and password {string}")
    public void iRegisterANewAccountWithNicknameAndEmailAndPassword(String nickname, String email, String password)
    {
        RegisterRequest request = new RegisterRequest();
        request.username = nickname;
        request.email = email;
        request.password = password;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post(baseUrl + "/players/register");

        response.then().statusCode(200);
        TestContext.setCurrentUser(nickname);
        TestContext.setCurrentPassword(password);
        TestContext.setResponse(response);
    }

    @Then("I should be able to log in as {string}")
    public void iShouldBeAbleToLogInAs(String username) throws InterruptedException
    {
        Thread.sleep(200);

        String password = TestContext.getCurrentPassword();

        if(password == null)
        {
            throw new IllegalStateException("Password is not set for user: " + username);
        }

        Response response = given()
                .auth().preemptive().basic(username, TestContext.getCurrentPassword())
                        .get(baseUrl + "/players/me");

        response.then().statusCode(200);
        TestContext.setResponse(response);
        System.out.println("Login successful for: " + username);
    }

    @When("I update my profile with nickname {string} and email {string}")
    public void iUpdateMyProfileWithNicknameAndEmail(String newNick, String newEmail) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("nickName", newNick);
        json.put("email", newEmail);

        Response response = given()
                .auth().preemptive().basic(TestContext.getCurrentUser(), TestContext.getCurrentPassword())
                .contentType(ContentType.JSON)
                .body(json.toString())
                .put(baseUrl + "/players/me/update");

        TestContext.setResponse(response);
    }

    @Then("I should see my updated profile with nickname {string} and email {string}")
    public void iShouldSeeMyUpdatedProfileWithNicknameAndEmail(String newNick, String newEmail)
    {
        Response response = TestContext.getResponse();
        response.then().statusCode(200);
        String body = response.getBody().asString();

        assert body.contains(newNick);
        assert body.contains(newEmail);

        System.out.println("Updated profile verified: " + body);
    }

    @When("I delete my own account")
    public void iDeleteMyOwnAccount()
    {
        Response response = given()
                .auth().preemptive().basic(TestContext.getCurrentUser(), TestContext.getCurrentPassword())
                .delete(baseUrl + "/players/me/delete");

        TestContext.setResponse(response);
    }

    @Then("I should not be able to log in as {string}")
    public void iShouldNotBeAbleToLogInAs(String username)
    {
        Response response = given()
                .auth().preemptive().basic(username, TestContext.getCurrentPassword())
                .get(baseUrl + "/players/me");

        response.then().statusCode(401);
        System.out.println("Login attempt failed as expected for: " + username);
    }
}
