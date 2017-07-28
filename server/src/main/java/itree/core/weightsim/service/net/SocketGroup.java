package itree.core.weightsim.service.net;

import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.service.common.LogService;
import itree.core.weightsim.util.SocketWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Service
public class SocketGroup
{
    private Socket[] sockets;

    @Autowired
    public SocketGroup(SimConfig simConfig, LogService logService)
    {
        int numPorts = simConfig.getNumPorts();
        sockets = new Socket[numPorts];
        for (int i = 0; i < numPorts; i++)
        {
            int port = simConfig.getStartPort() + i;
            InetSocketAddress inetSocketAddress = new InetSocketAddress(simConfig.getHostName(), port);
            sockets[i] = new Socket(i, simConfig, logService, new SocketWrapper(simConfig.isInitiator(), inetSocketAddress));
        }
    }

    public void send(int idx, Object object)
    {
        sockets[idx].send(object);
    }

    @PreDestroy
    public void cleanup()
    {
        //close the connections
        for (Socket socket : sockets)
        {
            socket.cleanup();
        }
    }
}
