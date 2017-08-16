package itree.core.weightsim.model;

public class SimState
{
    private VehicleState[] vehicleStates;
    private double currentVehiclePosition;
    private int plate;
    private int step;
    private int numSteps;
    private int stage;
    private boolean isStopped;

    public boolean isStopped()
    {
        return isStopped;
    }

    public void setStopped(boolean stopped)
    {
        isStopped = stopped;
    }

    public int getStage()
    {
        return stage;
    }

    public void setStage(int stage)
    {
        this.stage = stage;
    }

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

    public int getPlate()
    {
        return plate;
    }

    public int getStep()
    {
        return step;
    }

    public void setStep(int step)
    {
        this.step = step;
    }

    public void setPlate(int plate)
    {
        this.plate = plate;
    }

    public int getNumSteps()
    {
        return numSteps;
    }

    public void setNumSteps(int numSteps)
    {
        this.numSteps = numSteps;
    }
}
