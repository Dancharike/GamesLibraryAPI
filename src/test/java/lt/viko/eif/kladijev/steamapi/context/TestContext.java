package lt.viko.eif.kladijev.steamapi.context;

import io.restassured.response.Response;

/**
 * Класс общего контекста для хранения данных между step классами.
 */
public class TestContext
{
    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();
    private static Response response;

    public static Response getResponse() {
        return response;
    }

    public static void setResponse(Response response) {
        TestContext.response = response;
    }

    public static void setCurrentUser(String user)
    {
        currentUser.set(user);
    }

    public static String getCurrentUser()
    {
        return currentUser.get();
    }

    public static void clear()
    {
        currentUser.remove();
    }
}
