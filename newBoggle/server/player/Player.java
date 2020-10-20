package server.player;

import java.util.ArrayList;
//import java.util.Scanner;
import java.net.Socket;
import java.io.*;

public abstract class Player {
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    private int id;
    private int score;
    private ArrayList<String> wordList;
    private Socket socket;

    Player(int id, Socket socket, ObjectInputStream inStream, ObjectOutputStream outStream){
        this.outStream = outStream;
        this.inStream = inStream;
        this.id = id;
        this.score = 0;
        this.wordList = new ArrayList<String>();
        this.socket = socket;
    }
    
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Socket getSocket() {
        return this.socket;
    }
    public ObjectOutputStream getOutput() {
        return this.outStream;
    }
    public ObjectInputStream getInput() {
        return this.inStream;
    }

    public int calculateScore() {
        if (wordList.isEmpty()){
            return 0;
        } else if (wordList.get(0).startsWith("\\d")) {
            for (int i = 0; i < wordList.size(); i++) {
                wordList.set(i, wordList.get(i).replaceAll("[^\\d]", ""));
            }
        }
        for (int i = 0; i < wordList.size(); i++) {
            if (wordList.get(i).length() == 3 || wordList.get(i).length() == 4) {
                this.score += 1;
            } else if (wordList.get(i).length() == 5) {
                this.score += 2;
            } else if (wordList.get(i).length() == 6) {
                this.score += 3;
            } else if (wordList.get(i).length() == 7) {
                this.score += 5;
            } else if (wordList.get(i).length() == 8) {
                this.score += 11;
            }
        }
        return this.score;
    }
    public ArrayList<String> getWords() {
        return this.wordList;
    }
    public void addWord(String word) {
        this.wordList.add(word);
    }


}