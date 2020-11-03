package cz.uhk.pro2.tcpchat.server;

import javax.swing.text.DateFormatter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

            String message;
            while ((message  = reader.readLine()) != null) {

                // TODO DU 3.11.2020
                //    zprava "/time" od klienta -> odpovime mu, kolik je hodin
                //    zprava "/quit" od klienta -> ukoncime komunikaci s klientem (vyskocime z while cyklu)

                System.out.println("New message received: " + message + " " + connectedClientSocket);

                switch (message) {
                    case "/time":
                        SimpleDateFormat sdf = new SimpleDateFormat("mm:HH dd.MM.yy");
                        String currentDate = new Date().toString();
                        System.out.println(sdf.parse(currentDate));
                        break;

                    case "/quit":
                        reader.close();
                        return;

                    default:
                        broacaster.broadcastMessage(message, connectedClientSocket.getPort());
                }
            }
            System.out.println("UserThread ended " + connectedClientSocket);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
