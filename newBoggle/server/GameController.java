package server;

import java.io.*;
import java.net.*;
import java.util.*;
import server.player.*;
import server.Modes.*;
import java.io.FileNotFoundException;

public class GameController {
    
    private ArrayList<String> dictionary = new ArrayList<String>();
    private ArrayList<Player> playerArray = new ArrayList<Player>();
    private ServerSocket aSocket;
    private int PORT = 2048;
    private String WORDFILE = "CollinsScrabbleWords2019.txt";
    private static Boolean menuRun = true;

    

    public static void main(String argv[]) throws Exception {
        startNewGame();
        //System.out.println(client.getId());
        //client.setId(3);
        //System.out.println(client.getId());
        //Player localPlayer = new Player();
        //new StartNewGame();
    }

    private static void startNewGame() throws FileNotFoundException, IOException{
        GameModes gameMode = new GameModes();
        gameMode.loadJsonSettings("gameModes");
        while(menuRun) {
            StartMenu.printMenu(gameMode);
            Scanner in = new Scanner(System.in);
            String userInput = in.nextLine();
            userChoice(userInput, gameMode, in);
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
    
    private static void userChoice(String choice, GameModes mode, Scanner in) throws FileNotFoundException, IOException {
        if (choice.equals("Settings")) {
            StartMenu.printSettings(mode);
            String settingsChoice = in.nextLine();
            String [] setter = settingsChoice.split(" ", 2);
            if (setter[1].equals("size")) {
                if (checkSettings(mode.loadJsonSettings("boardSizes"), setter[0])) {
                    mode.setBoardSize(setter[0]);
                }
            } else if (setter[1].equals("dice")) {
                    if (setter[0].equals("generous")) {
                        mode.setGenerous(true);
                    } else {
                        mode.setGenerous(false);
                    }
            } else if (setter[1].equals("lang")) {
                System.out.println("GAY");
                if (checkSettings(mode.loadJsonSettings("languages"), setter[0])) {
                    System.out.println("teteteY");
                    mode.setLanguage(setter[0]);
                }
            } else if (setter[1].equals("solution")) {
                if (setter[0].equals("show")) {
                    mode.setShowSolution(true);
                } else {
                    mode.setShowSolution(false);
                }
            } else if (setter[1].equals("players")) {
                int i=Integer.parseInt(setter[0]);  
                if (i > 1) {
                    mode.setNumberOfPlayers(i);
                } else {
                    System.out.println("Must be 2 or more players.");
                }
            } else if (setter[1].equals("seconds")) {
                int i=Integer.parseInt(setter[0]); 
                if (i > 0){
                    mode.setGameTime(i);
                } else {
                    System.out.println("Must be 1 or more seconds.");
                }
            }
        } else if (choice.equals("Quit")) {
            System.out.println("BYE BYE");
            menuRun = false;
        } else if (checkSettings(mode.loadJsonSettings("gameModes"), choice)){
            runGame(choice);
        }
    }

    private static Boolean checkSettings(ArrayList<String> list, String word) {
        for (String setting: list) {
            if (word.equals(setting)) {
                return true;
            }
        }
        return false;
    }
}

