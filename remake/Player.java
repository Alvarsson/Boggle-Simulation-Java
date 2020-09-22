package remake;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javax.script.*;
import java.awt.Robot;
import java.awt.event.KeyEvent;


// Class: Player
//
// Purpose: To handle player related events and shutdown.
//
// Change: Look at individual functions descps. Make sure that player doesnt handle communication.
//         Give that to client instead maybe.

public class Player {
    public int playerID;
    public boolean online;
    public Socket connection;
    public ObjectInputStream inFromClient;
    public ObjectOutputStream outToClient;
    public ArrayList<String> words = new ArrayList<String>();
    Scanner in = new Scanner(System.in);
    
    // Function: Player
    //
    // Arguments: int playerID,
    //            Socket connection,
    //            ObjectInputStream inFromClient,
    //            ObjectOutputStream outToClient,
    //
    // Returns: None
    //
    // Local variables: None
    //
    // Purpose: Setting player object to necessary values.
    //
    // Description: First sets player objects id to input playerID. Next checks if we have connection, if so set online
    //              to true. Lastly sets objects variables to input arguments values.
    //
    // Change: This seems quite simple and good. No immediate change required.

    public Player(int playerID, Socket connection, ObjectInputStream inFromClient, ObjectOutputStream outToClient) {
        this.playerID = playerID;
        if(connection==null)
            this.online = false;
        else
            this.online = true;
        this.connection = connection; this.inFromClient = inFromClient; this.outToClient = outToClient;        
    }
    
    // Function: sendMessage
    //
    // Arguments: Object message
    //
    // Returns: None
    //
    // Local variables: String msg,      
    //
    // Purpose: Sending messages to client or as system print.
    //
    // Description: First set msg depending on message. If online is true try to write msg out to client.
    //              else write msg to system out.
    //
    // Change: Pretty short and concise. Might want to handle msg different depending on message in.
    public void sendMessage(Object message) {
        String msg = (message instanceof String[][])?printBoggle((String[][]) message):(String )message;
        if(online)
            try {outToClient.writeObject(msg);} catch (Exception e) {}
        else
            System.out.println(msg);
    }

    // Function: readMessage
    //
    // Arguments: None
    //
    // Returns: String
    //
    // Local variables: String word,
    //
    // Purpose: reading messages and then returning that as string word.
    //
    // Description: Sets word to empty string. If online true try to set word as string from ObjectInputStream.
    //              Else try to sen word as scanners nextLine return. Last return word.
    //
    // Change: Quite short and concise. Might wanna look at how and exactly what is being read from client object.

    public String readMessage() {
        String word = ""; 
        if(online)
            try{word = (String) inFromClient.readObject();} catch (Exception e){}
        else
            try {word=in.nextLine();} catch(Exception e){}
        return word;
    }
    
    // Function: close
    //
    // Arguments: None
    //
    // Returns: None
    //
    // Local variables: Robot robot,
    //
    // Purpose: Closing Socket and sends last message.
    //
    // Description: Tries to send message and then close connection if online is true. Else
    //              does a dirty fix to release the word=in.nextLine
    //
    // Change: Fix the dirty fix and make sure that we always catch message even if online is false.

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

    // Function: printBoggle
    //
    // Arguments: String[][] currentBoggle
    //
    // Returns: String
    //
    // Local variables: String returnMsg,
    //
    // Purpose: Returning string of the boggleBoard for printing.
    //
    // Description: Sets returnMsg as empty string. Loops over row and column of the boggle board, then
    //              sets returnMsg to column and adds ":" if column equals "Qu". Lastly return returnMsg.
    //
    // Change: Better place or way to check for "Qu"? otherwise quite clear function.

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
    
    // Function: calculateScore
    //
    // Arguments: None
    //
    // Returns: int
    //
    // Local variables: int score,
    //
    // Purpose:
    //
    // Description: Loops over words adding point to score. Replacing chars depending on math expression or not.
    //
    // Change: Looks good and simple. Could rewrite in a "matching" sense but that is probs not even worth the effort
    //         considering the time it would take.

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