package boggle.communication;
import java.io.*;
import java.net.SocketException;
import boggle.player.Player;
import boggle.player.LocalPlayer;

/**
 * <h1> Communication handler </h1>
 * Handles communication between players.
 * 
 * @author Axel Alvarsson
 * @version 1.0
 * @since 2020-10-25
 */

public class Communication {

    /**
     * Used to send message to player screen.
     * @param msg
     * @param player
     * @throws IOException
     */
    public void sendMessage(Object msg, Player player) throws IOException {
        String message = (msg instanceof String[][])?MessageParser.parseBoggle((String[][]) msg):(String) msg;
        try {
            if (player.getId() == 0) {
                if (((LocalPlayer)player).isHostOnline()) {
                    System.out.println(message);
                } 
            } else {
                player.getOutput().writeObject(message);
            }
        } catch (SocketException exception) {
        } catch (IOException e) {
            throw new IOException("Can't send message. Caught exception: "+ e);
        }
    }

    /**
     * This method reads input frmo players using scanner and
     * object input/output stream.
     * @param player
     * @return
     * @throws IOException
     * @throws EOFException
     */
    public String readMessage(Player player) throws IOException, EOFException {
        try {
            if (player.getId() != 0) {
                return ((String) player.getInput().readObject());
            } else if (player.getId() == 0) {
                return ((String) ((LocalPlayer)player).getScanner().nextLine());
                
            } else {
                return "";
            } 
        } catch(SocketException e) {
        } catch (EOFException e) {
            System.out.println("Player left, aborting game.");
            System.exit(0);
        } catch (IOException e) {
            throw new IOException("Couldn't read message. Caught exception: "+ e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}