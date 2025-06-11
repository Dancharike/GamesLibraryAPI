package lt.viko.eif.kladijev.steamapi.stepdefs;

import io.cucumber.java.en.Given;
import lt.viko.eif.kladijev.steamapi.context.TestContext;

/**
 * Класс общих шагов, что используются во всех классах с расширением файла .feature.
 */
public class CommonForEachStepdefs extends BaseStepdefs
{
    @Given("the API is running")
    public void theAPIIsRunning()
    {
        System.out.println("Assuming the API is running at: " + baseUrl);
    }
    /*
    @Given("I am logged in as {string}")
    public void iAmLoggedInAs(String role)
    {
        TestContext.setCurrentUser(role);
        System.out.println("Logged in as: " + role);
    }
    */
    @Given("I am logged in as {string}")
    public void iAmLoggedInAs(String username)
    {
        TestContext.setCurrentUser(username);

        switch(username)
        {
            case "playerOne":
                TestContext.setCurrentPassword("password");
                break;

            case "playerTwo":
                TestContext.setCurrentPassword("password");
                break;

            case "playerToDelete":
                TestContext.setCurrentPassword("password");
                break;

            case "admin1":
                TestContext.setCurrentPassword("password");
                break;

            default:
                throw new IllegalArgumentException("Unknown test user: " + username);
        }
        System.out.println("Logged in as: " + username);
    }
}
