package remake;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javax.script.*;

/*KNOWN BUGS:
- Cant change time of game.
- Cant run two games in a row. Only possible if you restart twice now
- Foggle can only run in 4x4, even if other size has been set.
- If you choose gamemode 1, then 2, then 3. When it starts, several identical gameboards gets printed.

*/




//Templates

// Function:
//
// Arguments:
//
// Returns:
//
// Local variables:
//
// Purpose:
//
// Description:
//
// Change:

// Class:
//
// Purpose:
//
// Description:
//
// Change:




public class VarietyBoggle {

    public String[][] boggle16 = {{"R", "I", "F", "O", "B", "X"},
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
    
    public String[][] boggle25 = {{"Qu", "B", "Z", "J", "X", "K"}, 
                                  {"T", "O", "U", "O", "T", "O"}, 
                                  {"O", "V", "W", "R", "G", "R"}, 
                                  {"A", "A", "A", "F", "S", "R"},
                                  {"A", "U", "M", "E", "E", "G"},
                                  {"H", "H", "L", "R", "D", "O"}, 
                                  {"N", "H", "D", "T", "H", "O"}, 
                                  {"L", "H", "N", "R", "O", "D"}, 
                                  {"A", "F", "A", "I", "S", "R"}, 
                                  {"Y", "I", "F", "A", "S", "R"},
                                  {"T", "E", "L", "P", "C", "I"}, 
                                  {"S", "S", "N", "S", "E", "U"}, 
                                  {"R", "I", "Y", "P", "R", "H"}, 
                                  {"D", "O", "R", "D", "L", "N"}, 
                                  {"C", "C", "W", "N", "S", "T"},
                                  {"T", "T", "O", "T", "E", "M"}, 
                                  {"S", "C", "T", "I", "E", "P"}, 
                                  {"E", "A", "N", "D", "N", "N"}, 
                                  {"M", "N", "N", "E", "A", "G"}, 
                                  {"U", "O", "T", "O", "W", "N"},
                                  {"A", "E", "A", "E", "E", "E"}, 
                                  {"Y", "I", "F", "P", "S", "R"}, 
                                  {"E", "E", "E", "E", "M", "A"}, 
                                  {"I", "T", "I", "T", "I", "E"}, 
                                  {"E", "T", "I", "L", "I", "C"}}; 
    
    // Dice-list found on https://boardgamegeek.com/boardgame/23771/foggle
    public String[][] foggle16 = {{"1", "2", "3", "4", "5", "6"},
                                  {"1", "2", "3", "4", "5", "6"},
                                  {"1", "2", "3", "4", "5", "6"},
                                  {"1", "2", "3", "4", "5", "6"},
                                  {"1", "2", "3", "4", "5", "6"},
                                  {"1", "2", "3", "4", "9", "0"},
                                  {"1", "2", "3", "4", "9", "0"},
                                  {"5", "6", "7", "8", "9", "0"},
                                  {"5", "6", "7", "8", "9", "0"},
                                  {"5", "6", "7", "8", "9", "0"},
                                  {"1", "2", "7", "8", "9", "8"},
                                  {"1", "2", "7", "8", "9", "8"},
                                  {"1", "2", "7", "8", "9", "8"},
                                  {"3", "4", "5", "6", "7", "8"},
                                  {"3", "4", "5", "6", "7", "8"},
                                  {"1", "2", "4", "7", "8", "0"}};
    
    public ArrayList<String> dictionary = new ArrayList<String>();
    public ArrayList<String> foundWords;
    public boolean foundInBoggleBoard;
    public ArrayList<Player> players = new ArrayList<Player>();
    String[][] currentBoggle = randomBoggle(boggle16);
    public int timeSeconds = 30;
    public boolean run = false;
    public ServerSocket aSocket;
    public boolean generousBoggle = false;
    public static ArrayList<String> battleWords = new ArrayList<String>();
    public ScriptEngineManager mgr = new ScriptEngineManager();
    public ScriptEngine engine = mgr.getEngineByName("JavaScript");
    
    // Function: main
    //
    // Arguments: String argv[]
    //
    // Returns:
    // 
    // Purpose: Starting the game.
    //
    // Description: Creates a new object of the VarietyBoggle class.
    //
    // Change: Put in own startfile containing only the necessary.
    public static void main(final String argv[]) throws Exception {
        new VarietyBoggle();
    }
    
    // Function: randomBoggle
    //
    // Arguments: String[][] boggleDie
    //
    // Returns: Square grid String[][]
    //
    // Local variables:
    //      returnRow, 
    //      returnColumn,
    //
    // Purpose: Randomizes the template grid and returns it.
    //
    // Description: First calculates the size of the grid 16. Next it inits. a new Random object rnd. 
    //              Next we init. returnBoggle, an square grid of String[size][size] to variable returnBoggle.
    //              Next we define the variable rows as a list of the boggleDie grid.
    //              Next we shuffle the rows variable.
    //              Finally we loop over the list rows and inserts random list element between 0-5.
    //              In the loop give returnColumn to returnColumn+1 if returnColumn < size-1, else set it too 0.
    //              In loop set returnRow to returnRow+1 if returnColumn == 0, else set it to returnRow
    //              Lastly return the randomized String[][] returnBoggle
    //
    // Change: Maybe split this into different functions, one for calculating the size of the grid,
    //         one for a simpler randomized String[][] of the template grid.

    public String[][] randomBoggle(final String[][] boggleDie) {
        final int size = (int) Math.sqrt(boggleDie.length);
        int returnRow=0, returnColumn = 0;
        final Random rnd = new Random();
        final String[][] returnBoggle = new String[size][size];
        final List<String[]> rows = Arrays.asList(boggleDie);
        Collections.shuffle(rows);
        for(final String[] row : rows) {
            returnBoggle[returnRow][returnColumn] = row[rnd.nextInt(6)];
            returnColumn=(returnColumn<(size-1)?returnColumn+1:0);
            returnRow=(returnColumn==0?returnRow+1:returnRow);
        }
        return returnBoggle;
    }
    
    // Function: findWordsUtil
    //
    // Arguments: final String[][] boggle, final boolean[][] visited, final int(i,j), String str, final ArrayList<String> usedDictionary
    //
    // Returns: None
    //
    // Local variables: 
    //      boolean visited,
    //      String str,
    //
    // Purpose: Checks if word is connected and is not already found
    //
    // Description: First set visited coordinates to true. Next set str to itself + capitalized letter of the coordinates
    //              [i][j] on the boggle board.
    //              Next check if the used Dictionary containt the string, if so it checks if str already is found, if not add
    //              str to foundWords list.
    //              Next the functions loops through the adjacent cells of the chosen letter and if cell is within bounds and not 
    //              already visited we call fundWordsUtil again.
    //              Next, after loop, erase character from str and mark as visited and set current cell as false(not visited).
    //
    // Change: To many arguments for this function to be simple.
    //         Make the travesal of adjacent cells a function in itself.
    //         Recusive call in the adjacency check is implemented redundantly(?) I believe. Make it its own function.
    //         

    public void findWordsUtil(final String[][] boggle, final boolean[][] visited, final int i, final int j, String str, final ArrayList<String> usedDictionary) { 
        visited[i][j] = true; 
        str = str + boggle[i][j].toUpperCase(); 
        if (usedDictionary.contains(str)) {
            if(!foundWords.contains(str))
                foundWords.add(str); 
        }
        // Traverse 8 adjacent cells of boggle[i][j] 
        for (int row = i - 1; row <= i + 1 && row < boggle.length; row++) 
            for (int col = j - 1; col <= j + 1 && col < boggle.length; col++) 
                if (row >= 0 && col >= 0 && !visited[row][col]) 
                    findWordsUtil(boggle, visited, row, col, str, usedDictionary); 
  
        // Erase current character from string and mark visited 
        // of current cell as false 
        str = "" + str.charAt(str.length() - 1); 
        visited[i][j] = false; 
    } 
  
    // Function: findWords
    //
    // Arguments: final String[][] boggle, final ArrayList<String> dictionary
    //
    // Returns: String[]
    //
    // Local variables:
    //
    // Purpose:
    //
    // Description:
    //
    // Change:

    // Prints all words present in dictionary. 
    public String[] findWords(final String[][] boggle, final ArrayList<String> dictionary) { 
        // Mark all characters as not visited 
        final boolean visited[][] = new boolean[boggle.length][boggle.length]; 
        final String str = ""; 
        foundWords = new ArrayList<String>();
  
        for (int i = 0; i < boggle.length; i++) 
            for (int j = 0; j < boggle.length; j++) 
                findWordsUtil(boggle, visited, i, j, str, dictionary); 
        final String[] words = new String[foundWords.size()]; 
        for(int i=0; i<foundWords.size(); i++) {
            words[i] = foundWords.get(i);
        }
        return words;
    }
    
    public String checkWord(final String[][] boggle, String word) {
        word = word.toUpperCase();
        foundInBoggleBoard = false;
        boolean validWord = false;
        boolean foggle = false;
        try {
            Integer.parseInt(boggle[0][0]);
            foggle = true;
            final String[] expressions = word.split("=");
            validWord = (engine.eval(expressions[0])) == (engine.eval(expressions[1]));
        } catch (final Exception e) {            
            if(dictionary.contains(word))
                validWord = true;
        }            
        if(validWord) {
            //The word exists in the dictionary, check if it exists on the board
            word = word.replaceAll("QU", "Q"); //Treat as one character in word due to Boggle dice
            word = word.replaceAll("[^a-zA-Z0-9]", ""); //For Foggle - just keep numbers
            final boolean[][] visited = new boolean[boggle.length][boggle.length];
            boolean found = false;
            for(int i=0; i< boggle.length; i++) {
                for(final boolean[] vrow : visited) {Arrays.fill(vrow, false);}
                for(int j=0; j<boggle.length; j++) {
                    if(boggle[i][j].startsWith(word.substring(0,1))) {
                        //Check if the word exists on the Boggle board
                        //Start with matching positions on the boggle board 
                        found = search(boggle, word, i, j, 1, visited);
                    }
                }
            }
            return (found?"OK":"NOT OK");
        } else {
            if(foggle)
                return "Not a valid expression";
            return "Not in the dictionary";
        }
    }
    
    public boolean search(final String[][] boggle, final String word, final int i, final int j, final int matches, final boolean[][] visited) {
        final int[] dirx = { -1, 0, 0, 1 , -1,  1, 1, -1}; //8 directions including diagonals
        final int[] diry = { 0, -1, 1, 0 , -1, -1, 1,  1};
        if(!generousBoggle)
            visited[i][j] = true;
        final int size=boggle.length;
        if(matches>=word.length()) {foundInBoggleBoard=true;} //The word was found on the boggle board
        for(int z=0; z<8; z++) {
            if(((i+dirx[z])>=0 && (i+dirx[z])<size) && ((j+diry[z])>=0 && (j+diry[z])<size) && (!visited[i+dirx[z]][j+diry[z]]) && matches<word.length()) {
                if(word.substring(matches,matches+1).equals(boggle[i+dirx[z]][j+diry[z]])) {
                    search(boggle, word, i+dirx[z], j+diry[z], matches+1, visited);
                }
            }
            if(foundInBoggleBoard) return true; //some branch found the word in the boggleboard
        }
        return false; //The word was not found on the boggle board
    }
    
    public void server(final int numberPlayers) throws Exception {
        players.add(new Player(0, null, null, null)); //add this instance as a player
        //Open for connections if there are online players
        if(numberPlayers>1)
            aSocket = new ServerSocket(2048);
        for(int i=1; i<numberPlayers; i++) {
            final Socket connectionSocket = aSocket.accept();
            final ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            final ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            players.add(new Player(i, connectionSocket, inFromClient, outToClient));
            System.out.println("Connected to player " + i);
            outToClient.writeObject("You connected to the server as player " + i);
        }
        
    }
    
    public void bogglePlay(final Player aPlayer, final String playMode) {
        battleWords.clear();
        final String info = "PlayerID: " + aPlayer.playerID + ", Game Mode: " + playMode + " Boggle, Time Limit: " + timeSeconds + " seconds";
        aPlayer.sendMessage(info);
        aPlayer.sendMessage(currentBoggle);
        while(run) {
            final String word = aPlayer.readMessage().toUpperCase();
            if(run) { //The timer may have already ended while waiting for new input
                if(aPlayer.words.contains(word) || (playMode.equals("Battle") && battleWords.contains(word)))
                    aPlayer.sendMessage("Already submitted");
                else {
                    final String check = checkWord(currentBoggle, word);
                    if(check.equals("OK")) {
                        battleWords.add(word);
                        aPlayer.words.add(word);
                        if(playMode.equals("Battle")) {
                            for(final Player pl : players) {
                                if(!pl.equals(aPlayer)) {
                                    pl.sendMessage("Player " + aPlayer.playerID + " played " + word);                                
                                }
                            }                            
                        }
                    }
                    aPlayer.sendMessage(check);
                }
                aPlayer.sendMessage(currentBoggle);
            }
        }
    }
    
    // Function: VariatyBoggle
    //
    // Arguments: None
    //
    // Returns: None
    //
    // Local variables:
    //      boardsize, String set deafault as "4x4"
    //      language, String set default as "English"
    //      showSolution, boolean set default as false
    //      numberOfPLayers, int set default as 2
    //      playmode, String set default as "0"
    //
    // Purpose:
    //
    // Description:
    //
    // Change:

    public VarietyBoggle() throws Exception {
    	String boardsize = "4x4";
    	String language = "English";
    	boolean showSolution = false;
    	int numberOfPlayers = 2;
    	String playmode = "0";
        

        try {
            final FileReader fileReader = new FileReader("CollinsScrabbleWords2019.txt");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                dictionary.add(line);
            }
            bufferedReader.close();           
        } catch (final IOException e) {}

        while(!playmode.equals("!")) {
        System.out.println("**************************************\n" +
                         "*      Welcome to VarietyBoggle      *\n" +
                         "**************************************\n" +
                         "* Current settings:                  *\n" +
                         "*    Board size: " + boardsize + "                 *\n" +
                         "*    Language: "+ language + "               *\n" + 
                         "*    Generous boggle: " + (generousBoggle?"on ":"off") + "            *\n"+
                         "*    Show solution: " + (showSolution?"on ":"off") + "              *\n"+
                         "*    Number of players: " + numberOfPlayers + "            *\n"+
                         "*    Number of seconds per game: " + timeSeconds + "     ".substring(0, 4-(String.valueOf(timeSeconds).length())) +"*\n"+
                         "**************************************\n" +
                         "* Menu:                              *\n" +
                         "* [1] Play standard boggle           *\n" +
                         "* [2] Play battle-boggle             *\n" +
                         "* [3] Play foggle-boggle             *\n" +
                         "* [4] Settings                       *\n" +
                         "* [?] Test (remove for final product)*\n" +
                         "* [!] Quit                           *\n" +
                         "**************************************\n");
            //TODO: CLOSE THE SCANNER
            final Scanner in = new Scanner(System.in);
            playmode=in.nextLine();
            if(playmode.equals("1") || playmode.equals("2") || playmode.equals("3")) { //Standard Boggle || Battle Boggle || Foggle
                String mode;
                if(playmode.equals("1"))
                    mode = "Standard";
                else if(playmode.equals("2"))
                    mode = "Battle";
                else
                    mode = "Foggle";
                if(boardsize.equalsIgnoreCase("4x4"))
                    currentBoggle = randomBoggle(boggle16);
                if(boardsize.equalsIgnoreCase("5x5"))
                    currentBoggle = randomBoggle(boggle25);
                if(mode.equals("Foggle"))
                    currentBoggle = randomBoggle(foggle16);
                server(numberOfPlayers);
                run = true;
                final ExecutorService threadpool = Executors.newFixedThreadPool(players.size());
                for(int i=0; i<players.size(); i++) {
                    final Player aPlayer = players.get(i);
                    final Runnable task = new Runnable() {
                        @Override
                        public void run() {
                            bogglePlay(aPlayer, mode);
                        }
                    };
                    threadpool.execute(task);
                }
                threadpool.awaitTermination(timeSeconds, TimeUnit.SECONDS);
                run = false; threadpool.shutdownNow();
                
                //Calculate score for each player and select the winner                
                String result = "\n"; 
                for(final Player pl : players) {
                    result += "Player " + pl.playerID + " got " + pl.calculateScore() + " points\n";
                }
                for(final Player aPlayer : players) {
                    aPlayer.sendMessage(result);
                    aPlayer.close();
                }                
                players.clear(); aSocket.close();
            }
            else if(playmode.equals("4")) {
                System.out.println("Settings: \n" + 
                                   "   Board size (" + boardsize + ") : [4x4 | 5x5]\n"+
                                   "   Language (" + language + ") : [English | Spanish]\n"+
                                   "   Toggle generous boggle ("+generousBoggle+") : [GenerousBoggle]\n"+
                                   "   Toggle show solution ("+showSolution+") : [ShowSolution]\n"+
                                   "   Number of players ("+numberOfPlayers+") : [#]\n"+
                                   "   Number of seconds per game ("+timeSeconds+") : [# seconds]");
                final String settingChoice = in.nextLine();
                if(settingChoice.equalsIgnoreCase("4x4") || settingChoice.equalsIgnoreCase("5x5")) {
                  boardsize=settingChoice;
                } else if(settingChoice.equalsIgnoreCase("English") || settingChoice.equalsIgnoreCase("Spanish")) {
                  language = settingChoice;
                } else if(settingChoice.equalsIgnoreCase("GenerousBoggle")) {
                  generousBoggle = !generousBoggle;
                } else if(settingChoice.equalsIgnoreCase("ShowSolution")) {
                  showSolution = !showSolution;
                } else if(settingChoice.endsWith("seconds")) {
                    try {
                        timeSeconds = Integer.parseInt(settingChoice.substring(0, settingChoice.indexOf(" ")));
                    } catch (final NumberFormatException e) {}
                }
                try {
                    numberOfPlayers = Integer.parseInt(settingChoice);
                } catch (final NumberFormatException e) {}
            }            
            else if(playmode.equals("?")) { //remove for final release
                final String[][] newBoggle = {{"U", "R", "Qu"}, {"R", "L", "I"}, {"D", "U", "Z"}}; //Test for finding all words
                System.out.println(Player.printBoggle(newBoggle));        
                final String[] words = findWords(newBoggle, dictionary);
                System.out.println(Arrays.toString(words));         
            }
        }
    }
}