package server;

public class StartMenu {
    //GameModes mode = new GameModes();

    public StartMenu(GameModes mode) {
        printMenu(mode);
    }

    private void printMenu(GameModes mode) {
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
                         "* [1] Play standard boggle           *\n" +
                         "* [2] Play battle-boggle             *\n" +
                         "* [3] Play foggle-boggle             *\n" +
                         "* [4] Settings                       *\n" +
                         "* [!] Quit                           *\n" +
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