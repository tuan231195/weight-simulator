package itree.core.weightsim.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketWrapper
{
    private Socket socket;
    private ServerSocket serverSocket;
    private PrintWriter printWriter;
    private SocketAddress socketAddress;
    private boolean isInitiator;
    private boolean isInitiated;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Logger logger = LoggerFactory.getLogger(SocketWrapper.class);

    public SocketWrapper(boolean isInitiator, InetSocketAddress socketAddress)
    {
        this.socketAddress = socketAddress;
        this.isInitiator = isInitiator;
        if (isInitiator)
        {
            this.socket = new Socket();
        }
        else
        {
            try
            {
                this.serverSocket = new ServerSocket(socketAddress.getPort());
            }
            catch (IOException e)
            {
                logger.error("Failed to initialize server socket", e);
            }
        }
    }

    public void connect() throws IOException
    {
        if (isInitiator)
        {
            if (!this.socket.isConnected())
            {
                this.socket.connect(socketAddress);
                this.printWriter = new PrintWriter(socket.getOutputStream(), true);
            }
        }
        else
        {
            if (!isInitiated || (this.socket != null && this.socket.isClosed()))
            {
                isInitiated = true;
                executorService.submit(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            //listen for connection
                            socket = serverSocket.accept();
                        }
                        catch (IOException e)
                        {
                            logger.error("Failed to initialize server socket", e);
                        }
                    }
                });
            }

        }
    }


    public void send(Object message) throws IOException
    {
        if (this.socket != null)
        {
            printWriter.print(message.toString());
        }
    }

    public void close() throws IOException
    {
        if (socket != null && socket.isConnected())
        {
            socket.getOutputStream().close();
            socket.close();
        }
        if (serverSocket != null)
        {
            serverSocket.close();
        }
        executorService.shutdownNow();
    }

}
