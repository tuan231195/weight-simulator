package itree.core.weightsim.service.net;

import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.service.common.LogService;
import itree.core.weightsim.util.SocketWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

class Socket
{
    private SimConfig simConfig;
    private Logger logger = LoggerFactory.getLogger(Socket.class);
    private int port;
    private int idx;
    private LogService logService;
    private SocketWrapper socketWrapper;

    Socket(int idx, SimConfig simConfig, LogService logService, SocketWrapper socketWrapper)
    {
        this.idx = idx;
        this.simConfig = simConfig;
        port = simConfig.getStartPort() + idx;
        this.logService = logService;
        this.socketWrapper = socketWrapper;
    }


    void send(Object message)
    {
        String hostName = simConfig.getHostName();
        try
        {
            socketWrapper.connect();
            socketWrapper.send(message);
            logService.log(idx, message.toString());
        } catch (IOException e)
        {
            logger.error("Failed to connect to " + hostName + " on port " + port, e);
        } catch (Exception e)
        {
            logger.error("Failed to send to server on port: " + port, e);
        }
    }

    void cleanup()
    {
        logger.debug("Cleaning socket client at port " + port);
        try
        {
            socketWrapper.close();
        } catch (Exception e)
        {
            logger.error("Failed to close connection", e);
        }
    }
}
