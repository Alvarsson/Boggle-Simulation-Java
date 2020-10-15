package server.communication;
import java.io.*;
import server.player.Player;
import server.player.LocalPlayer;
public class Communication {

    public Communication() {

    }

    public void sendMessage(Object msg, Player player) throws IOException {
        //TODO: make sendMessage so we can send information to players.
        String message = (msg instanceof String[][])?MessageParser.parseBoggle((String[][]) msg):(String) msg;
        try {
            if (player.getId() == 0) {
                System.out.println(message);
            } else {
                player.getOutput().writeObject(message);
            }
        } catch (IOException e) {
            throw new IOException("Can't send message. Caught exception: "+ e);
        }
    }

    public String readMessage(Player player) throws IOException {
        try {
            if (player.getId() != 0) {
                return ((String) player.getInput().readObject());
            } else if (player.getId() == 0) {
                //LocalPlayer localPlayer = ((LocalPlayer) player);
                return ((String) ((LocalPlayer)player).getScanner().nextLine());
            } else {
                return "";
            }
            
        } catch (IOException e) {
            throw new IOException("Couldn't read message. Caught exception: "+ e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



}