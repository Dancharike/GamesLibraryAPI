package lt.viko.eif.kladijev.steamapi.stepdefs;

import io.restassured.response.Response;
import lt.viko.eif.kladijev.steamapi.context.TestContext;

import static io.restassured.RestAssured.given;

/**
 * Абстрактный базовый класс, предоставляющий общие переменные и методы для всех stepdefs.
 */
public abstract class BaseStepdefs
{
    protected final String baseUrl = "http://localhost:8080/api";
    protected Response response;

    protected Response getWithAuth(String path)
    {
        return given()
                .auth().preemptive().basic(TestContext.getCurrentUser(), "password")
                .get(baseUrl + path);
    }
}
