package projectBoggle.gameplay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.json.JSONException;

import projectBoggle.modes.GameModes;
import projectBoggle.communication.Communication;
import projectBoggle.player.Player;

public class GameLogic {
    private ScriptEngineManager mgr = new ScriptEngineManager();
    private ScriptEngine engine = mgr.getEngineByName("JavaScript");
    private Boolean foundExpression = false;

    static String[][] randomizeBoard(String[][] board) {
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
    // Using Fisher Yates algo for randomizing. Modded for 2D
    /* static String[][] randomizeBoard(String[][] board) {
        
        Random rnd = new Random();
        int size = (int) Math.sqrt(board.length);
        for(int i = size -1; i > 0; i--) {
            for (int j = board[i].length - 1; j > 0; j--) {
                int row = rnd.nextInt(i+1);
                int col = rnd.nextInt(j+1);
                String temp = board[i][j];
                board[i][j] = board[row][col];
                board[row][col] = temp;
            }
        }
        for(int i = 0; i< size; i++) {
            for(int j = 0; j< size; j++) {
                System.out.println(board[i][j]);
            }
        } 
        return board;
    } */

    Boolean checkWordFree(String input, Player player, GameModes mode, ArrayList<String> wordList)
            throws FileNotFoundException, JSONException, IOException {
        if (player.getWords().contains(input)) {
            return false;
        } else if (mode.loadJsonGameMode(mode.getGameMode(), "wordUses").contains("once") && wordList.contains(input)) {
            return false;
        } else {
            return true;
        }
    }

    Boolean checkWordValid(String input, GameModes mode, ArrayList<String> dictionary)
            throws FileNotFoundException, IOException, ScriptException {
        if (mode.loadJsonGameMode(mode.getGameMode(), "modeType").contains("numeric")) {
            return checkExpression(input);
        } else if (dictionary.contains(input)) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean checkExpression(String input) throws ScriptException {
        if (input.matches(".*[a-zA-Z]+.*")) { // regex for math expressions, could use ([\\d]|[*/+-])*|[=][\\d]
            return false;
        }
        String[] expression = input.split("=");
        try {
            Boolean evaluated = (engine.eval(expression[0])) == (engine.eval(expression[1]));
            return evaluated;
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return false;
    }
    String isExpressionOnBoard(String expression, String[][] board, GameModes mode) {
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
        return found?"Expression found!":"Expression not found";
    }

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

    public Boolean getUseWordsOnce(GameModes mode) throws FileNotFoundException, JSONException, IOException {
        if (mode.loadJsonGameMode(mode.getGameMode(), "wordUses").contains("once")){
            return true;
        }
        return false;
    }

    public static int calculatePlayerScore(Player player) {
        return player.calculateScore();
    }

    static ArrayList<String> loadDictionary(String WORDFILE) throws FileNotFoundException, IOException{
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
    static void printEndScore(ArrayList<Player> playerArray) throws IOException {
        Communication msg = new Communication();
        String finalScore = "";
        for (Player player : playerArray) {
            finalScore += "Player "+player.getId()+" got "+ calculatePlayerScore(player)+" points.\n";
        }
        for (Player player : playerArray) {
            msg.sendMessage(finalScore,player);
        }
    }
}