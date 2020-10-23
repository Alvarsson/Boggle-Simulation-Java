package projectBoggle.gameplay.gridsolvers;

import java.util.ArrayList;
import java.util.Arrays;

public class SmartSearch {
    private static String[] dictionary;
    private static int boardSize;
    private int dictLength;
    private char[][] board;
    private String[][] entryBoard;
    private static int alphabetSize;
    private static Boolean isGenerous;
    private static ArrayList<String> gameDict = new ArrayList<String>();

    /* 
    Implementation of the trie Node Binary search tree algorithm
    */
    public SmartSearch(ArrayList<String> dict, String[][] board, int boardSize, Boolean isGenerous) {
        this.dictionary = dict.toArray(new String[dict.size()]);
        this.isGenerous = isGenerous;
        this.boardSize = boardSize;
        this.alphabetSize = 26;
        this.entryBoard = board;
        this.board = restructBoard(board, this.boardSize);
        this.dictLength = dict.size();
        this.gameDict.clear();
        

        for(int i = 0; i < boardSize; i++) {
            for (int j = 0; j< boardSize; j++) {
                System.out.print(board[i][j]);
            }
        } 

        TrieNode root = new TrieNode();
        for (int i = 0; i < this.dictLength; i++) {
            insert(root, this.dictionary[i]);
        }

        //findQU(this.entryBoard);
        findWords(this.board, root);

        for(int i = 0; i < gameDict.size(); i ++) {
            System.out.println(gameDict.get(i));
        }
        
    }
    
    public ArrayList<String> getCurrentDict() {
        return gameDict;
    }
    private static char[][] restructBoard(String[][] board, int size) {
        char[][] charBoard = new char[boardSize][boardSize]; 
        for (int i = 0; i < size; i ++) {
            for (int j = 0; j <size; j++) {
                /* if (board[i][j].length() > 1) {
                    board[i][j] = "Q";
                }   */
                charBoard[i][j] = board[i][j].charAt(0);
            } 
        }
        return charBoard;
    }

    private static class TrieNode {
        // English alphabet size
        TrieNode[] Child = new TrieNode[alphabetSize];
        Boolean leaf;

        public TrieNode() { 
            leaf = false; 
            for (int i = 0; i < alphabetSize; i++) 
                Child[i] = null; 
        }
    }

    private static void findWords(char boggle[][], TrieNode root) { 
        // Mark all characters as not visited 
        boolean[][] visited = new boolean[boardSize][boardSize]; 
        TrieNode rootChild = root; 
        String word = ""; 
        // traverse all elements 
        for (int i = 0; i < boardSize; i++) { 
            for (int j = 0; j < boardSize; j++) { 
                // search for word in dictionary if find character Trie root 
                if (rootChild.Child[(boggle[i][j]) - 'A'] != null) { 
                    word = word + boggle[i][j]; 
                    searchWord(rootChild.Child[(boggle[i][j]) - 'A'], 
                               boggle, i, j, visited, word); 
                    word = ""; 
                } 
            } 
        } 
    } 

    private static void insert(TrieNode root, String Key) { 
        int n = Key.length(); 
        TrieNode rootChild = root; 
        for (int i = 0; i < n; i++) { 
            int index = Key.charAt(i) - 'A'; 
  
            if (rootChild.Child[index] == null) 
                rootChild.Child[index] = new TrieNode(); 
  
            rootChild = rootChild.Child[index]; 
        } 
        // make last node as leaf node 
        rootChild.leaf = true; 
    } 

