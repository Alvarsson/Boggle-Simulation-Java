package server.player;
import java.util.ArrayList;
import java.net.Socket;
import java.io.*;

public class ClientPlayer extends Player {

    public ClientPlayer(int id, Socket socket, ObjectInputStream inStream, ObjectOutputStream outStream){
        super(id, socket, inStream, outStream);
    }


}