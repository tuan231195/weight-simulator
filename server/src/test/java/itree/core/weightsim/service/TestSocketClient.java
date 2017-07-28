package itree.core.weightsim.service;


import itree.core.weightsim.model.SimConfig;
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
public class TestSocketClient
{
    private SocketClient socketClient;
    @Mock
    private SimConfig simConfig;

    @Mock
    private LogService logService;

    @Mock
    private SocketWrapper socket;

    @Captor
    private ArgumentCaptor<InetSocketAddress> socketCaptor;

    private static final String HOST_NAME = "example.org";


    @Before
    public void setup()
    {
        when(simConfig.getStartPort()).thenReturn(2002);
        when(simConfig.getHostName()).thenReturn(HOST_NAME);
        socketClient = new SocketClient(1, simConfig, logService, socket);
    }

    @Test
    public void testSend() throws IOException
    {
        socketClient.send(20.0);
        verify(socket).connect(socketCaptor.capture());
        InetSocketAddress inetSocketAddress = socketCaptor.getValue();
        assertEquals(inetSocketAddress.getPort(), 2003);
        assertEquals(inetSocketAddress.getHostName(), HOST_NAME);

        verify(socket).send(anyString());
    }


    @After
    public void cleanup() throws IOException
    {
        socketClient.cleanup();
        //should close
        verify(socket).close();
    }
}
