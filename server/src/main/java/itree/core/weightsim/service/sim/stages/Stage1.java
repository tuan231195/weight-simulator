package itree.core.weightsim.service.sim.stages;

import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.model.StageState;
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
    private double[] realValues;
    private double[] values;
    private PlateConfig plateConfig;

    public Stage1(SimThread simThread, SocketGroup socketGroup, WeightCalculator weightCalculator)
    {
        this.simThread = simThread;
        this.simConfig = simThread.getSimConfig();
        this.socketGroup = socketGroup;
        this.weightCalculator = weightCalculator;
        this.plateConfig = simThread.getPlateConfig();
        realValues = new double[plateConfig.getNumPlates()];
        values = new double[plateConfig.getNumPlates()];
    }

    @Override
    public StageState run(WeightConfig weightConfig)
    {
        List<Double> computedValues = weightCalculator.getWeight(weightConfig);
        if (numSentPackets < simConfig.getNumRamPackets())
        {
            for (int j = 0; j < this.plateConfig.getNumPlates(); j++)
            {
                double delta = computedValues.get(j) / (simConfig.getNumRamPackets() + 1);
                this.realValues[j] += delta;
                this.values[j] = weightCalculator.mutate(this.realValues[j], delta);
                socketGroup.send(j, PacketBuilder.newGedgeStruct(false, this.values[j]));
            }
            numSentPackets++;
            if (numSentPackets == simConfig.getNumRamPackets())
            {
                simThread.nextStage();
                reset();
            }
        }
        return new StageState(values, getCurrentVehicleOffset());
    }

    private void reset()
    {
        numSentPackets = 0;
        for (int i = 0; i < plateConfig.getNumPlates(); i++)
        {
            values[i] = 0;
            realValues[i] = 0;
        }
    }

    private double getCurrentVehicleOffset()
    {
        return (numSentPackets * 1.0 / simConfig.getNumRamPackets()) / 3;
    }
}
