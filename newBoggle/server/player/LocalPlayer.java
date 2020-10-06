package server.player;
import java.util.ArrayList;
import java.net.Socket;
import java.io.*;

public class LocalPlayer extends Player {

    private Boolean isOnline;
    
    public LocalPlayer(int id, Socket socket, ObjectInputStream inStream, ObjectOutputStream outStream) {
        super(id, socket, inStream, outStream);
        this.isOnline = true;
    }

    public Boolean HostOnline() {
        return this.isOnline;
    }
    public void setHostOffline() {
        this.isOnline = false;
    }

}