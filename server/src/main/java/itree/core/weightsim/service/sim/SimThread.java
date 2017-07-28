package itree.core.weightsim.service.sim;

import itree.core.weightsim.jpa.dao.WeightConfigDao;
import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
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
    private String sessionId;

    SimThread(PlateConfig plateConfig, String code, SimConfig simConfig, WeightConfigDao weightConfigDao, ScheduledExecutorService scheduledExecutorService)
    {
        this.weightConfigDao = weightConfigDao;
        this.plateConfig = plateConfig;
        this.code = code;
        this.scheduledExecutorService = scheduledExecutorService;
        this.simConfig = simConfig;
    }

    void setStages(List<Stage> stages)
    {
        this.stages = stages;
    }

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
                return;
            }
            Stage stage = stages.get(currentStage);
            stage.run(plateConfig, weightConfigList.get(currentStep));
            scheduledExecutorService.schedule(this, simConfig.getTickRates(), TimeUnit.SECONDS);
        }
        catch (SQLException e)
        {
            logger.error("Error getting weight configurations", e);
            throw new RuntimeException(e);
        }
        catch (InterruptedException e)
        {
            logger.error("Thread was interrupted", e);
        }
    }

    public void nextStage()
    {
        Stage stage = stages.get(currentStage);
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
                isRunning = false;
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

    void stop()
    {
        isRunning = false;
    }

    void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    int getCurrentStep()
    {
        return currentStep;
    }

    PlateConfig getPlateConfig()
    {
        return plateConfig;
    }

    public String getCode()
    {
        return code;
    }
}
