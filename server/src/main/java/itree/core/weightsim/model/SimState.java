package itree.core.weightsim.model;

public class SimState
{
    private VehicleState[] vehicleStates;
    private double currentVehiclePosition;

    public VehicleState[] getVehicleStates()
    {
        return vehicleStates;
    }

    public double getCurrentVehiclePosition()
    {
        return currentVehiclePosition;
    }

    public void setVehicleStates(VehicleState[] vehicleStates)
    {
        this.vehicleStates = vehicleStates;
    }

    public void setCurrentVehiclePosition(double currentVehiclePosition)
    {
        this.currentVehiclePosition = currentVehiclePosition;
    }
}
