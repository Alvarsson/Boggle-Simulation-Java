package server.player;
import java.util.ArrayList;
import java.net.Socket;
import java.io.*;

public class LocalPlayer extends Player {

    
    private Boolean isOnline;
    private int id;
    private int score;
    private ArrayList<String> wordList;
    private Socket socket;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    
    public LocalPlayer(int id, Socket socket, ObjectInputStream inStream, ObjectOutputStream outStream) {
        super(id, socket, inStream, outStream);
        this.isOnline = true;
        this.id = id;
        this.score = 0;
        this.wordList = new ArrayList<String>();
        this.socket = socket;
        this.inStream = inStream;
        this.outStream = outStream;
    }

    public Boolean HostOnline() {
        return this.isOnline;
    }
/*
    public int getId() {
        return this.id;
    } 
    public void setId(int id) {
        this.id = id;
    }
    public int getScore() {
        return this.score;
    }
    public void wordScore(int wordLength) {
        int score = 0;
        if (wordLength == 3 || wordLength == 4) {
            score = 1;
        } else if (wordLength == 5) {
            score = 2;
        } else if (wordLength == 6) {
            score = 3;
        } else if (wordLength == 7) {
            score = 5;
        } else if (wordLength == 8) {
            score = 11;
        }
        this.score = score;
    }
    
    public ArrayList<String> getWords() {
        return this.wordList;
    }
    public void addWord(String word) {
        this.wordList.add(word);
    }
*/

}