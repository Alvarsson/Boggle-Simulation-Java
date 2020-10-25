package boggle.printouts;
import boggle.modes.GameModes;
/**
 * <h1>Print out class</h1>
 * Hold the methods for print out statements.
 * @author Axel Alvarsson
 * @version 1.0
 * @since 2020-10-25
 */
public class StartMenu {
    /**
     * Method for printing menu with current game mode settings.
     * @param mode
     */
    public static void printMenu(GameModes mode) {
    
        System.out.println("**************************************\n" +
                        "*      Welcome to VarietyBoggle      *\n" +
                        "**************************************\n" +
                        "* Current settings:                  *\n" +
                        "*    Board size: " + mode.getBoardSize() + "                 *\n" +
                        "*    Language: "+ mode.getLanguage() + "               *\n" + 
                        "*    Generous boggle: " + (mode.getGenerous()?"on ":"off")+ "            *\n"+
                        "*    Number of players: " + mode.getNumberOfPlayers() + "            *\n"+
                        "*    Number of seconds per game: " + mode.getGameTime() + "     ".substring(0, 4-(String.valueOf(mode.getGameTime()).length())) +"*\n"+
                        "**************************************\n" +
                        getMenu(mode));   
    
    }
    /**
     * Method to get the variaty of implemented modes into the
     * menu printout. 
     * @param mode
     * @return
     */
    private static String getMenu(GameModes mode) {
        String menu = "* Menu:                              \n";
        for (String game: mode.getAllModes()) {
            menu += "* ["+ game + "] Play standard "+ game +"      \n";
        }
        return menu += "* [settings] Settings                \n" +
        "* [words] Last game wordList?                        \n" +
        "* [quit] Quit                        \n" +
        "**************************************\n";
    }
    /**
     * Method to print out the settings possible to the user.
     * @param mode
     */
    public static void printSettings(GameModes mode) {
        System.out.println("Settings: \n" + 
                                   "   Board size (" + mode.getBoardSize() + ") : [4x4 size | 5x5 size]\n"+
                                   "   Language (" + mode.getLanguage() + ") : [english lang | spanish lang]\n"+
                                   "   Toggle generous boggle ("+mode.getGenerous()+") : [once dice | generous dice ]\n"+
                                   "   Number of players ("+mode.getNumberOfPlayers()+") : [# players]\n"+
                                   "   Number of seconds per game ("+mode.getGameTime()+") : [# seconds]");
    }
}
