package itree.core.weightsim.service.sim.stages;

import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.service.net.SocketGroup;
import itree.core.weightsim.service.sim.SimThread;
import itree.core.weightsim.service.sim.WeightCalculator;
import itree.core.weightsim.util.PacketBuilder;

import java.util.List;

public class Stage2 implements Stage
{
    private SimThread simThread;
    private SimConfig simConfig;
    private SocketGroup socketGroup;
    private boolean isStabled;
    private WeightCalculator weightCalculator;

    public Stage2(SimThread simThread, SimConfig simConfig, SocketGroup socketGroup, WeightCalculator weightCalculator)
    {
        this.simThread = simThread;
        this.simConfig = simConfig;
        this.socketGroup = socketGroup;
        this.weightCalculator = weightCalculator;
    }

    @Override
    public void run(PlateConfig plateConfig, WeightConfig weightConfig) throws InterruptedException
    {
        List<Double> realValues = weightCalculator.getWeight(weightConfig);
        if (isStabled)
        {
            for (int j = 0; j < plateConfig.getNumPlates(); j++)
            {
                socketGroup.send(j, PacketBuilder.newGedgeStruct(true, realValues.get(j)));
            }
        }
        else
        {
            simThread.nextStage();
        }
    }

    public boolean isStabled()
    {
        return isStabled;
    }

    public void setStabled(boolean stabled)
    {
        isStabled = stabled;
    }
}
