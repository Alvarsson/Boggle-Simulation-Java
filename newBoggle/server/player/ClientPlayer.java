package server.player;
import java.util.ArrayList;
import java.net.Socket;
import java.io.*;

public class ClientPlayer extends Player {
    private int id;
    private int score;
    private ArrayList<String> wordList;
    private Socket socket;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;

    public ClientPlayer(int id, Socket socket, ObjectInputStream inStream, ObjectOutputStream outStream){
        super(id, socket, inStream, outStream);
        this.id = id;
        this.score = 0;
        this.wordList = new ArrayList<String>();
        this.socket = socket;
        this.inStream = inStream;
        this.outStream = outStream;
    }


}