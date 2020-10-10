package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import server.player.*;
import server.Modes.*;
import java.io.FileNotFoundException;

//  TODO: dont need to abstract the player class since socket closed reacts to the tcp channel being closed by 
// throwing an exception.



public class GameController {
    
    private ArrayList<String> dictionary = new ArrayList<String>();
    private static ArrayList<Player> playerArray = new ArrayList<Player>();
    private static ServerSocket serverSocket;
    public final static int PORT = 2048;
    private String WORDFILE = "CollinsScrabbleWords2019.txt";
    private static Boolean menuRun = true;
    private static Boolean running = false;

    public static void main(String argv[]) throws Exception {
        startNewGame();
    }

    private static void startNewGame() throws FileNotFoundException, IOException , InterruptedException{
        GameModes gameMode = new GameModes();
        gameMode.loadJsonSettings("gameModes");
        while(menuRun) {
            StartMenu.printMenu(gameMode);
            Scanner in = new Scanner(System.in);
            String userInput = in.nextLine();
            userChoice(userInput, gameMode, in);
        }
    }

    // need to check that number of players > 1 
    private static void startServer(int port, int players) throws IOException{
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started, waiting for players to connect..."); 
            for(int i=1; i < players; i++) {
                Socket connectionSocket = serverSocket.accept();
                ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
                ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
                playerArray.add(new ClientPlayer(i, connectionSocket, inFromClient, outToClient));
            }
            
        } catch (IOException e) { 
            throw new IOException("Couldn't start server. Exception thrown: "+e); 
        } 
    }

     // Should i use this to create the localHost player object?
    private static void createLocalHost() {
            playerArray.add(new LocalPlayer(0, null, null, null));
    }

    public static void addClientPlayer(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        playerArray.add(new ClientPlayer(1,socket,in,out)); // TEST WITH ID 1
    }
    
//LOGIC
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
    private static void startGame(Player player, GameModes mode) {
        System.out.println("ln");
    }



//HERE
    private static void userChoice(String choice, GameModes mode, Scanner in) throws FileNotFoundException, IOException, InterruptedException {
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
        } else if (checkSettings(mode.loadJsonSettings("gameModes"), choice)) {
            createLocalHost();
            startServer(PORT, mode.getNumberOfPlayers());
            startThreads(choice, mode);
        }
    }



    //LOGIC
    private static Boolean checkSettings(ArrayList<String> list, String word) {
        for (String setting: list) {
            if (word.equals(setting)) {
                return true;
            }
        }
        return false;
    }

    private static void startThreads(String choice, GameModes mode) throws IOException, InterruptedException {
        final int players = mode.getNumberOfPlayers();
        ExecutorService threadpool = Executors.newFixedThreadPool(players);
        for(int i=0; i<players; i++) {
            Player aPlayer = playerArray.get(i);
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    startGame(aPlayer, mode);
                }
            };
            threadpool.execute(task);
        }
        threadpool.awaitTermination(mode.getGameTime(), TimeUnit.SECONDS);
        running = false;
        threadpool.shutdownNow();
    }
}

