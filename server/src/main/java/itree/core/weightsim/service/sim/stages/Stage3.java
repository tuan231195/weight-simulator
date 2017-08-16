package itree.core.weightsim.service.sim.stages;


import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.model.StageState;
import itree.core.weightsim.service.net.SocketGroup;
import itree.core.weightsim.service.sim.SimThread;
import itree.core.weightsim.service.sim.WeightCalculator;
import itree.core.weightsim.util.PacketBuilder;

import java.util.Arrays;
import java.util.List;

public class Stage3 implements Stage
{
    private SimThread simThread;
    private SimConfig simConfig;
    private int numSentPackets;
    private SocketGroup socketGroup;
    private WeightCalculator weightCalculator;
    private PlateConfig plateConfig;
    private double[] values;
    private double[] realValues;

    public Stage3(SimThread simThread, SocketGroup socketGroup, WeightCalculator weightCalculator)
    {
        this.simThread = simThread;
        this.simConfig = simThread.getSimConfig();
        this.socketGroup = socketGroup;
        this.weightCalculator = weightCalculator;
        this.plateConfig = simThread.getPlateConfig();
        values = new double[plateConfig.getNumPlates()];
        realValues = new double[plateConfig.getNumPlates()];
    }

    @Override
    public StageState run(WeightConfig weightConfig)
    {
        StageState stageState = null;
        List<Double> computedValues = weightCalculator.getWeight(weightConfig);
        //initialize
        if (numSentPackets == 0)
        {
            for (int i = 0; i < plateConfig.getNumPlates(); i++)
            {
                realValues[i] = computedValues.get(i);
            }
        }

        if (numSentPackets < simConfig.getNumRamPackets() + 1)
        {
            numSentPackets++;
            if (numSentPackets == simConfig.getNumRamPackets() + 1)
            {
                simThread.nextStage();
                reset();
                for (int j = 0; j < plateConfig.getNumPlates(); j++) {
                    socketGroup.send(j, PacketBuilder.newGedgeStruct(false, values[j]));
                }
                stageState = new StageState(Arrays.copyOf(values, values.length), 0);
            }
            else
            {
                for (int j = 0; j < plateConfig.getNumPlates(); j++)
                {
                    double delta = computedValues.get(j) / (simConfig.getNumRamPackets() + 1);
                    realValues[j] -= delta;
                    values[j] = weightCalculator.mutate(realValues[j], delta);
                    socketGroup.send(j, PacketBuilder.newGedgeStruct(false, values[j]));
                }
                stageState = new StageState(Arrays.copyOf(values, values.length), getCurrentVehicleOffset());
            }

        }
        return stageState;
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
        return 2.0 / 3 + (numSentPackets * 1.0/ ((simConfig.getNumRamPackets() + 1) * 3));
    }
}
