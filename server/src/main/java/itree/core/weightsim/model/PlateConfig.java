package itree.core.weightsim.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class PlateConfig
{
    @JsonProperty("name")
    private String plateConfigName;

    @JsonProperty("num")
    private int numPlates;

    @JsonProperty("version")
    private int plateVersion;

    @JsonProperty("port")
    private int startPort;

    public PlateConfig(String plateConfigName, int startPort, int numPlates, int plateVersion)
    {
        this.plateConfigName = plateConfigName;
        this.numPlates = numPlates;
        this.plateVersion = plateVersion;
        this.startPort = startPort;
    }

    public PlateConfig()
    {
    }

    public String getPlateConfigName()
    {
        return plateConfigName;
    }

    public int getNumPlates()
    {
        return numPlates;
    }

    public int getPlateVersion()
    {
        return plateVersion;
    }

    public int getStartPort()
    {
        return startPort;
    }
}
