package boggle.communication;

/**
 * <h1>Parsing board message to players</h1>
 * 
 * @author Axel Alvarsson
 * @version 1.0
 * @since 2020-10-25
 */
public class MessageParser {

    /**
     * Method to parse the board message to players.
     * @param currentBoard
     * @return
     */
    public static String parseBoggle(String[][] currentBoard) {
        String boggleBoard = "";
        for(String[] row : currentBoard){
            for(String column : row) {
                boggleBoard += column + (column.equals("Qu")?" ":"  ");
            }
            boggleBoard += "\n";
        }
        return boggleBoard;
    }

}