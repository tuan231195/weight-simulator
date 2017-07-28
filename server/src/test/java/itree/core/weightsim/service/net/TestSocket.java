package itree.core.weightsim.service.net;


import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.service.common.LogService;
import itree.core.weightsim.util.SocketWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.InetSocketAddress;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestSocket
{
    private Socket socket;
    @Mock
    private SimConfig simConfig;

    @Mock
    private LogService logService;

    @Mock
    private SocketWrapper socketWrapper;

    private static final String HOST_NAME = "example.org";


    @Before
    public void setup()
    {
        when(simConfig.getStartPort()).thenReturn(2002);
        when(simConfig.getHostName()).thenReturn(HOST_NAME);
        socket = new Socket(1, simConfig, logService, socketWrapper);
    }

    @Test
    public void testSend() throws IOException
    {
        socket.send(20.0);
        verify(socketWrapper).connect();
        verify(socketWrapper).send(anyString());
    }


    @After
    public void cleanup() throws IOException
    {
        socket.cleanup();
        //should close
        verify(socketWrapper).close();
    }
}
