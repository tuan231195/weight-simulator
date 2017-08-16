package itree.core.weightsim.service.sim;

import itree.core.weightsim.model.PlateConfig;
import itree.core.weightsim.model.SimState;
import itree.core.weightsim.util.LoggerWrapper;
import itree.core.weightsim.util.LoggerWrapperFactory;
import itree.core.weightsim.web.SessionNotFoundException;
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
    private LoggerWrapper logger = LoggerWrapperFactory.getLogger(SimService.class);

    @Autowired
    public SimService(ScheduledExecutorService executorService, SimThreadFactory simThreadFactory)
    {
        this.executorService = executorService;
        this.simThreadFactory = simThreadFactory;
        simThreadMap = new HashMap<String, SimThread>();
    }

    public void simulate(String sessionId, PlateConfig plate, String vType) throws SQLException
    {

        logger.debug(String.format("Simulating %s, %s. Session id: %s", plate.getPlateConfigName(), vType, sessionId));
        SimThread simThread = simThreadFactory.getSimThread(sessionId, plate, vType);
        if (simThread != null)
        {
            simThreadMap.put(sessionId, simThread);
            executorService.execute(simThread);
        }
    }

    public void stop(String sessionId) throws SessionNotFoundException
    {
        logger.debug(String.format("Stoping session id: %s", sessionId));
        SimThread simThread = simThreadMap.get(sessionId);
        if (simThread != null)
        {
            logger.debug(String.format("Plate config: %s, Code: %s", simThread.getPlateConfig(), simThread.getCode()));
            simThread.stop();
        } else
        {
            throw new SessionNotFoundException(sessionId);
        }
    }

    private void removeSession(String sessionId)
    {
        logger.debug("Removed session " + sessionId);
        simThreadMap.remove(sessionId);
    }

    public void nextStep(String sessionId) throws SessionNotFoundException
    {
        logger.debug("Increment step for session id" + sessionId);
        SimThread simThread = simThreadMap.get(sessionId);
        if (simThread != null)
        {
            logger.debug("Plate config: " + simThread.getPlateConfig() + ", Code: " + simThread.getCode());
            logger.debug("Current Step: " + simThread.getCurrentStep());
            simThread.nextStep();
        } else
        {
            throw new SessionNotFoundException(sessionId);
        }
    }

    public SimState getState(String sessionId) throws SessionNotFoundException
    {
        SimThread simThread = simThreadMap.get(sessionId);
        if (simThread != null)
        {
            if (simThread.isStopped())
            {
                removeSession(sessionId);
            }
            logger.debug(String.format("Plate config: %s, Code: %s", simThread.getPlateConfig(), simThread.getCode()));
            return simThread.getState();
        } else
        {
            throw new SessionNotFoundException(sessionId);
        }
    }
}
