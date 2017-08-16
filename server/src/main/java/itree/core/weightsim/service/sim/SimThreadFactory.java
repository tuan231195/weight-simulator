package itree.core.weightsim.service.sim;

import itree.core.weightsim.jpa.dao.WeightConfigDao;
import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.service.net.SocketGroup;
import itree.core.weightsim.service.sim.stages.*;
import itree.core.weightsim.util.LoggerWrapper;
import itree.core.weightsim.util.LoggerWrapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class SimThreadFactory
{
    private final WeightConfigDao weightConfigDao;

    private final SimConfig simConfig;

    private final SocketGroup socketGroup;

    private final WeightCalculator weightCalculator;

    private final ScheduledExecutorService scheduledExecutorService;

    private LoggerWrapper loggerWrapper = LoggerWrapperFactory.getLogger(getClass());

    @Autowired
    public SimThreadFactory(WeightConfigDao weightConfigDao, SimConfig simConfig, SocketGroup socketGroup, WeightCalculator weightCalculator, ScheduledExecutorService executorService)
    {
        this.weightConfigDao = weightConfigDao;
        this.simConfig = simConfig;
        this.socketGroup = socketGroup;
        this.weightCalculator = weightCalculator;
        this.scheduledExecutorService = executorService;
    }

    public SimThread getSimThread(String sessionId, PlateConfig plateConfig, String type)
    {
        SimThread simThread = null;
        try
        {
            List<WeightConfig> weightConfigList = weightConfigDao.findAll(plateConfig.getNumPlates(), type);
            if (weightConfigList != null && !weightConfigList.isEmpty())
            {
                simThread = new SimThread(sessionId, plateConfig, type, simConfig, weightConfigList, scheduledExecutorService);
                List<Stage> stages = new ArrayList<Stage>();
                Stage stage0 = new Stage0(simThread, socketGroup);
                stages.add(stage0);
                Stage stage1 = new Stage1(simThread, socketGroup, weightCalculator);
                stages.add(stage1);
                Stage stage2 = new Stage2(simThread, socketGroup, weightCalculator);
                stages.add(stage2);
                Stage stage3 = new Stage3(simThread, socketGroup, weightCalculator);
                stages.add(stage3);
                simThread.setStages(stages);
            }

        }
        catch (SQLException e)
        {
            loggerWrapper.error("Failed to get weight configs", e);
        }

        return simThread;
    }
}
