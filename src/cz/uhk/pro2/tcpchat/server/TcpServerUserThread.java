package cz.uhk.pro2.tcpchat.server;

import java.io.*;
import java.net.Socket;

public class TcpServerUserThread extends Thread {
    private final Socket connectedClientSocket;
    private final MessageBroadcaster broadcaster;


    public TcpServerUserThread(Socket connectedClientSocket, MessageBroadcaster broadcaster) {
        this.connectedClientSocket = connectedClientSocket;
        this.broadcaster = broadcaster;
    }

    public void run() {
        try {
            InputStream is = connectedClientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("New message recieved: " + message);
                OutputStream os = connectedClientSocket.getOutputStream();
                PrintWriter w = new PrintWriter(new OutputStreamWriter(os), true);
                switch (message) {
                    case "/time":
                        w.println(java.time.LocalTime.now());
                        break;
                    case "/quit":
                        reader.close();
                        return;
                    default:
                        broadcaster.broadcastMessage(message, connectedClientSocket);
                        break;
                }
            }
            System.out.println("User thread ended " + connectedClientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
