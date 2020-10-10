import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import server.GameController;

public class ConnectClient {

    public static void main(String argv[]) throws IOException, InterruptedException {
    	new ConnectClient("127.0.0.1", GameController.PORT);
    }

    private ConnectClient(String ip, int port) throws IOException, InterruptedException {
        Scanner in = new Scanner(System.in);
        try {
            Socket socket = new Socket(ip, port);
            ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
            //GameController.addClientPlayer(socket, inFromServer, outFromServer);
            Runnable receive = new Runnable() {

				@Override
				public void run() {
					boolean run = true;
					while(run) {
						try {
							String message = (String) inFromServer.readObject();
							if(message.equals("CLOSE SOCKET")) {
								run=false;
								socket.close();
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
				Thread.sleep(50); 
			}
        } catch (IOException e) {
            throw new IOException("Couldn't connect to socket. Error: " + e);
        } catch (InterruptedException e) {
            throw new InterruptedException("Thread interrupted. Error: "+ e);
        }
    }
}