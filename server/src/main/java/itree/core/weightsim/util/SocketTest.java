package itree.core.weightsim.util;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SocketTest
{
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(2002);
        Socket clientSocket = serverSocket.accept();
        Scanner scanner = new Scanner(clientSocket.getInputStream());
        do
        {
            String str = scanner.nextLine();
            System.out.println(str);
        }
        while(scanner.hasNextLine());
        scanner.close();
        clientSocket.close();
        serverSocket.close();
    }
}
