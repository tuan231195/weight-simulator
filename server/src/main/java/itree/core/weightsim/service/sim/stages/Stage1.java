package itree.core.weightsim.service.sim.stages;

import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.service.net.SocketGroup;
import itree.core.weightsim.service.sim.SimThread;
import itree.core.weightsim.service.sim.WeightCalculator;
import itree.core.weightsim.util.PacketBuilder;

import java.util.List;

public class Stage1 implements Stage
{
    private SimThread simThread;
    private SimConfig simConfig;
    private int numSentPackets;
    private SocketGroup socketGroup;
    private WeightCalculator weightCalculator;

    public Stage1(SimThread simThread, SimConfig simConfig, SocketGroup socketGroup, WeightCalculator weightCalculator)
    {
        this.simThread = simThread;
        this.simConfig = simConfig;
        this.socketGroup = socketGroup;
        this.weightCalculator = weightCalculator;
    }

    @Override
    public void run(PlateConfig plateConfig, WeightConfig weightConfig) throws InterruptedException
    {
        double[] values = new double[plateConfig.getNumPlates()];
        List<Double> realValues = weightCalculator.getWeight(weightConfig);
        if (numSentPackets < simConfig.getNumRamPackets())
        {
            for (int j = 0; j < plateConfig.getNumPlates(); j++)
            {
                double delta = realValues.get(j) / (simConfig.getNumRamPackets() + 1);
                values[j] += delta;
                socketGroup.send(j, PacketBuilder.newGedgeStruct(false, weightCalculator.mutate(values[j], delta)));
            }
            numSentPackets++;
            if (numSentPackets == simConfig.getNumRamPackets())
            {
                numSentPackets = 0;
                simThread.nextStage();
            }
        }
    }


}
