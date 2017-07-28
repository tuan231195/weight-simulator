package itree.core.weightsim.service;

import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.util.SocketWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class SocketGroup
{
    private SocketClient[] socketClients;

    @Autowired
    public SocketGroup(SimConfig simConfig, LogService logService)
    {
        int numPorts = simConfig.getNumPorts();
        socketClients = new SocketClient[numPorts];
        for (int i = 0; i < numPorts; i++)
        {
            socketClients[i] = new SocketClient(i, simConfig, logService, new SocketWrapper());
        }
    }

    void send(int idx, double weight)
    {
        socketClients[idx].send(weight);
    }

    @PreDestroy
    public void cleanup()
    {
        //close the connections
        for (SocketClient socketClient : socketClients)
        {
            socketClient.cleanup();
        }
    }
}
