package server;

import java.util.ArrayList;

class SmartSearch {
    private String[] dictionary;
    private static int boardSize;
    private int dictLength;
    private char[][] board;
    private static int alphabetSize;

    public SmartSearch(ArrayList<String> dict, char[][] board, int boardSize) {
        this.dictionary = dict.toArray(new String[dict.size()]);
        this.boardSize = boardSize;
        this.alphabetSize = 26;
        this.board = board;
        this.dictLength = dict.size();
        TrieNode root = new TrieNode();
        for (int i = 0; i < dictLength; i++) {
            insert(root, this.dictionary[i]);
        }
        findWords(board, root);
    }

    static class TrieNode {
        // English alphabet size
        
        TrieNode[] Child = new TrieNode[alphabetSize];
        Boolean leaf;

        public TrieNode() { 
            leaf = false; 
            for (int i = 0; i < alphabetSize; i++) 
                Child[i] = null; 
        }
    }

    static void findWords(char boggle[][], TrieNode root) 
    { 
        // Mark all characters as not visited 
        boolean[][] visited = new boolean[boardSize][boardSize]; 
        TrieNode pChild = root; 
  
        String str = ""; 
  
        // traverse all matrix elements 
        for (int i = 0; i < boardSize; i++) { 
            for (int j = 0; j < boardSize; j++) { 
                // we start searching for word in dictionary 
                // if we found a character which is child 
                // of Trie root 
                if (pChild.Child[(boggle[i][j]) - 'A'] != null) { 
                    str = str + boggle[i][j]; 
                    searchWord(pChild.Child[(boggle[i][j]) - 'A'], 
                               boggle, i, j, visited, str); 
                    str = ""; 
                } 
            } 
        } 
    } 

    static void insert(TrieNode root, String Key) 
    { 
        int n = Key.length(); 
        TrieNode pChild = root; 
  
        for (int i = 0; i < n; i++) { 
            int index = Key.charAt(i) - 'A'; 
  
            if (pChild.Child[index] == null) 
                pChild.Child[index] = new TrieNode(); 
  
            pChild = pChild.Child[index]; 
        } 
  
        // make last node as leaf node 
        pChild.leaf = true; 
    } 

    static void searchWord(TrieNode root, char boggle[][], int i, 
                           int j, boolean visited[][], String str) 
    { 
        // if we found word in trie / dictionary 
        if (root.leaf == true) 
            System.out.println(str); 
  
        // If both I and j in  range and we visited 
        // that element of matrix first time 
        if (isSafe(i, j, visited)) { 
            // make it visited 
            visited[i][j] = true; 
  
            // traverse all child of current root 
            for (int K = 0; K < alphabetSize; K++) { 
                if (root.Child[K] != null) { 
                    // current character 
                    char ch = (char)(K + 'A'); 
  
                    // Recursively search reaming character of word 
                    // in trie for all 8 adjacent cells of 
                    // boggle[i][j] 
                    if (isSafe(i + 1, j + 1, visited) 
                        && boggle[i + 1][j + 1] == ch) 
                        searchWord(root.Child[K], boggle, 
                                   i + 1, j + 1, 
                                   visited, str + ch); 
                    if (isSafe(i, j + 1, visited) 
                        && boggle[i][j + 1] == ch) 
                        searchWord(root.Child[K], boggle, 
                                   i, j + 1, 
                                   visited, str + ch); 
                    if (isSafe(i - 1, j + 1, visited) 
                        && boggle[i - 1][j + 1] == ch) 
                        searchWord(root.Child[K], boggle, 
                                   i - 1, j + 1, 
                                   visited, str + ch); 
                    if (isSafe(i + 1, j, visited) 
                        && boggle[i + 1][j] == ch) 
                        searchWord(root.Child[K], boggle, 
                                   i + 1, j, 
                                   visited, str + ch); 
                    if (isSafe(i + 1, j - 1, visited) 
                        && boggle[i + 1][j - 1] == ch) 
                        searchWord(root.Child[K], boggle, 
                                   i + 1, j - 1, 
                                   visited, str + ch); 
                    if (isSafe(i, j - 1, visited) 
                        && boggle[i][j - 1] == ch) 
                        searchWord(root.Child[K], boggle, 
                                   i, j - 1, 
                                   visited, str + ch); 
                    if (isSafe(i - 1, j - 1, visited) 
                        && boggle[i - 1][j - 1] == ch) 
                        searchWord(root.Child[K], boggle, 
                                   i - 1, j - 1, 
                                   visited, str + ch); 
                    if (isSafe(i - 1, j, visited) 
                        && boggle[i - 1][j] == ch) 
                        searchWord(root.Child[K], boggle, 
                                   i - 1, j, 
                                   visited, str + ch); 
                } 
            } 
  
            // make current element unvisited 
            visited[i][j] = false; 
        } 
    } 

    static boolean isSafe(int i, int j, boolean visited[][]) 
    { 
        return (i >= 0 && i < boardSize && j >= 0
                && j < boardSize && !visited[i][j]); 
    } 


}