package itree.core.weightsim.model;


public class StageState
{
    private double [] values;
    private double vehiclePosition;

    public StageState(double[] values, double vehiclePosition)
    {
        this.values = values;
        this.vehiclePosition = vehiclePosition;
    }

    public double[] getValues()
    {
        return values;
    }

    public double getVehiclePosition()
    {
        return vehiclePosition;
    }
}
