package itree.core.weightsim.service;

import itree.core.weightsim.jpa.dao.WeightConfigDao;
import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.util.TimerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class SimThread implements Runnable
{
    private String code;
    private PlateConfig plateConfig;
    private boolean isRunning;
    private boolean isStabled;
    private WeightGenerator weightGenerator;
    private WeightConfigDao weightConfigDao;
    private SimConfig simConfig;
    private SocketGroup socketGroup;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private TimerWrapper timerWrapper;

    public SimThread(PlateConfig plateConfig, String code, WeightConfigDao weightConfigDao, SimConfig simConfig, SocketGroup socketGroup, TimerWrapper timerWrapper, WeightGenerator weightGenerator)
    {
        this.weightConfigDao = weightConfigDao;
        this.plateConfig = plateConfig;
        this.code = code;
        this.simConfig = simConfig;
        this.socketGroup = socketGroup;
        this.timerWrapper = timerWrapper;
        this.weightGenerator = weightGenerator;
    }

    public void run()
    {
        isRunning = true;
        List<WeightConfig> weightConfigList = null;
        try
        {
            weightConfigList = weightConfigDao.findAll(this.plateConfig.getNumPlates(), code);
            int currentStage = 0;
            boolean nextStep;
            for (WeightConfig weightConfig : weightConfigList)
            {
                nextStep = false;
                while (!nextStep)
                {
                    switch (currentStage)
                    {
                        case 0:
                            stage0();
                            break;
                        case 1:
                            stage1(weightConfig);
                            break;
                        case 2:
                            stage2(weightConfig);
                            break;
                        case 3:
                            nextStep = true;
                            stage3(weightConfig);
                            break;
                    }
                    currentStage = (currentStage + 1) % 4;
                }
            }
        } catch (SQLException e)
        {
            logger.error("Error getting weight configurations", e);
        } catch (InterruptedException e)
        {
            logger.error("Thread was interrupted", e);
        }

    }

    void stage3(WeightConfig weightConfig) throws InterruptedException
    {
        List<Double> values = weightGenerator.getWeight(weightConfig);

        for (int i = 0; i < simConfig.getRampPackets(); i++)
        {
            for (int j = 0; j < plateConfig.getNumPlates(); j++)
            {
                double delta = weightConfig.getScales()[j] / (simConfig.getRampPackets() + 1);
                values.set(j, values.get(j) - delta);
                socketGroup.send(j, weightGenerator.stage(3, values.get(j), delta));
            }
            timerWrapper.sleep(simConfig.getTickRates());
        }
    }

    void stage2(WeightConfig weightConfig) throws InterruptedException
    {
        isStabled = true;
        while (isStabled)
        {
            for (int j = 0; j < plateConfig.getNumPlates(); j++)
            {
                socketGroup.send(j, weightGenerator.getWeight(weightConfig).get(j));
            }
            timerWrapper.sleep(simConfig.getTickRates());
        }
    }

    public void setStabled(boolean stabled)
    {
        isStabled = stabled;
    }

    void stage1(WeightConfig weightConfig) throws InterruptedException
    {
        double[] values = new double[plateConfig.getNumPlates()];

        for (int i = 0; i < simConfig.getRampPackets(); i++)
        {
            for (int j = 0; j < plateConfig.getNumPlates(); j++)
            {
                double delta = weightConfig.getScales()[j] / (simConfig.getRampPackets() + 1);
                values[j] += delta;
                socketGroup.send(j, weightGenerator.stage(1, values[j], delta));
            }
            timerWrapper.sleep(simConfig.getTickRates());
        }
    }

    void stage0() throws InterruptedException
    {
        for (int i = 0; i < simConfig.getNumInitPackets(); i++)
        {
            for (int j = 0; j < plateConfig.getNumPlates(); j++)
            {
                socketGroup.send(j, 0);
            }
            timerWrapper.sleep(simConfig.getTickRates());
        }
    }
}
