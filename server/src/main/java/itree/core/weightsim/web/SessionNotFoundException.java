package itree.core.weightsim.controllers;

public class SessionNotFoundException extends Exception
{
    private String session;
    public SessionNotFoundException(String session)
    {
        super("Session " + session + " cannot be found");
        this.session = session;
    }

    public String getSession()
    {
        return session;
    }
}
