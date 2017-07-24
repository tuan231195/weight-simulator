package itree.core.weightsim.service;

import itree.core.weightsim.model.SimConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;

@Service
public class SimService
{
    private SimConfig simConfig;
    private SocketGroup socketGroup;
    private WeightGenerator weightGenerator;
    private ScheduledExecutorService executorService;

    @Autowired
    public SimService(SimConfig simConfig, SocketGroup socketGroup, ScheduledExecutorService executorService)
    {
        this.simConfig = simConfig;
        this.socketGroup = socketGroup;
        this.executorService = executorService;
    }

    public void simulate(String plate)
    {

    }

    class SimThread implements Runnable
    {

        public void run()
        {

        }
    }
}
