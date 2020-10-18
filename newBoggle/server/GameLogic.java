package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.*;

import org.json.JSONException;

import server.SmartSearch;
import server.Modes.GameModes;
import server.player.Player;

public class GameLogic {

    String[][] randomizeBoard(String[][] board) {
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
    Boolean checkWordTaken(String input, Player player, GameModes mode, ArrayList<String> wordList)
            throws FileNotFoundException, JSONException, IOException {
        if (player.getWords().contains(input)) {
            return true;
        } else if (mode.loadJsonGameMode(mode.getGameMode(), "wordUses").contains("once") && wordList.contains(input)) {
            return true;
        } else {
            return false;
        }
    } 
    Boolean checkWordValid(String input, GameModes mode, ArrayList<String> dictionary) throws FileNotFoundException, IOException {
        if (mode.loadJsonGameMode(mode.getGameMode(), "modeType").contains("numeric")){
            return checkExpression(input);
        } else if (dictionary.contains(input)) {
            return true;
        } else {
            return false;
        }
    }
    private Boolean checkExpression(String input) {
        String[] expressions = input.split("(+)|(\\=)");
        if (expressions[0] + expressions[1] == expressions[2]) {
            return true;
        } else {
            return false;
        }
    }
    Boolean isWordOnBoard(String word, String[][] board, GameModes mode) {
        if (word.matches(".*\\d.*")) {
            word = word.replaceAll("[^\\d]", "");
            System.out.println(word); // test
        } else {
            word = word.replaceAll("QU", "Q");
            System.out.println(word); // test
        }
        final int size = board.length;
        Boolean[][] visited = new Boolean[size][size];
        Boolean found = false;
        //TODO: This could be done better with BST or even Trie
        for(int irow=0; irow< size; irow++) {
            for(Boolean[] vrow : visited) {
                Arrays.fill(vrow, false);
                for(int jcol=0; jcol< size; jcol++) {
                    if(board[irow][jcol].startsWith(word.substring(0,1))) {
                        //Check if the word exists on the Boggle board
                        //Start with matching positions on the boggle board 
                        found = searchWord(board, word, irow, jcol, 1, visited, mode);
                    }
                }
            }
        }
        return found;
    }
    //TODO: Refactor this to something better?
    private Boolean searchWord(String[][] board, String word, int i, int j, int matches, Boolean[][] visited, GameModes mode) {
        int[] dirx = { -1, 0, 0, 1 , -1,  1, 1, -1}; //8 directions including diagonals
        int[] diry = { 0, -1, 1, 0 , -1, -1, 1,  1};
        if(!mode.getGenerous())
            visited[i][j] = true;
        int size=board.length;
        //if(matches>=word.length()) {foundInBoggleBoard=true;} //The word was found on the boggle board
        for(int z=0; z<8; z++) {
            if(((i+dirx[z])>=0 && (i+dirx[z])<size) && ((j+diry[z])>=0 && (j+diry[z])<size) && (!visited[i+dirx[z]][j+diry[z]]) && matches<word.length()) {
                if(word.substring(matches,matches+1).equals(board[i+dirx[z]][j+diry[z]])) {
                    searchWord(board, word, i+dirx[z], j+diry[z], matches+1, visited, mode);
                }
            }
            if (matches>=word.length()) {
                return true;
            }
            //if(foundInBoggleBoard) return true; //some branch found the word in the boggleboard
        }
        return false; //The word was not found on the boggle board
    }
    public void searchWord(ArrayList<String> dict, String[][] board, int boardSize) {

        SmartSearch search = new SmartSearch(dict,);
    }

    public Boolean getUseWordsOnce(GameModes mode) throws FileNotFoundException, JSONException, IOException {
        if (mode.loadJsonGameMode(mode.getGameMode(), "wordUses").contains("once")){
            return true;
        }
        return false;
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


}