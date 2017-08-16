package itree.core.weightsim.service.sim;

import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.*;
import itree.core.weightsim.service.sim.stages.Stage;
import itree.core.weightsim.service.sim.stages.Stage1;
import itree.core.weightsim.service.sim.stages.Stage2;
import itree.core.weightsim.service.sim.stages.Stage3;
import itree.core.weightsim.util.LoggerWrapper;
import itree.core.weightsim.util.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimThread implements Runnable
{
    private static final long DEFAULT_TIME_OUT = 7000;
    private String code;
    private PlateConfig plateConfig;
    private boolean isRunning;
    private int currentStep;
    private int currentStage;
    private boolean init;
    private LoggerWrapper logger = LoggerWrapperFactory.getLogger(getClass());
    private List<WeightConfig> weightConfigList;
    private List<Stage> stages;
    private ScheduledExecutorService scheduledExecutorService;
    private SimConfig simConfig;
    private StageState currentStateValues = new StageState(new double[]{0.0, 0.0, 0.0, 0.0, 0.0}, 0.0);
    private static final double TOTAL_LENGTH = 1000;
    private long lastActiveTime;
    private String sessionId;

    SimThread(String sessionId, PlateConfig plateConfig, String code, SimConfig simConfig, List<WeightConfig> weightConfigList, ScheduledExecutorService scheduledExecutorService)
    {
        this.sessionId = sessionId;
        this.weightConfigList = weightConfigList;
        this.plateConfig = plateConfig;
        this.code = code;
        this.scheduledExecutorService = scheduledExecutorService;
        this.simConfig = simConfig;
    }

    void setStages(List<Stage> stages)
    {
        this.stages = stages;
    }

    @Override
    public synchronized void run()
    {
        try
        {
            if (!init)
            {
                isRunning = true;
                logger.debug("Initializing Sim Thread. Session Id: " + sessionId);
                init = true;
                lastActiveTime = System.currentTimeMillis();
            }
            if (!isRunning)
            {
                return;
            }
            if (System.currentTimeMillis() - lastActiveTime > DEFAULT_TIME_OUT)
            {
                logger.info("Thread timeout. Session Id: " + sessionId);
                stop();
                return;
            }
            Stage stage = stages.get(currentStage);
            this.currentStateValues = stage.run(weightConfigList.get(currentStep));
            scheduledExecutorService.schedule(this, simConfig.getTickRates(), TimeUnit.SECONDS);
        }
        catch (Exception e)
        {
            logger.error("Internal error", e);
        }
    }

    public void nextStage()
    {
        Stage stage = stages.get(currentStage);
        logger.debug(String.format("SessionId %s: Current Stage: %d", sessionId, currentStage));
        if (stage instanceof Stage1)
        {
            Stage2 stage2 = (Stage2) stages.get(currentStage + 1);
            stage2.setStabled(true);
        }
        else if (stage instanceof Stage2)
        {
            Stage2 stage2 = (Stage2) stage;
            stage2.setStabled(false);
        }
        else if (stage instanceof Stage3)
        {
            currentStep++;
            if (currentStep >= this.weightConfigList.size())
            {
                stop();
                return;
            }
        }
        this.currentStage = (currentStage + 1) % 4;
        logger.debug(String.format("SessionId %s: Moved to next stage: %d", sessionId, currentStage));
    }

    void nextStep()
    {
        logger.debug(String.format("SessionId %s: Continue to next step: %d", sessionId, currentStep + 1));
        Stage stage = stages.get(currentStage);
        if (stage instanceof Stage2)
        {
            nextStage();
        }
    }

    synchronized boolean isStopped()
    {
        return init && !isRunning;
    }

    int getCurrentStep()
    {
        return currentStep;
    }

    public PlateConfig getPlateConfig()
    {
        return plateConfig;
    }

    public String getCode()
    {
        return code;
    }

    SimState getState()
    {
        if (weightConfigList == null)
        {
            return null;
        }
        lastActiveTime = System.currentTimeMillis();
        SimState simState = new SimState();
        simState.setCurrentVehiclePosition(0);
        simState.setPlate(plateConfig.getNumPlates());
        simState.setStep(currentStep);
        simState.setStage(currentStage);
        simState.setNumSteps(weightConfigList.size());
        simState.setStopped(isStopped());
        VehicleState[] vehicleStates = new VehicleState[plateConfig.getNumPlates()];
        double[] values = currentStateValues.getValues();


        if (currentStep != weightConfigList.size())
        {
            WeightConfig weightConfig = weightConfigList.get(currentStep);
            simState.setCurrentVehiclePosition(getVehiclePosition());
            for (int i = 0; i < vehicleStates.length; i++)
            {
                vehicleStates[i] = new VehicleState();
                vehicleStates[i].setPort(simConfig.getStartPort() + i);
                vehicleStates[i].setScaleActive(weightConfig.getScaleActive()[i]);
                if (i < vehicleStates.length - 1)
                {
                    vehicleStates[i].setScaleJoin(weightConfig.getScaleActive()[i]);
                }
                else
                {
                    vehicleStates[i].setScaleJoin(false);
                }
                vehicleStates[i].setWeight(values[i] / 1000);
            }
        }
        else
        {
            for (int i = 0; i < vehicleStates.length; i++)
            {
                vehicleStates[i] = new VehicleState();
                vehicleStates[i].setPort(simConfig.getStartPort() + i);
                vehicleStates[i].setScaleActive(false);
                vehicleStates[i].setScaleJoin(false);
                vehicleStates[i].setWeight(values[i] / 1000);
            }
            simState.setCurrentVehiclePosition(TOTAL_LENGTH);
        }
        simState.setVehicleStates(vehicleStates);
        return simState;
    }

    public SimConfig getSimConfig()
    {
        return simConfig;
    }

    public synchronized double getVehiclePosition()
    {
        double lengthPerStep = TOTAL_LENGTH / weightConfigList.size();
        double curOffset = currentStep * lengthPerStep;
        return curOffset + lengthPerStep * currentStateValues.getVehiclePosition();
    }

    public synchronized void stop()
    {
        logger.debug("Stopping Sim Thread");
        isRunning = false;
    }
}
