package projectBoggle.communication;
import java.io.*;
import java.net.SocketException;

import projectBoggle.player.Player;
import projectBoggle.player.LocalPlayer;

public class Communication {
    public Communication() {

    }
    public void sendMessage(Object msg, Player player) throws IOException {
        String message = (msg instanceof String[][])?MessageParser.parseBoggle((String[][]) msg):(String) msg;
        try {
            if (player.getId() == 0) {
                System.out.println(message);
            } else {
                player.getOutput().writeObject(message);
            }
        } catch (SocketException exception) {
        } catch (IOException e) {
            throw new IOException("Can't send message. Caught exception: "+ e);
        }
    }

    public String readMessage(Player player) throws IOException, EOFException {
        try {
            if (player.getId() != 0) {
                return ((String) player.getInput().readObject());
            } else if (player.getId() == 0) {
                //LocalPlayer localPlayer = ((LocalPlayer) player);
                System.out.println(((LocalPlayer)player).getScanner().nextLine());
                return ((String) ((LocalPlayer)player).getScanner().nextLine());
            } else {
                return "";
            } 
        } catch(SocketException e) {
            return "";
        } catch (EOFException e) {
            System.out.println("Game has ended");
        } catch (IOException e) {
            throw new IOException("Couldn't read message. Caught exception: "+ e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //} 
        return "";
    }



}