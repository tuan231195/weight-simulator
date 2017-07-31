package itree.core.weightsim.controllers;

public class PlateNotFoundException extends Exception
{
    private int version;

    public PlateNotFoundException(int version)
    {
        super("Plate version " + version + " cannot be found");
        this.version = version;
    }

    public int getVersion()
    {
        return version;
    }
}
