package itree.core.weightsim.service.sim;

import itree.core.weightsim.jpa.dao.WeightConfigDao;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.service.net.SocketGroup;
import itree.core.weightsim.service.sim.stages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Autowired
    public SimThreadFactory(WeightConfigDao weightConfigDao, SimConfig simConfig, SocketGroup socketGroup, WeightCalculator weightCalculator, ScheduledExecutorService executorService)
    {
        this.weightConfigDao = weightConfigDao;
        this.simConfig = simConfig;
        this.socketGroup = socketGroup;
        this.weightCalculator = weightCalculator;
        this.scheduledExecutorService = executorService;
    }

    public SimThread getSimThread(SimService simService, PlateConfig plateConfig, String type)
    {
        SimThread simThread = new SimThread(plateConfig, type, simService, simConfig, weightConfigDao, scheduledExecutorService);
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
        return simThread;
    }
}
