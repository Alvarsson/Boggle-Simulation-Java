package server.Modes;

public class StartMenu {

    public StartMenu(GameModes mode) {
        printMenu(mode);
    }

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
                         "* Menu:                              *\n" +
                         "* [standard] Play standard boggle           *\n" +
                         "* [battle] Play battle-boggle             *\n" +
                         "* [foggle] Play foggle-boggle             *\n" +
                         "* [settings] Settings                       *\n" +
                         "* [quit] Quit                           *\n" +
                         "**************************************\n");
        

        
    }
    private void printSettings(GameModes mode) {
        System.out.println("Settings: \n" + 
                                   "   Board size (" + mode.getBoardSize() + ") : [4x4 | 5x5]\n"+
                                   "   Language (" + mode.getLanguage() + ") : [English | Spanish]\n"+
                                   "   Toggle generous boggle ("+mode.getGenerous()+") : [GenerousBoggle]\n"+
                                   "   Toggle show solution ("+mode.getShowSolution()+") : [ShowSolution]\n"+
                                   "   Number of players ("+mode.getNumberOfPlayers()+") : [#]\n"+
                                   "   Number of seconds per game ("+mode.getGameTime()+") : [# seconds]");
    }
}