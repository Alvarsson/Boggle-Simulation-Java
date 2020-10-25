package boggle.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.script.ScriptException;
import org.junit.Test;

import boggle.gameplay.GameLogic;
import boggle.gameplay.gridsolvers.SmartSearch;
import boggle.modes.GameModes;
import boggle.player.LocalPlayer;
import boggle.player.Player;
public class Tests {

    @Test
    public void doesSmartSearchFindWord() {
        ArrayList<String> dict = new ArrayList<String>();
        dict.add("HELLO");
        String[][] board = {{"H","I","P","O"},
                            {"J","E","K","C"},
                            {"L","D","L","Y"},
                            {"A","U","O","L"}};

        SmartSearch search = new SmartSearch(dict, board, 4, false);
        ArrayList<String> newDict = new ArrayList<String>();
        newDict = search.getCurrentDict();
        assertTrue(newDict.get(0).equals(dict.get(0)));
    } 
    @Test
    public void tooFewPlayers() throws FileNotFoundException, IOException {
        GameModes mode = new GameModes();
        mode.setNumberOfPlayers(-7);
        assertEquals(mode.getNumberOfPlayers(), 2);
    }
    @Test
    public void manyPlayers() throws FileNotFoundException, IOException {
        GameModes mode = new GameModes();
        mode.setNumberOfPlayers(100);
        assertEquals(mode.getNumberOfPlayers(), 100);
    }
    @Test
    public void boardIsRandom() {
        GameLogic logic = new GameLogic();
        String[][] board = {{"R", "I", "F", "O", "B", "X"},
        {"I", "F", "E", "H", "E", "Y"},
        {"D", "E", "N", "O", "W", "S"},
        {"U", "T", "O", "K", "N", "D"},
        {"H", "M", "S", "R", "A", "O"},
        {"L", "U", "P", "E", "T", "S"},
        {"A", "C", "I", "T", "O", "A"},
        {"Y", "L", "G", "K", "U", "E"},
        {"Qu", "B", "M", "J", "O", "A"},
        {"E", "H", "I", "S", "P", "N"},
        {"V", "E", "T", "I", "G", "N"},
        {"B", "A", "L", "I", "Y", "T"},
        {"E", "Z", "A", "V", "N", "D"},
        {"R", "A", "L", "E", "S", "C"},
        {"U", "W", "I", "L", "R", "G"},
        {"P", "A", "C", "E", "M", "D"}};
        int size = (int) Math.sqrt(board.length);
        String[][] rndBoard = logic.randomizeBoard(board);
        String letterOrderFirstBoard = "";
        String letterOrderRndBoard = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                letterOrderFirstBoard += board[i][j];
                letterOrderRndBoard += rndBoard[i][j];
            }
        }
        assertNotSame(letterOrderFirstBoard, letterOrderRndBoard);
    }
    @Test
    public void testScoreAmount() throws IOException {
        Player player = new LocalPlayer(0, null, null, null);
        player.addWord("TJO");
        player.addWord("TJOS");
        player.addWord("HO");
        
        int score = player.calculateScore();
        assertEquals(2, score);
    }
    @Test
    public void testScoreAmountWithQU() {
        Player player = new LocalPlayer(0, null, null, null);
        player.addWord("TJO");
        player.addWord("TJOS");
        player.addWord("HO");
        player.addWord("QUOTA");
        int score = player.calculateScore();
        assertEquals(4, score);
    }

    @Test
    public void testWordUsesPerPlayer() throws FileNotFoundException, IOException {
        Player player = new LocalPlayer(0, null, null, null);
        player.addWord("TJO");
        GameLogic logic = new GameLogic();
        GameModes mode = new GameModes();
        ArrayList<String> wordList = new ArrayList<String>();
        wordList.add("TJO");
        Boolean isWordTaken = logic.checkWordFree("TJO", player, mode, wordList);
        assertFalse(isWordTaken);
    }

    @Test
    public void testWordTakenInBattleMode() throws FileNotFoundException, IOException {
        Player player = new LocalPlayer(0, null, null, null);
        player.addWord("TJO");
        GameLogic logic = new GameLogic();
        GameModes mode = new GameModes();
        mode.setGameMode("battle");
        ArrayList<String> wordList = new ArrayList<String>();
        wordList.add("TJO");
        Player player2 = new LocalPlayer(1, null, null, null);
        Boolean isWordTaken = logic.checkWordFree("TJO", player2, mode, wordList);
        assertFalse(isWordTaken);
    } 
    @Test
    public void changeBoardSize() throws FileNotFoundException, IOException {
        GameModes mode = new GameModes();
        String firstBoardSize = mode.getBoardSize();
        mode.setBoardSize("5x5");
        String secondBoardSize = mode.getBoardSize();
        assertNotSame(firstBoardSize, secondBoardSize);
    }
    @Test
    public void testGenerousBoggleWorks() {
        String[][] board = {{"H","I","P","O"},
                            {"J","E","K","C"},
                            {"L","D","L","Y"},
                            {"A","U","O","L"}};
        ArrayList<String> wordList = new ArrayList<String>();
        wordList.add("EKE");
        SmartSearch search = new SmartSearch(wordList, board, 4, true);
        ArrayList<String> generousWordList = search.getCurrentDict(); 
        String word = generousWordList.get(0);
        assertEquals("EKE", word);
    }
    @Test
    public void testIsAMathExpression() throws ScriptException {
        GameLogic logic = new GameLogic();
        String expr = "4*1+6/2=7";
        Boolean thisIsTrue = logic.checkExpression(expr);
        assertTrue(thisIsTrue);
    }
    @Test
    public void testNotAMathExpression() throws ScriptException {
        GameLogic logic = new GameLogic();
        String expr = "4*1+a/5=2";
        Boolean thisIsTrue = logic.checkExpression(expr);
        assertFalse(thisIsTrue);
    }

    
}

