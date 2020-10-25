package boggle.gameplay.gridsolvers;
import java.util.ArrayList;

/**
 * <h1>Find all words possible</h1>
 * Used to quickly find all possible words on boggle board.
 * 
 * @author Axel Alvarsson
 * @version 1.0
 * @since 2020-10-25
 */
public class SmartSearch {
    private static String[] dictionary;
    private static int boardSize;
    private int dictLength;
    private String[][] board;
    private static int alphabetSize;
    private static Boolean isGenerous;
    private static ArrayList<String> gameDict = new ArrayList<String>();
    /**
     * Initializes the used arguments for search using Trie Node BST algorithm.
     */
    public SmartSearch(ArrayList<String> dict, String[][] board, int boardSize, Boolean isGenerous) {
        this.dictionary = dict.toArray(new String[dict.size()]);
        this.isGenerous = isGenerous;
        this.boardSize = boardSize;
        this.alphabetSize = 26;
        this.board = board;
        this.dictLength = dict.size();
        this.gameDict.clear();

        TrieNode root = new TrieNode();

        for (int i = 0; i < this.dictLength; i++) {
            insert2(root, this.dictionary[i]);
        }
        findWords(this.board, root);  
    }
    /**
     * Returns the dictionary with found words.
     * @return
     */
    public ArrayList<String> getCurrentDict() {
        return gameDict;
    }
    /**
     * Trie Node class and initializer
     */
    private static class TrieNode {
        TrieNode[] Child = new TrieNode[alphabetSize];
        Boolean leaf;

        public TrieNode() { 
            leaf = false; 
            for (int i = 0; i < alphabetSize; i++) 
                Child[i] = null; 
        }
    }

    private static void findWords(String[][] boggle, TrieNode root){
        boolean[][] visited = new boolean[boardSize][boardSize];
        TrieNode rootChild = root;
        String word = "";

        for (int i = 0; i < boardSize; i++) { 
            for (int j = 0; j < boardSize; j++) { 
                Character ch = boggle[i][j].charAt(0);
                if (rootChild.Child[(ch) - 'A'] != null) {
                    word = word + boggle[i][j];
                    searchWord(rootChild.Child[ch -'A'], boggle, i, j, visited, word);
                    word = "";
                }
            }
        }
    }
    /**
     * Method to insert key into tree if not already present.
     * If key is prefix of trie node, marks as leaf node.
     * @param root
     * @param Key
     */
    private static void insert2(TrieNode root, String Key) {
        int maxSize = Key.length();
        TrieNode rootChild = root;
        for (int i = 0; i < maxSize; i ++) {
            int index = Key.charAt(i) - 'A';
            if (rootChild.Child[index] == null)
                rootChild.Child[index] = new TrieNode();
            rootChild = rootChild.Child[index];
        }
        rootChild.leaf = true;
    }
    /**
     * Searches around each valid letter
     * recursivly on the board.
     * 
     * @param root
     * @param boggle
     * @param i
     * @param j
     * @param visited
     * @param str
     */
    private static void searchWord(TrieNode root, String[][] boggle, int i, int j, boolean visited[][], String str) {
        if (root.leaf == true)
            gameDict.add(str.toUpperCase());
        
        if (checkCurrentSquare(i, j, visited)) {
            if (!isGenerous) {visited[i][j] = true;}
            
            String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "QU", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

            for(int k = 0; k < alphabet.length; k++) {
                if (root.Child[k] != null) {
                    if (checkCurrentSquare(i + 1, j + 1, visited)  //1
                        && boggle[i +1][j + 1].equals(alphabet[k])) {
                            searchWord(root.Child[k], boggle,
                                         i + 1, j + 1, visited,
                                         str + alphabet[k]); 
                        }
                    if (checkCurrentSquare(i, j + 1, visited) //2
                        && boggle[i][j + 1].equals(alphabet[k])) {
                            searchWord(root.Child[k], boggle,
                                         i, j + 1, visited,
                                         str + alphabet[k]); 
                        }
                    if (checkCurrentSquare(i - 1, j + 1, visited) //3
                        && boggle[i - 1][j + 1].equals(alphabet[k])) {
                            searchWord(root.Child[k], boggle,
                                         i - 1, j + 1, visited,
                                         str + alphabet[k]); 
                        }
                    if (checkCurrentSquare(i + 1, j, visited) //4
                        && boggle[i + 1][j].equals(alphabet[k])) {
                            searchWord(root.Child[k], boggle,
                                         i + 1, j, visited,
                                         str + alphabet[k]); 
                        }
                    if (checkCurrentSquare(i + 1, j - 1, visited) //5
                        && boggle[i + 1][j - 1].equals(alphabet[k])) {
                            searchWord(root.Child[k], boggle,
                                         i + 1, j - 1, visited,
                                         str + alphabet[k]);
                        }
                    if (checkCurrentSquare(i, j - 1, visited) // 6
                        && boggle[i][j - 1].equals(alphabet[k])) {
                            searchWord(root.Child[k], boggle,
                                         i, j - 1, visited,
                                         str + alphabet[k]);
                        }
                    if (checkCurrentSquare(i - 1, j - 1, visited) // 7
                        && boggle[i - 1][j - 1].equals(alphabet[k])) {
                            searchWord(root.Child[k], boggle,
                                         i - 1, j - 1, visited,
                                         str + alphabet[k]);
                        }
                    if (checkCurrentSquare(i - 1, j, visited) // 8
                        && boggle[i - 1][j].equals(alphabet[k])) {
                            searchWord(root.Child[k], boggle,
                                         i - 1, j, visited,
                                         str + alphabet[k]);
                        }
                } 
            }
            visited[i][j] = false;
        } 
    }
    /**
     * This methods tests the current square in range
     * and visited status.
     * @param i
     * @param j
     * @param visited
     * @return
     */
    static boolean checkCurrentSquare(int i, int j, boolean visited[][]) { 
        return (i >= 0 && i < boardSize && j >= 0
                && j < boardSize && !visited[i][j]); 
    } 

}