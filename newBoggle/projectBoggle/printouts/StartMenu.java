package projectBoggle.printouts;

import projectBoggle.modes.GameModes;

public class StartMenu {

    public static void printMenu(GameModes mode) {
        //TODO: Get default values to the Start Menu
        System.out.println("**************************************\n" +
                         "*      Welcome to VarietyBoggle      *\n" +
                         "**************************************\n" +
                         "* Current settings:                  *\n" +
                         "*    Board size: " + mode.getBoardSize() + "                 *\n" +
                         "*    Language: "+ mode.getLanguage() + "               *\n" + 
                         "*    Generous boggle: " + (mode.getGenerous()?"on ":"off")+ "            *\n"+
                         "*    Show solution: " + (mode.getShowSolution()?"on ":"off") + "              *\n"+
                         "*    Number of players: " + mode.getNumberOfPlayers() + "            *\n"+
                         "*    Number of seconds per game: " + mode.getGameTime() + "     ".substring(0, 4-(String.valueOf(mode.getGameTime()).length())) +"*\n"+
                         "**************************************\n" +
                         getMenu(mode));   
    }
    
    private static String getMenu(GameModes mode) {
        String menu = "* Menu:                              \n";
        for (String game: mode.getAllModes()) {
            menu += "* ["+ game + "] Play standard "+ game +"      \n";
        }
        return menu += "* [Settings] Settings                \n" +
        "* [Quit] Quit                        \n" +
        "**************************************\n";
    }

    public static void printSettings(GameModes mode) {
        System.out.println("Settings: \n" + 
                                   "   Board size (" + mode.getBoardSize() + ") : [4x4 size | 5x5 size]\n"+
                                   "   Language (" + mode.getLanguage() + ") : [english lang | spanish lang]\n"+
                                   "   Toggle generous boggle ("+mode.getGenerous()+") : [once dice | generous dice ]\n"+
                                   "   Toggle show solution ("+mode.getShowSolution()+") : [no solution | show solution]\n"+
                                   "   Number of players ("+mode.getNumberOfPlayers()+") : [# players]\n"+
                                   "   Number of seconds per game ("+mode.getGameTime()+") : [# seconds]");
    }
}
