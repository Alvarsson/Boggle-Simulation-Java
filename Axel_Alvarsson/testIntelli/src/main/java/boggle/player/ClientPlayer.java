package boggle.player;

import java.net.Socket;
import java.io.*;
/**
 * <h1>Client player abstracted calss</h1>
 * 
 * @author Axel Alvarsson
 * @version 1.0
 * @since 2020-10-25
 */
public class ClientPlayer extends Player {

    public ClientPlayer(int id, Socket socket, ObjectInputStream inStream, ObjectOutputStream outStream){
        super(id, socket, inStream, outStream);
    }
}