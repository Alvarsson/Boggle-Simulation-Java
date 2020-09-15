package remake;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.Robot;
import java.awt.event.KeyEvent;


// Class: Player
// 
//
class Player {
    public int playerID;
    public boolean online;
    public Socket connection;
    public ObjectInputStream inFromClient;
    public ObjectOutputStream outToClient;
    public ArrayList<String> words = new ArrayList<String>();
    Scanner in = new Scanner(System.in);
    
    public Player(int playerID, Socket connection, ObjectInputStream inFromClient, ObjectOutputStream outToClient) {
        this.playerID = playerID;
        if(connection==null)
            this.online = false;
        else
            this.online = true;
        this.connection = connection; this.inFromClient = inFromClient; this.outToClient = outToClient;        
    }
    
    public void sendMessage(Object message) {
        String msg = (message instanceof String[][])?printBoggle((String[][]) message):(String )message;
        if(online)
            try {outToClient.writeObject(msg);} catch (Exception e) {}
        else
            System.out.println(msg);
    }
    
    public String readMessage() {
        String word = ""; 
        if(online)
            try{word = (String) inFromClient.readObject();} catch (Exception e){}
        else
            try {word=in.nextLine();} catch(Exception e){}
        return word;
    }
    
    public void close() {
        try {
            if(online) {
                sendMessage("CLOSE SOCKET");
                connection.close();
            } else {
                //Dirty fix to release the word=in.nextLine(); Doesn't always work
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_ENTER);robot.keyRelease(KeyEvent.VK_ENTER);
            }            
        } catch (Exception e) {System.out.println(e.getMessage());}
    }
    public static String printBoggle(String[][] currentBoggle) {
        String returnMsg = "";
        for(String[] row : currentBoggle){
            for(String column : row) {
                returnMsg += column + (column.equals("Qu")?" ":"  ");
            }
            returnMsg += "\n";
        }
        return returnMsg;
    }
    
    public int calculateScore() {
        int score = 0;
        for(String word : words) {
            if(word.contains("="))
                word = word.replaceAll("[^0-9]", "");
            if(word.length() == 3 || word.length() == 4)
                score += 1;
            if(word.length() == 5)
                score +=2;
            if(word.length() == 6)
                score += 3;
            if(word.length() == 7)
                score += 5;
            if(word.length()> 7)
                score += 11;
        }
        return score;
    }
}