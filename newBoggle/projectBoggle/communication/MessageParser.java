package projectBoggle.communication;

public class MessageParser {

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