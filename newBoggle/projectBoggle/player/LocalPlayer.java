package projectBoggle.player;

import java.util.Scanner;
import java.net.Socket;
import java.io.*;

public class LocalPlayer extends Player {
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private Boolean isOnline;
    private Scanner in = new Scanner(System.in);
    
    public LocalPlayer(int id, Socket socket, ObjectInputStream inStream, ObjectOutputStream outStream) {
        super(id, socket, inStream, outStream);
        this.isOnline = true;
        this.inStream = inStream;
        this.outStream = outStream;
    }
    public Boolean HostOnline() {
        return this.isOnline;
    }
    public void setHostOffline() {
        this.isOnline = false;
    }
    public Scanner getScanner() {
        return this.in;
    }

}