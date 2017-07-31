package itree.core.weightsim.service.sim;

import itree.core.weightsim.jpa.dao.WeightConfigDao;
import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.*;
import itree.core.weightsim.service.sim.stages.Stage;
import itree.core.weightsim.service.sim.stages.Stage1;
import itree.core.weightsim.service.sim.stages.Stage2;
import itree.core.weightsim.service.sim.stages.Stage3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimThread implements Runnable
{
    private String code;
    private PlateConfig plateConfig;
    private boolean isRunning;
    private int currentStep;
    private int currentStage;
    private WeightConfigDao weightConfigDao;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private List<WeightConfig> weightConfigList;
    private List<Stage> stages;
    private ScheduledExecutorService scheduledExecutorService;
    private SimConfig simConfig;
    private SimService simService;
    private String sessionId;
    private StageState currentStateValues;
    private static final double TOTAL_LENGTH = 1000;

    SimThread(PlateConfig plateConfig, String code, SimService simService, SimConfig simConfig, WeightConfigDao weightConfigDao, ScheduledExecutorService scheduledExecutorService)
    {
        this.weightConfigDao = weightConfigDao;
        this.plateConfig = plateConfig;
        this.code = code;
        this.simService = simService;
        this.scheduledExecutorService = scheduledExecutorService;
        this.simConfig = simConfig;
    }

    void setStages(List<Stage> stages)
    {
        this.stages = stages;
    }

    @Override
    public void run()
    {
        try
        {
            if (this.weightConfigList == null)
            {
                this.weightConfigList = weightConfigDao.findAll(this.plateConfig.getNumPlates(), code);
                if (!this.weightConfigList.isEmpty())
                {
                    this.isRunning = true;
                }
            }
            if (!this.isRunning)
            {
                simService.removeSession(sessionId);
                return;
            }
            Stage stage = stages.get(currentStage);
            this.currentStateValues = stage.run(weightConfigList.get(currentStep));
            scheduledExecutorService.schedule(this, simConfig.getTickRates(), TimeUnit.SECONDS);
        } catch (SQLException e)
        {
            logger.error("Error getting weight configurations", e);
        } catch (Exception e)
        {
            logger.error("Internal error", e);
        }
    }

    public synchronized void nextStage()
    {
        Stage stage = stages.get(currentStage);
        if (stage instanceof Stage1)
        {
            Stage2 stage2 = (Stage2) stages.get(currentStage + 1);
            stage2.setStabled(true);
        } else if (stage instanceof Stage2)
        {
            Stage2 stage2 = (Stage2) stage;
            stage2.setStabled(false);
        } else if (stage instanceof Stage3)
        {
            currentStep++;
            if (currentStep >= this.weightConfigList.size())
            {
                stop();
                return;
            }
        }
        this.currentStage = (currentStage + 1) % 4;
    }

    void nextStep()
    {
        Stage stage = stages.get(currentStage);
        if (stage instanceof Stage2)
        {
            nextStage();
        }
    }

    boolean isRunning()
    {
        return isRunning;
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

    synchronized SimState getState()
    {
        if (weightConfigList == null)
            return null;
        SimState simState = new SimState();
        simState.setCurrentVehiclePosition(0);
        simState.setPlate(plateConfig.getNumPlates());
        simState.setStep(currentStep);
        simState.setStage(currentStage);
        simState.setNumSteps(weightConfigList.size());
        VehicleState[] vehicleStates = new VehicleState[plateConfig.getNumPlates()];
        double[] values = currentStateValues.getValues();
        simState.setCurrentVehiclePosition(getVehiclePosition());

        if (currentStep != 4)
        {
            WeightConfig weightConfig = weightConfigList.get(currentStep);

            for (int i = 0; i < vehicleStates.length; i++)
            {
                vehicleStates[i] = new VehicleState();
                vehicleStates[i].setPort(simConfig.getStartPort() + i);
                vehicleStates[i].setScaleActive(weightConfig.getScaleActive()[i]);
                if (i < vehicleStates.length - 1)
                {
                    vehicleStates[i].setScaleJoin(weightConfig.getScaleActive()[i]);
                } else
                {
                    vehicleStates[i].setScaleJoin(false);
                }
                vehicleStates[i].setWeight(values[i] / 1000);
            }
        } else
        {
            for (int i = 0; i < vehicleStates.length; i++)
            {
                vehicleStates[i] = new VehicleState();
                vehicleStates[i].setPort(simConfig.getStartPort() + i);
                vehicleStates[i].setScaleActive(false);
                vehicleStates[i].setScaleJoin(false);
                vehicleStates[i].setWeight(values[i] / 1000);
            }
        }
        simState.setVehicleStates(vehicleStates);
        return simState;
    }

    void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public String getSessionId()
    {
        return sessionId;
    }


    public SimConfig getSimConfig()
    {
        return simConfig;
    }

    public double getVehiclePosition()
    {
        double lengthPerStep = TOTAL_LENGTH / weightConfigList.size();
        double curOffset = currentStep * lengthPerStep;
        return curOffset + lengthPerStep * currentStateValues.getVehiclePosition();
    }

    public void stop()
    {
        isRunning = false;
    }
}
