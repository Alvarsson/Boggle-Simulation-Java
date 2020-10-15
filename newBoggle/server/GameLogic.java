package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.*;

import org.json.JSONException;

import server.Modes.GameModes;
import server.player.Player;

public class GameLogic {

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

    //checks if word is acceptable against the current mode and if its already taken.
    static Boolean checkWordTaken(String input, Player player, GameModes mode, ArrayList<String> wordList)
            throws FileNotFoundException, JSONException, IOException {
        if (player.getWords().contains(input)) {
            return false;
        } else if (mode.loadJsonGameMode(mode.getGameMode(), "wordUses").contains("once") && wordList.contains(input)) {
            return false;
        } else {
            return true;
        }
    } 
    static Boolean checkWordValid(String input, GameModes mode, ArrayList<String> dictionary) throws FileNotFoundException, IOException {
        if (mode.loadJsonGameMode(mode.getGameMode(), "gameType").equals("numeric")){
            return checkExpression(input);
        } else if (dictionary.contains(input)) {
            return true;
        } else {
            return false;
        }
    }
    static Boolean checkExpression(String input) {
        String[] expressions = input.split("(+)|(\\=)");
        if (expressions[0] + expressions[1] == expressions[2]) {
            return true;
        } else {
            return false;
        }
    }


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


}