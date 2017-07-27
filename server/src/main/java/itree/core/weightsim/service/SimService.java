package itree.core.weightsim.service;

import itree.core.weightsim.jpa.dao.WeightConfigDao;
import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class SimService
{
    private SimConfig simConfig;
    private SocketGroup socketGroup;
    private WeightGenerator weightGenerator;
    private ScheduledExecutorService executorService;
    private WeightConfigDao weightConfigDao;

    @Autowired
    public SimService(SimConfig simConfig, SocketGroup socketGroup, ScheduledExecutorService executorService)
    {
        this.simConfig = simConfig;
        this.socketGroup = socketGroup;
        this.executorService = executorService;
    }

    public void simulate(PlateConfig plate, String vType) throws SQLException
    {
        SimThread simThread = new SimThread(plate, vType, weightConfigDao);
        executorService.execute(simThread);

    }
}
