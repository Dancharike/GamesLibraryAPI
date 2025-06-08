package lt.viko.eif.kladijev.steamapi.context;

/**
 * Класс общего контекста для хранения данных между step классами.
 */
public class TestContext
{
    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();

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
