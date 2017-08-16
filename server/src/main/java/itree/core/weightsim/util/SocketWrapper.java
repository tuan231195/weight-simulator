package itree.core.weightsim.util;

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
    private boolean isConnected;
    private ServerSocket serverSocket;
    private PrintWriter printWriter;
    private SocketAddress socketAddress;
    private boolean isInitiator;
    private boolean isInitiated;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private LoggerWrapper logger = LoggerWrapperFactory.getLogger(SocketWrapper.class);

    public SocketWrapper(boolean isInitiator, InetSocketAddress socketAddress)
    {
        this.socketAddress = socketAddress;
        this.isInitiator = isInitiator;
        if (isInitiator)
        {
            this.socket = new Socket();
        } else
        {
            try
            {
                this.serverSocket = new ServerSocket(socketAddress.getPort());
            } catch (IOException e)
            {
                logger.error("Failed to initialize server socket", e);
            }
        }
    }

    public void connect() throws IOException
    {
        if (isInitiator)
        {
            if (!isConnected)
            {
                if (this.socket != null)
                {
                    this.socket = new Socket();
                }
                this.socket.connect(socketAddress);
                isConnected = true;
                this.printWriter = new PrintWriter(socket.getOutputStream(), true);
            }
        } else
        {
            if (!isInitiated || (this.socket != null && !isConnected))
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
                            isConnected = true;
                        } catch (IOException e)
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
            if (printWriter.checkError()) {
                isConnected = false;
            }
            printWriter.flush();
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
