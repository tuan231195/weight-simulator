package itree.core.weightsim.model;

public class VehicleState
{
    private int plate;
    private int step;
    private int port;
    private double weight;
    private boolean scaleActive;
    private boolean scaleJoin;

    public int getPlate()
    {
        return plate;
    }

    public void setPlate(int plate)
    {
        this.plate = plate;
    }

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

    public int getStep()
    {
        return step;
    }

    public void setStep(int step)
    {
        this.step = step;
    }
}
