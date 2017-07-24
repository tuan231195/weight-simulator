package itree.core.weightsim.model;

public class SimState
{
    private PlateState[] plateStates;
    private double currentVehiclePosition;

    public PlateState[] getPlateStates()
    {
        return plateStates;
    }

    public double getCurrentVehiclePosition()
    {
        return currentVehiclePosition;
    }
}
