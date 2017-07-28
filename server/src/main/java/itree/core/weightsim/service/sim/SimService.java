package itree.core.weightsim.service.sim;

import itree.core.weightsim.model.PlateConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class SimService
{
    private ScheduledExecutorService executorService;
    private SimThreadFactory simThreadFactory;
    private Map<String, SimThread> simThreadMap;
    private Logger logger = LoggerFactory.getLogger(SimService.class);

    @Autowired
    public SimService(ScheduledExecutorService executorService, SimThreadFactory simThreadFactory)
    {
        this.executorService = executorService;
        this.simThreadFactory = simThreadFactory;
        simThreadMap = new HashMap<String, SimThread>();
    }

    public void simulate(String sessionId, PlateConfig plate, String vType) throws SQLException
    {
        logger.debug("Simulating " + plate.getPlateConfigName() + ", " + vType + ". Session id: " + sessionId);
        SimThread simThread = simThreadFactory.getSimThread(plate, vType);
        simThread.setSessionId(sessionId);
        executorService.execute(simThread);
        simThreadMap.put(sessionId, simThread);
    }

    public void stop(String sessionId)
    {
        logger.debug("Stoping session id: " + sessionId);
        SimThread simThread = simThreadMap.get(sessionId);
        if (simThread != null)
        {
            logger.debug("Plate config: " + simThread.getPlateConfig() + ", Code: " + simThread.getCode());
            simThread.stop();
            simThreadMap.remove(sessionId);
        }
    }

    public void nextStep(String sessionId)
    {
        logger.debug("Increment step for session id" + sessionId);
        SimThread simThread = simThreadMap.get(sessionId);
        if (simThread != null)
        {
            logger.debug("Plate config: " + simThread.getPlateConfig() + ", Code: " + simThread.getCode());
            logger.debug("Current Step: " + simThread.getCurrentStep());
            simThread.nextStep();
        }
    }
}
