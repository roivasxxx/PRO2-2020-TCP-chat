package cz.uhk.pro2.tcpchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

/**
 * Thread that handles a single connected client
 */
public class TcpServerUserThread extends Thread {
    private final Socket connectedClientSocket;
    private final MessageBroacaster broacaster;

    public TcpServerUserThread(Socket connectedClientSocket, MessageBroacaster broadcaster) {
        this.connectedClientSocket = connectedClientSocket;
        this.broacaster = broadcaster;
    }

    @Override
    public void run() {
        try {
            InputStream is = connectedClientSocket.getInputStream();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            
            String message=null;
            do
             {
                message=reader.readLine();
                // TODO DU 3.11.2020
                //    zprava "/time" od klienta -> odpovime mu, kolik je hodin
                //    zprava "/quit" od klienta -> ukoncime komunikaci s klientem (vyskocime z while cyklu)
                if(message.toLowerCase().equals("/quit")){
                    break;
                }
                if(message.toLowerCase().equals("/time")){
                    
                    broacaster.broadcastMessage(LocalTime.now().toString(),null);
                }
                if(message!=null){
                System.out.println("New message received: " + message + " " + connectedClientSocket);
                broacaster.broadcastMessage(message,connectedClientSocket);
                }
            }
            while (!message.equals(""));
            if(message.toLowerCase().equals("/quit")){
            System.out.println("New message received: " + message + " " + connectedClientSocket);
            broacaster.broadcastMessage("/quit",connectedClientSocket);
            }
            
            broacaster.broadcastMessage("Someone has left ",null);
            System.out.println("UserThread ended " + connectedClientSocket);

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
