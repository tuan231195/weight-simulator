package itree.core.weightsim.service;

import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.util.SocketWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

class SocketClient
{
    private SimConfig simConfig;
    private Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private int port;
    private int idx;
    private InetSocketAddress inetSocketAddress;
    private LogService logService;
    private SocketWrapper socketWrapper;

    SocketClient(int idx, SimConfig simConfig, LogService logService, SocketWrapper socketWrapper)
    {
        this.idx = idx;
        this.simConfig = simConfig;
        port = simConfig.getStartPort() + idx;
        inetSocketAddress = new InetSocketAddress(simConfig.getHostName(), port);
        this.logService = logService;
        this.socketWrapper = socketWrapper;
    }


    void send(double weight)
    {
        String hostName = simConfig.getHostName();
        try
        {
            socketWrapper.connect(inetSocketAddress);
            String message = "Test";
            socketWrapper.send(message);
            logService.log(idx, message);
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
