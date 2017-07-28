package itree.core.weightsim.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketWrapper
{
    private Socket socket;
    private PrintWriter printWriter;

    public SocketWrapper()
    {
        this.socket = new Socket();
    }

    public void connect(SocketAddress socketAddress) throws IOException
    {
        if (!this.socket.isConnected())
        {
            this.socket.connect(socketAddress);
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        }
    }


    public void send(String message) throws IOException
    {
        printWriter.print(message);
    }

    public void close() throws IOException
    {
        if (socket != null && socket.isConnected())
        {
            socket.getOutputStream().close();
            socket.close();
        }
    }

}
