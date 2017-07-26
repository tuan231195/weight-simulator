package itree.core.weightsim.model;


import java.util.List;

public class PlateConfig
{
    private String plateConfigName;
    private int numPlates;
    private int plateVersion;
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
