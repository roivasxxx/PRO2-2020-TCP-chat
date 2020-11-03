package cz.uhk.pro2.tcpchat.server;

import java.net.Socket;

public interface MessageBroadcaster {
    void broadcastMessage(Socket sender, String message);
}