    private static void searchWord(TrieNode root, char boggle[][], int i, int j, boolean visited[][], String str) { 
        // if we found word in trie / dictionary 
        if (root.leaf == true) 
            gameDict.add(str);

        /* if (boggle[i][j] == 'Q') {
            str = str + 'U';
            System.out.println(str);
        }  */
        
        // If i and j in range + visited element  
        // that element of matrix first time 
        if (checkCurrentSquare(i, j, visited)) { 
            // make it visited 
            visited[i][j] = true; 

            // traverse all child of current root 
            for (int K = 0; K < alphabetSize; K++) { 
                if (root.Child[K] != null) { 
                    // current character 
                    char character = (char)(K + 'A'); 
                    
  
                    // Recursively search 8 squares around chosen square
                    if (checkCurrentSquare(i + 1, j + 1, visited) 
                        && boggle[i + 1][j + 1] == character) 
                        searchWord(root.Child[K], boggle, 
                                   i + 1, j + 1, 
                                   visited, str + character); 
                    if (checkCurrentSquare(i, j + 1, visited) 
                        && boggle[i][j + 1] == character) 
                        searchWord(root.Child[K], boggle, 
                                   i, j + 1, 
                                   visited, str + character); 
                    if (checkCurrentSquare(i - 1, j + 1, visited) 
                        && boggle[i - 1][j + 1] == character) 
                        searchWord(root.Child[K], boggle, 
                                   i - 1, j + 1, 
                                   visited, str + character); 
                    if (checkCurrentSquare(i + 1, j, visited) 
                        && boggle[i + 1][j] == character) 
                        searchWord(root.Child[K], boggle, 
                                   i + 1, j, 
                                   visited, str + character); 
                    if (checkCurrentSquare(i + 1, j - 1, visited) 
                        && boggle[i + 1][j - 1] == character) 
                        searchWord(root.Child[K], boggle, 
                                   i + 1, j - 1, 
                                   visited, str + character); 
                    if (checkCurrentSquare(i, j - 1, visited) 
                        && boggle[i][j - 1] == character) 
                        searchWord(root.Child[K], boggle, 
                                   i, j - 1, 
                                   visited, str + character); 
                    if (checkCurrentSquare(i - 1, j - 1, visited) 
                        && boggle[i - 1][j - 1] == character) 
                        searchWord(root.Child[K], boggle, 
                                   i - 1, j - 1, 
                                   visited, str + character); 
                    if (checkCurrentSquare(i - 1, j, visited) 
                        && boggle[i - 1][j] == character) 
                        searchWord(root.Child[K], boggle, 
                                   i - 1, j, 
                                   visited, str + character); 
                } 
            } 
            // make current square unvisited 
            visited[i][j] = false; 
        } 
    } 

    static boolean checkCurrentSquare(int i, int j, boolean visited[][]) { 
        return (i >= 0 && i < boardSize && j >= 0
                && j < boardSize && !visited[i][j]); 
    } 

    /* private static void findQU(String boggle[][]) {
        Boolean[][] visited = new Boolean[boardSize][boardSize];
        for(Boolean[] vrow : visited) {
            Arrays.fill(vrow, false);
        }
        String str = "";
        for (int i = 0; i < boardSize; i++) { 
            for (int j = 0; j < boardSize; j++) {
                if(boggle[i][j] == "Qu"){
                    findQUutil(boggle, visited, i, j, str);
                }
            }
        }
    } 
    private static Boolean isQUWord(String str) {
        for (int i = 194000; i < dictionary.length; i++) {
            if (str.equals(dictionary[i])) {
                return true;
            } 
        } return false;
    }
    private static void findQUutil(String[][] boggle, Boolean[][] visited, int i, int j, String str){
        if (!isGenerous) {
            visited[i][j] = true;
        }

        str = str + boggle[i][j];
        if (isQUWord(str)){
            gameDict.add(str);
        }
        for(int row = i-1; row <= i +1 && row < boardSize; row++)
            for(int col = j-1; col <= j+1 && col < boardSize; col++)
                if (row >= 0 && col >= 0 && !visited[row][col])
                    findQUutil(boggle, visited, row, col, str);
        
        str = str.substring(0, str.length() -1);
        visited[i][j] = false;
    } */

}