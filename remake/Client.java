package remake;
import java.io.*;
import java.net.*;
import java.util.*;

// Class: Client
//
// Purpose: Same as function
//
// Description: Function Client
//
// Change: ....

public class Client {

	// Function: Client
	//
	// Arguments: String ipAddress,
	//
	// Returns: None
	//
	// Local variables: ObjectInputStream inFromServer,
	//					Scanner in,
	//					Socket aSocket,
	//
	// Purpose: Setting the client stuff needed for players to connect to socket.
	//
	// Description: Takes in an ipAddress, First starting a scanner and then tries to connect to server with
	//				ipAddress. Waiting for socket close message and then making sure to print in correct
	//				order. 
	//
	// Change: Probs some stuff later on...
	//
    public Client (String ipAddress) {
    	ObjectInputStream inFromServer;
    	Scanner in = new Scanner(System.in);
		try {
			//Connect to server
			Socket aSocket = new Socket(ipAddress, 2048);
			ObjectOutputStream outToServer = new ObjectOutputStream(aSocket.getOutputStream());
			inFromServer = new ObjectInputStream(aSocket.getInputStream());
			Runnable receive = new Runnable() { //Be ready to receive messages from the server at any time
				@Override
				public void run() {
					boolean run = true;
					while(run) {
						try {
							String message = (String) inFromServer.readObject();
							if(message.equals("CLOSE SOCKET")) {
								run=false;
								aSocket.close();
								System.exit(0);
							}
							System.out.println(message);					
						} catch (Exception e) {
							System.out.println("Socket has closed");
							System.exit(0);
						}						
					}
				}
			};
			(new Thread(receive)).start();
			while(true) {
				outToServer.writeObject(in.nextLine());
				Thread.sleep(50); //Print things in the correct order
			}						
		} catch(Exception e) {
			System.out.println("Game has ended");
			//threadpool.shutdownNow();
		}
    }

    public static void main(String argv[]) {
    	new Client("127.0.0.1");
    }

}