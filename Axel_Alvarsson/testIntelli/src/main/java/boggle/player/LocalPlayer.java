package boggle.player;

import java.util.Scanner;
import java.net.Socket;
import java.io.*;
/**
 * <h1>Local player abstracted class</h1>
 * 
 * @author Axel Alvarsson
 * @version 1.0
 * @since 2020-10-25
 */
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
    /**
     * Method to return online statis against serversocket
     * @return Boolean
     */
    public Boolean isHostOnline() {
        return this.isOnline;
    }
    /**
     * Method to set the status as offline when serversocket closes.
     */
    public void setHostOffline() {
        this.isOnline = false;
    }
    /**
     * Method to get local player scanner.
     * @return Scanner object
     */
    public Scanner getScanner() {
        return this.in;
    }

}