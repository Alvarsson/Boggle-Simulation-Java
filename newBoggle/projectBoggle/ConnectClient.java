package projectBoggle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import projectBoggle.gameplay.GameController;

public class ConnectClient {

    public static void main(String argv[]) throws IOException, InterruptedException {
    	new ConnectClient("127.0.0.1", GameController.PORT);
    }

    private ConnectClient(String ip, int port) throws IOException, InterruptedException {
        ObjectInputStream inFromServer;
        Scanner in = new Scanner(System.in);
        try {
            Socket socket = new Socket(ip, port);
            ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
            inFromServer = new ObjectInputStream(socket.getInputStream());
            Runnable receive = new Runnable() {

				@Override
				public void run() {
					boolean run = true;
					while(run) {
						try {
							String message = (String) inFromServer.readObject();
							if(message.equals("CLOSE SOCKET")) {
                                System.out.println("Socket closed");
                                run=false;
                                in.close();

                                socket.close();
                                System.out.println("Client socket closed");
                                System.exit(0);
                                
							}
							System.out.println(message);					
						} catch (ClassNotFoundException e) {
                            System.out.println("Socket not found");
							System.exit(0);
                        } catch (IOException e) {
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