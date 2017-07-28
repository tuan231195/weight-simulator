package itree.core.weightsim.service;

import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.util.SimThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class SimService
{
    private SimConfig simConfig;
    private SocketGroup socketGroup;
    private WeightGenerator weightGenerator;
    private ScheduledExecutorService executorService;
    private SimThreadFactory simThreadFactory;

    @Autowired
    public SimService(SimConfig simConfig, SocketGroup socketGroup, ScheduledExecutorService executorService, SimThreadFactory simThreadFactory)
    {
        this.simConfig = simConfig;
        this.socketGroup = socketGroup;
        this.executorService = executorService;
    }

    public void simulate(PlateConfig plate, String vType) throws SQLException
    {
        SimThread simThread = simThreadFactory.getSimThread(plate, vType);
        executorService.execute(simThread);
    }
}
