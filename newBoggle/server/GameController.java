package server;

import java.io.*;
import java.net.*;
import java.util.*;
import server.player.*;
import server.Modes.*;


public class GameController {
    private String WORDFILE = "CollinsScrabbleWords2019.txt";
    private ArrayList<String> dictionary = new ArrayList<String>();
    private ArrayList<Player> playerArray = new ArrayList<Player>();
    private ServerSocket aSocket;
    private int PORT = 2048;

    

    public static void main(String argv[]) throws Exception {
        startNewGame();
        //System.out.println(client.getId());
        //client.setId(3);
        //System.out.println(client.getId());
        //Player localPlayer = new Player();
        //new StartNewGame();
    }

    private static void startNewGame(){
        GameModes gameMode = new GameModes();

        while(gameMode.getGameMode() != "!") {
            StartMenu.printMenu(gameMode);
            Scanner in = new Scanner(System.in);
            String userInput = in.nextLine();
            if (userInput.equals("settings")) {

            }
            else {
                try {
                    
                }
            }
            /* if (userInput.equals("1")){
                gameMode.setGameMode("Standard");
            } else if (userInput.equals("2")){
                gameMode.setGameMode("Battle");
            } else if (userInput.equals("3")){
                gameMode.setGameMode("Foggle");
            } */




        }
    }

    public GameController() {
        

    }
    // need to check that number of players > 1 
    private void startServer(int port) throws IOException{
        try {
            aSocket = new ServerSocket(port);
            System.out.println("Server started"); 
        } catch (IOException e) { 
            throw new IOException("Couldn't start server. Exception thrown: "+e); 
        } 
    }

     // Should i use this to create the localHost player object?
    private void createLocalHost() throws IOException {
        try {
            startServer(PORT);
            playerArray.add(new LocalPlayer(0, null, null, null));
        } catch (IOException e) {
            throw new IOException("Could not create localhost. Exception thrown: "+e);
        }
        
    }

    private void createPlayers(int numberOfPlayers) throws IOException {
        try {
            playerArray.add(new LocalPlayer(0,null,null,null));
            for(int i=1; i<numberOfPlayers; i++) {
                Socket connectionSocket = aSocket.accept();
                ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
                ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
                playerArray.add(new ClientPlayer(i, connectionSocket, inFromClient, outToClient));
            } 
        } catch (IOException e) {
            throw new IOException("Did not get the correct socket.");
        }

    }
    private void readScrabble() throws IOException {
        try {
            FileReader fileReader = new FileReader(WORDFILE);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                dictionary.add(line);
            }
            bufferedReader.close();           
        } catch (IOException e) {
            throw new IOException("Couldn't read the Words file. Exception thrown: "+e);
        }
    }
    




}
