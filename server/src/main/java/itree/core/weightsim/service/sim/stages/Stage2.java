package itree.core.weightsim.service.sim.stages;

import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.StageState;
import itree.core.weightsim.service.net.SocketGroup;
import itree.core.weightsim.service.sim.SimThread;
import itree.core.weightsim.service.sim.WeightCalculator;
import itree.core.weightsim.util.PacketBuilder;

import java.util.Arrays;
import java.util.List;

public class Stage2 implements Stage
{
    private SimThread simThread;
    private SocketGroup socketGroup;
    private boolean isStabled;
    private WeightCalculator weightCalculator;
    private double[] values;
    private PlateConfig plateConfig;

    public Stage2(SimThread simThread, SocketGroup socketGroup, WeightCalculator weightCalculator)
    {
        this.simThread = simThread;
        this.socketGroup = socketGroup;
        this.weightCalculator = weightCalculator;
        this.plateConfig = simThread.getPlateConfig();
        values = new double[plateConfig.getNumPlates()];
    }

    @Override
    public StageState run(WeightConfig weightConfig)
    {
        List<Double> realValues = weightCalculator.getWeight(weightConfig);
        for (int i = 0; i < realValues.size(); i++)
        {
            values[i] = realValues.get(i);
        }
        if (isStabled)
        {
            for (int j = 0; j < plateConfig.getNumPlates(); j++)
            {
                socketGroup.send(j, PacketBuilder.newGedgeStruct(true, values[j]));
            }
        } else
        {
            simThread.nextStage();
        }
        return new StageState(Arrays.copyOf(values, values.length), getCurrentVehicleOffset());
    }

    private double getCurrentVehicleOffset()
    {
        return 2.0 / 3;
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
