package itree.core.weightsim.service.sim.stages;

import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.model.StageState;
import itree.core.weightsim.service.net.SocketGroup;
import itree.core.weightsim.service.sim.SimThread;
import itree.core.weightsim.util.PacketBuilder;

public class Stage0 implements Stage
{
    private SimThread simThread;
    private SimConfig simConfig;
    private int numSentPackets;
    private SocketGroup socketGroup;
    private double[] values;
    private PlateConfig plateConfig;

    public Stage0(SimThread simThread, SocketGroup socketGroup)
    {
        this.simThread = simThread;
        this.simConfig = simThread.getSimConfig();
        this.socketGroup = socketGroup;
        this.plateConfig = simThread.getPlateConfig();
        values = new double[plateConfig.getNumPlates()];
    }

    @Override
    public StageState run(WeightConfig weightConfig)
    {
        if (numSentPackets < simConfig.getNumInitPackets())
        {
            for (int j = 0; j < plateConfig.getNumPlates(); j++)
            {
                socketGroup.send(j, PacketBuilder.newGedgeStruct(false, 0));
            }
            numSentPackets++;
            if (numSentPackets == simConfig.getNumInitPackets())
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
    }

    private double getCurrentVehicleOffset()
    {
        return 0;
    }
}
