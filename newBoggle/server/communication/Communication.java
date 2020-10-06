package server.communication;
import java.io.*;
import server.communication.MessageParser;

public class Communication {
    private ObjectInputStream inFromPlayer;
    private ObjectOutputStream outToPlayer;

    public Communication() {

    }

    public void sendMessage(Object msg) throws IOException {
        //TODO: make sendMessage so we can send information to players.
        String message = (msg instanceof String[][])?MessageParser.parseBoggle((String[][]) msg):(String) msg;
        try {
            outToPlayer.writeObject(message);
        } catch (IOException e) {
            throw new IOException("Can't send message. Caught exception: "+ e);
        }
    }

    public String readMessage() throws IOException {
        //TODO: make recieveMessage for players to read information.
        try {
            return ((String) inFromPlayer.readObject());
        } catch (IOException e) {
            throw new IOException("Couldn't read message. Caught exception: "+ e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



}