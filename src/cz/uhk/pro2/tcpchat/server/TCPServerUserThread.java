package cz.uhk.pro2.tcpchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * thread that handles a single connected client
 */

public class TCPServerUserThread extends Thread {

    private final Socket connectedClientSocket;
    private final MessageBroadcaster broadcaster;

    public TCPServerUserThread(Socket connectedClientSocket, MessageBroadcaster broadcaster) {
        this.connectedClientSocket = connectedClientSocket;
        this.broadcaster = broadcaster;
    }

    @Override
    public void run() {
        try {
            InputStream is = connectedClientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is)); // readline() hazi null kdyz dojde na konec souboru/nebo spojeni, je to jedno
            String message;
            while ((message = reader.readLine()) != null) {
                //todo pokud bude zprava od klienta př. "/time" - nerozesle se ostatnim, ale server mu vrati kolik je hodin - takovy jakoze chatbot
                // todo zprava od klienta "/quit" - ukoncime komunikaci s klientem - vyskocime z while cyklu
                System.out.println("New message received " + message + " " + connectedClientSocket);
                broadcaster.broadcastMessage(message);
            }
            System.out.println("User thread ended " + connectedClientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
