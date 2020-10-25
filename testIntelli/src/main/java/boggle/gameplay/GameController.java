package boggle.gameplay;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.script.ScriptException;
import boggle.gameplay.gridsolvers.*;
import boggle.player.*;
import boggle.modes.*;
import boggle.printouts.*;
import boggle.communication.Communication;

/**
 * <h1>Controller for handling the game.</h1>
 * This class holds the game vital objects
 *  and runs the game correctly
 * @author Axel Alvarsson
 * @version 1.0
 * @since 2020-10-25
 */
public class GameController {
    
    private static ArrayList<String> dictionary = new ArrayList<String>();
    private static ArrayList<Player> playerArray = new ArrayList<Player>();
    private static ArrayList<String> wordList = new ArrayList<String>();
    private static String[][] boggleBoard;
    private static ServerSocket serverSocket;
    public final static int PORT = 2048;
    private static String WORDFILE = "dictionaries/english.txt";
    private static Boolean menuRun = true;
    private static Boolean running = false;
    private static Scanner in;

    /**
     * Method running at start of game until quit.
     * Used to get first input scanner and gives choices to
     * local host.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InterruptedException
     * @throws ScriptException
     */
    public static void startNewGame() throws FileNotFoundException, IOException , InterruptedException , ScriptException{
        GameModes gameMode = new GameModes();
        gameMode.loadJsonSettings("gameModes");
        in = new Scanner(System.in);
        while(menuRun) {
            in.reset();
            StartMenu.printMenu(gameMode);
            String userInput = in.nextLine();
            userChoice(userInput, gameMode, in);
        }
    }
    /**
     * Method used to run start server and provide 
     * serverinfo to assigned player objects.
     * @param port
     * @param players
     * @throws IOException
     */
    public static void startServer(int port, int players) throws IOException{
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started, waiting for players to connect..."); 
            for(int i=1; i < players; i++) {
                Socket connectionSocket = serverSocket.accept();
                ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
                ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
                addClientPlayer(i, connectionSocket, inFromClient, outToClient);
                System.out.println("Player " + i + " connected.");
                outToClient.writeObject("You connected to the server as player " + i);
            }
            running = true;
            
        } catch (IOException e) { 
            throw new IOException("Couldn't start server. Exception thrown: "+ e); 
        } 
    }

    private static void createLocalHost() {
            playerArray.add(new LocalPlayer(0, null, null, null));
    }

    private static void addClientPlayer(int i, Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        playerArray.add(new ClientPlayer(i,socket,in,out)); 
    } 
    
    /**
     * Method for running correct game process and
     *  logic for user events.
     * 
     * @param player
     * @param mode
     * @param boggleBoard
     * @throws IOException
     * @throws ScriptException
     */
    private static void runGame(Player player, GameModes mode, String[][] boggleBoard) throws IOException, ScriptException{
        GameLogic logic = new GameLogic(); 
        Communication messages = new Communication();
        String startInfo = "PlayerID: " + player.getId() + ", Game Mode: " + mode.getGameMode() + " Boggle, Time Limit: " + mode.getGameTime() + " seconds";
        messages.sendMessage(startInfo, player);
        messages.sendMessage(boggleBoard, player);
        
        while(running){
            String userWord = messages.readMessage(player).toUpperCase();
            if (logic.checkWordFree(userWord, player, mode, wordList)) {
                if (logic.checkWordValid(userWord, mode, dictionary, boggleBoard)) { 
                    if (logic.isModeNumeric(mode)) {
                        if (logic.isExpressionOnBoard(userWord, boggleBoard,mode)){
                            messages.sendMessage("Expression OK", player);
                            wordList.add(userWord);
                            player.addWord(userWord);
                            if (logic.getUseWordsOnce(mode)) {
                                for (Player pl: playerArray) {
                                    if (pl != player){
                                        String msg = "Player id "+ pl.getId() + ", played expression:" + userWord;
                                        messages.sendMessage(msg, pl);
                                    }
                                }
                            } 
                        }
                    } else {
                        messages.sendMessage("OK", player);
                        wordList.add(userWord);
                        player.addWord(userWord);
                        if (logic.getUseWordsOnce(mode)) {
                            for (Player pl: playerArray) {
                                if (pl != player){
                                    String msg = "Player id "+ pl.getId() + ", played word:" + userWord;
                                    messages.sendMessage(msg, pl);
                                }
                            }
                        } 
                    }
                } else {messages.sendMessage("Not Valid", player); }
            } else {messages.sendMessage("Already submitted", player); }
            messages.sendMessage(boggleBoard, player);
        }
    }
    /**
     * Method for checking the user choice against game possibilities
     * Allows for user choice to check against possibilities in game mode.
     * Starts the game play if mode is seleceted to run. 
     * @param choice
     * @param mode
     * @param in
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InterruptedException
     * @throws ScriptException
    */
    private static void userChoice(String choice, GameModes mode, Scanner in) throws FileNotFoundException, IOException, InterruptedException, ScriptException {
        wordList.clear();
        GameLogic logic = new GameLogic();
        if (choice.equals("settings")) {
            StartMenu.printSettings(mode);
            String settingsChoice = in.nextLine();
            String [] setter = settingsChoice.split(" ", 2);
            if (setter.length != 2) {
                System.out.println("Faulty input, write as, e.g. '3 players', '50 seconds'...  ");
                return;
            }
            if (setter[1].equals("size")) {
                if (logic.checkSettings(mode.loadJsonSettings("boardSizes"),
                    setter[0])) {
                    mode.setBoardSize(setter[0]);
                } else {System.out.println("Size not in game");}

            } else if (setter[1].equals("dice")) {
                    if (setter[0].equals("generous")) {
                        mode.setGenerous(true);
                    } else if (setter[0].equals("once")) {
                        mode.setGenerous(false);
                    }

            } else if (setter[1].equals("lang")) {
                if (logic.checkLanguageAvaliable(mode, setter[0])) {
                    WORDFILE = "dictionaries/"+setter[0]+".txt";
                }

            } else if (setter[1].equals("players")) {
                logic.checkAmountPlayers(mode, setter[0]);
                
            } else if (setter[1].equals("seconds")) {
                logic.checkSetTime(mode,setter[0]);
            }

        } else if (choice.equals("words")) {
            if (dictionary.size() != 0) {
                logic.getAllPossibleWords(dictionary);
                
            } else {
                System.out.println("No previous game found");
            }

        } else if (choice.equals("quit")) {
            System.out.println("BYE BYE");
            menuRun = false;
            System.exit(0);
        } else if (logic.checkSettings(mode.loadJsonSettings("gameModes")
                    , choice)) {
            in.reset();
            mode.setGameMode(choice);
            boggleBoard = logic.randomizeBoard(BoggleBoards.getBoggleBoard(mode.getGameMode(), mode.getBoardSize()));
            if (!logic.isModeNumeric(mode)) {
                dictionary = logic.loadDictionary(WORDFILE);
                SmartSearch newdict = new SmartSearch(dictionary, boggleBoard, boggleBoard.length, mode.getGenerous());
                dictionary = newdict.getCurrentDict();
            }
            createLocalHost();
            startServer(PORT, mode.getNumberOfPlayers());
            startThreads(choice, mode);
            logic.printEndScore(playerArray);
            logic.getWinner(playerArray);
            endGame(mode); 
        }
    }
    /**
     * Method to create threads for the player objects 
     * and awaits set time termination.
     * @param choice
     * @param mode
     * @throws InterruptedException
     * @throws IOException
     * @throws ScriptException
     */
    private static void startThreads(String choice, final GameModes mode) throws InterruptedException, IOException, ScriptException {
        final int players = mode.getNumberOfPlayers();
        ExecutorService threadpool = Executors.newFixedThreadPool(players);
        for(int i=0; i<players; i++) {
            final Player aPlayer = playerArray.get(i);
            Runnable task = new Runnable() {
                //@Override
                public void run() {
                    try {
                        runGame(aPlayer, mode, boggleBoard);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ScriptException e) {
                        new ScriptException("Error evaluating expression: "+e);
                    }
                }
            };
            threadpool.execute(task);
        }
        threadpool.awaitTermination(mode.getGameTime(), TimeUnit.SECONDS);
        running = false;
        threadpool.shutdownNow();
    }
    /**
     * Method to close necessary parts after time runs out
     * in game.
     * @param mode
     * @throws IOException
     */
    static void endGame(GameModes mode) throws IOException {
        Communication msg = new Communication();
        for (Player player : playerArray){
            try {
                if (player.getId() != 0) {
                    player.getOutput().flush();
                    player.getInput().close();
                    msg.sendMessage("CLOSE SOCKET", player);
                    player.getSocket().close();
                } else if (player.getId() == 0) {
                    ((LocalPlayer)player).setHostOffline();
                } 
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerArray.clear();
        serverSocket.close();
        System.out.println("");
    }
}

