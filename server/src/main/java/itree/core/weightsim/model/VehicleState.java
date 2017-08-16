package itree.core.weightsim.model;

public class VehicleState
{
    private int port;
    private double weight;
    private boolean scaleActive;
    private boolean scaleJoin;

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public double getWeight()
    {
        return weight;
    }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    public boolean isScaleActive()
    {
        return scaleActive;
    }

    public void setScaleActive(boolean scaleActive)
    {
        this.scaleActive = scaleActive;
    }

    public boolean isScaleJoin()
    {
        return scaleJoin;
    }

    public void setScaleJoin(boolean scaleJoin)
    {
        this.scaleJoin = scaleJoin;
    }
}
