package itree.core.weightsim.model;


public class SimRequest
{
    private int version;
    private int numPlate;
    private String sessionId;
    private String code;

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public int getNumPlate()
    {
        return numPlate;
    }

    public void setNumPlate(int numPlate)
    {
        this.numPlate = numPlate;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    @Override
    public String toString()
    {
        return "SimRequest{" +
                "version=" + version +
                ", numPlate=" + numPlate +
                ", sessionId='" + sessionId + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
