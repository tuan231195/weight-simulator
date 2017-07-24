package itree.core.weightsim.service;

import itree.core.weightsim.model.SimConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class SocketGroup
{
    private SocketClient[] socketClientThreads;

    @Autowired
    public SocketGroup(SimConfig simConfig, LogService logService)
    {
        int numThreads = simConfig.getNumThreads();
        socketClientThreads = new SocketClient[numThreads];
        for (int i = 0; i < numThreads; i++)
        {
            socketClientThreads[i] = new SocketClient(i, simConfig, logService);
        }
    }

    /**
     * Get the current weight at thread index serverIdx
     * @param serverIdx :the server thread index
     * @return: the weight
     */
    public double getWeight(int serverIdx)
    {
        return socketClientThreads[serverIdx].getWeight();
    }

    @PreDestroy
    public void cleanup()
    {
        //close the connections
        for (SocketClient socketClient : socketClientThreads)
        {
            socketClient.cleanup();
        }
    }
}
