package boggle.gameplay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.json.JSONException;
import boggle.modes.GameModes;
import boggle.communication.Communication;
import boggle.player.Player;

/**
 * <h1>Logic methods for gameplay</h1>
 * Class contains logic methods for when game is running.
 * 
 * @author Axel Alvarsson
 * @version 1.0
 * @since 2020-10-25
 */
public class GameLogic {
    private ScriptEngineManager mgr = new ScriptEngineManager();
    private ScriptEngine engine = mgr.getEngineByName("JavaScript");
    private Boolean foundExpression = false;
    /**
     * Method to randomize a given board.
     * @param board
     * @return randomized board
     */
    public String[][] randomizeBoard(String[][] board) {
        int size = (int) Math.sqrt(board.length);
        int returnRow = 0, returnColumn = 0;
        Random rnd = new Random();
        String[][] returnBoggle = new String[size][size];
        List<String[]> rows = Arrays.asList(board);
        Collections.shuffle(rows);
        for (String[] row : rows) {
            returnBoggle[returnRow][returnColumn] = row[rnd.nextInt(6)];
            returnColumn = (returnColumn < (size - 1) ? returnColumn + 1 : 0);
            returnRow = (returnColumn == 0 ? returnRow + 1 : returnRow);
        }
        return returnBoggle;
    } 
    /**
     * Method to check if word is taken by player already.
     * Or if mode is set to battle and word is taken by other player.
     * @param input
     * @param player
     * @param mode
     * @param wordList
     * @return Boolean
     * @throws FileNotFoundException
     * @throws JSONException
     * @throws IOException
     */
    public Boolean checkWordFree(String input, Player player, GameModes mode, ArrayList<String> wordList)
            throws FileNotFoundException, JSONException, IOException {
        if (player.getWords().contains(input)) {
            return false;
        } else if (mode.loadJsonGameMode(mode.getGameMode(), "wordUses").contains("once") && wordList.contains(input)) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * Checks if input is valid.
     * If word checks if that word is in possible words dictionary.
     * Checks expression via expression checker method.
     * @param input
     * @param mode
     * @param dictionary
     * @param board
     * @return Boolean
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ScriptException
     */
    Boolean checkWordValid(String input, GameModes mode, ArrayList<String> dictionary, String[][] board)
            throws FileNotFoundException, IOException, ScriptException {
        if (isModeNumeric(mode)) {
            return checkExpression(input);
        } else if (dictionary.contains(input)) {
            return true;
        } else {
            return false;
        }
    }
    public Boolean checkExpression(String input) throws ScriptException {
        if (input.matches(".*[a-zA-Z]+.*")) { // regex for math expressions, could use ([\\d]|[*/+-])*|[=][\\d]
            return false;
        }
        String[] expression = input.split("=");
        try {
            Boolean evaluated = (engine.eval(expression[0])) == (engine.eval(expression[1]));
            return evaluated;
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }
    /**
     * Method to traverse board with method searchExpression.
     * Iterates board to find start of expression, then call searchExpression.
     * @param expression
     * @param board
     * @param mode
     * @return Boolean
     */
    Boolean isExpressionOnBoard(String expression, String[][] board, GameModes mode) {
        expression = expression.replaceAll("[^\\d]", "");
        foundExpression = false;
        final int size = board.length;
        Boolean[][] visited = new Boolean[size][size];
        Boolean found = false;
        for(Boolean[] vrow : visited) {
            Arrays.fill(vrow, false);
        }
        for(int irow=0; irow< size; irow++) {
            for(int jcol=0; jcol< size; jcol++) {
                if(board[irow][jcol].startsWith(expression.substring(0,1))) {
                    found = searchExpression(board, expression, irow, jcol, 1, visited, mode);
                }
            }
        }
        return found;
    }

    /**
     * Method to check if expression is connected.
     * If found returns true, else false.
     * @param board
     * @param expr
     * @param i
     * @param j
     * @param match
     * @param visited
     * @param mode
     * @return Boolean
     */
    private Boolean searchExpression(String[][] board,String expr, int i, int j, int match, Boolean[][] visited, GameModes mode) {
        int[] dirx = { -1, 0, 1, 1 , 1,  0, -1, -1};
        int[] diry = { -1, -1, -1, 0 , 1, 1, 1,  0};
        if(!mode.getGenerous())
            visited[i][j] = true;
        int boardSize = board.length;
        if (match >= expr.length()) {foundExpression =  true;} 
        for (int z = 0; z< 8; z++) {
            int xSquare = i+dirx[z];
            int ySquare = j+diry[z];
            if ( (xSquare >= 0 && ySquare >= 0) && (xSquare < boardSize && ySquare < boardSize) ) {
                if (!visited[xSquare][ySquare] && match<expr.length()) {
                    if (expr.substring(match,match+1).equals(board[xSquare][ySquare])) {
                        searchExpression(board, expr, xSquare, ySquare, match+1, visited, mode);
                    }
                }
            }
            if (foundExpression) {return true;} 
        }
        return false;
    }
    /**
     * Method to check if game mode is set to word use once.
     * @param mode
     * @return Boolean
     * @throws FileNotFoundException
     * @throws JSONException
     * @throws IOException
     */
    public Boolean getUseWordsOnce(GameModes mode) throws FileNotFoundException, JSONException, IOException {
        if (mode.loadJsonGameMode(mode.getGameMode(), "wordUses").contains("once")){
            return true;
        }
        return false;
    }
    /**
     * Method to give specific player calculated score.
     * @param player
     * @return
     */
    public static int calculatePlayerScore(Player player) {
        return player.calculateScore();
    }
    /**
     * Method to load the original dictionary.
     * @param WORDFILE
     * @return ArrayList
     * @throws FileNotFoundException
     * @throws IOException
     */
    ArrayList<String> loadDictionary(String WORDFILE) throws FileNotFoundException, IOException{
        ArrayList<String> dictionary = new ArrayList<String>();
        try {
            FileReader fileReader = new FileReader(WORDFILE);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                dictionary.add(line);
            }
            bufferedReader.close();
            return dictionary;           
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Word file not found, Exception thrown: "+ e);
        } catch (IOException e) {
            throw new IOException("Could not read the file. Exception thrown: "+ e);
        }
    }
    /**
     * Method for sending end game score to players.
     * @param playerArray
     * @throws IOException
     */
    void printEndScore(ArrayList<Player> playerArray) throws IOException {
        Communication msg = new Communication();
        String finalScore = "";
        for (Player player : playerArray) {
            finalScore += "Player "+player.getId()+" got "+ calculatePlayerScore(player)+" points.\n";
        }
        for (Player player : playerArray) {
            msg.sendMessage(finalScore,player);
        }
    }
    /**
     * Method to print out the correct winner.
     * @param playerArray
     * @throws IOException
     */
    void getWinner(ArrayList<Player> playerArray)throws IOException{
        Communication msg = new Communication();
        String winner = "";
        int winnerScore = 0;
        String winners = "";
        
        for (Player player : playerArray) {
            if (player.getScore() >= winnerScore) {
                winnerScore = player.getScore();
                winner = ""+ player.getId();
            }
        }
        int counter = 0;
        for (Player player : playerArray) {
            if (player.getScore() == winnerScore) {
                counter +=1;
                winners += player.getId()+", ";
            }
        }
        
        for (Player player : playerArray)  {
            if (counter >1) {
                msg.sendMessage("The winners are players: "+ winners+ "with the score: "+ winnerScore, player);
            } else {
                msg.sendMessage("The winner is player: "+ winner +" with the score: "+ winnerScore, player);
            }
            
        }
        
        
    }
    /**
     * Methid to check if input is in the possible settings.
     * @param list
     * @param word
     * @return Boolean
     */
    Boolean checkSettings(ArrayList<String> list, String word) {
        for (String setting: list) {
            if (word.equals(setting)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Checking if the current game mode is numeric or not.
     * @param mode gamemode object
     * @return Boolean
     * @throws FileNotFoundException
     * @throws JSONException
     * @throws IOException
     */
    Boolean isModeNumeric(GameModes mode) throws FileNotFoundException, JSONException, IOException {
        return mode.loadJsonGameMode(mode.getGameMode(), "modeType").contains("numeric");
    }
    /**
     * Method to print out all possible words of previous game.
     * @param dictionary
     */
    public void getAllPossibleWords(ArrayList<String> dictionary) {
        for (int i = 0; i < dictionary.size(); i++) {
            System.out.println(dictionary.get(i));
        }
    }

    /**
     * Method to check valid time input
     * @param mode gamemode object
     * @param time game time input
     */
    void checkSetTime(GameModes mode, String time) {
        try {int i=Integer.parseInt(time);
            if (i > 0){
                mode.setGameTime(i);
            } else {
                System.out.println("Must be 1 or more seconds.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Needs to be a number");
        }
    }

    /**
     * Method to check valid amount of players
     * @param mode gamemode object
     * @param players player number
     */
    void checkAmountPlayers(GameModes mode, String players) {
        try {int i=Integer.parseInt(players);
            mode.setNumberOfPlayers(i);
        } catch (NumberFormatException e) {
            System.out.println("Needs to be a number");
        }
    }

    /**
     * Method to check if a language is avaliable
     * @param mode gamemode object
     * @param language language input
     * @return boolean
     */
    Boolean checkLanguageAvaliable(GameModes mode, String language) {
        try {
            if (checkSettings(mode.loadJsonSettings("languages"),
                    language)) {
                mode.setLanguage(language);
                return true;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not find languages file");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Could not find language.");
        return false;
    }

}