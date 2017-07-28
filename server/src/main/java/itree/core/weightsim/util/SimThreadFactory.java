package itree.core.weightsim.util;

import itree.core.weightsim.jpa.dao.WeightConfigDao;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.service.SimThread;
import itree.core.weightsim.service.SocketGroup;
import itree.core.weightsim.service.WeightGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimThreadFactory
{
    private final WeightConfigDao weightConfigDao;

    private final SimConfig simConfig;

    private final SocketGroup socketGroup;

    private final TimerWrapper timerWrapper;

    private final WeightGenerator weightGenerator;

    @Autowired
    public SimThreadFactory(WeightConfigDao weightConfigDao, SimConfig simConfig, SocketGroup socketGroup, TimerWrapper timerWrapper, WeightGenerator weightGenerator)
    {
        this.weightConfigDao = weightConfigDao;
        this.simConfig = simConfig;
        this.socketGroup = socketGroup;
        this.timerWrapper = timerWrapper;
        this.weightGenerator = weightGenerator;
    }

    public SimThread getSimThread(PlateConfig plateConfig, String type)
    {
        return new SimThread(plateConfig, type, weightConfigDao, simConfig, socketGroup, timerWrapper, weightGenerator);
    }
}
