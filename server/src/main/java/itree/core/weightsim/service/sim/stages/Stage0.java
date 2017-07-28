package itree.core.weightsim.service.sim.stages;

import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.GedgeStruct;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.service.net.SocketGroup;
import itree.core.weightsim.service.sim.SimThread;
import itree.core.weightsim.util.PacketBuilder;

public class Stage0 implements Stage
{
    private SimThread simThread;
    private SimConfig simConfig;
    private int numSentPackets;
    private SocketGroup socketGroup;

    public Stage0(SimThread simThread, SimConfig simConfig, SocketGroup socketGroup)
    {
        this.simThread = simThread;
        this.simConfig = simConfig;
        this.socketGroup = socketGroup;
    }

    @Override
    public void run(PlateConfig plateConfig, WeightConfig weightConfig) throws InterruptedException
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
                numSentPackets = 0;
                simThread.nextStage();
            }
        }
    }
}
