package itree.core.weightsim.service;

import itree.core.weightsim.model.SimConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient
{
    private SimConfig simConfig;
    private double weight;
    private Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private Socket socket;
    private PrintWriter printWriter;
    private int port;
    private int idx;
    private InetSocketAddress inetSocketAddress;
    private LogService logService;

    public SocketClient(int idx, SimConfig simConfig, LogService logService)
    {
        this.idx = idx;
        this.simConfig = simConfig;
        port = simConfig.getStartPort() + idx;
        socket = new Socket();
        inetSocketAddress = new InetSocketAddress(simConfig.getHostName(), port);
        this.logService = logService;
    }

    public double getWeight()
    {
        return weight;
    }

    public void send()
    {
        String hostName = simConfig.getHostName();
        try
        {
            //late initialized socket
            if (!socket.isConnected())
            {
                logger.debug("Connecting to port: " + port);
                socket.connect(inetSocketAddress);
            }
            printWriter = new PrintWriter(socket.getOutputStream());
            String message = "Test";
            printWriter.print(message);
            logService.log(idx, message);
        }
        catch (IOException e)
        {
            logger.error("Failed to connect to " + hostName + " on port " + port, e);
        }
        catch (Exception e)
        {
            logger.error("Failed to send to server on port: " + port, e);
        }
    }

    public void cleanup()
    {
        logger.debug("Cleaning socket client at port " + port);
        try
        {
            if (socket != null && socket.isConnected())
            {
                socket.getOutputStream().close();
                socket.close();
            }
        }
        catch (Exception e)
        {
            logger.error("Failed to close connection", e);
        }
    }
}
