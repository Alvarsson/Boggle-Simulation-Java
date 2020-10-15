package server.Modes;
public class BoggleBoards {
    private static String[][] boggle16 = {{"R", "I", "F", "O", "B", "X"},
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

    private static String[][] boggle25 = {{"Qu", "B", "Z", "J", "X", "K"}, 
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

    private static String[][] foggle16 = {{"1", "2", "3", "4", "5", "6"},
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

    public static String[][] getBoggleBoard(String mode, String size) {
            String[] sizeSplit = size.split("x");
            String [][] boggleBoard =  new String[Integer.parseInt(sizeSplit[0])] [Integer.parseInt(sizeSplit[1])];
            if (mode.equals("Standard") || mode.equals("Battle")) {
                if (size.equals("4x4")) {
                    boggleBoard = boggle16;
                } else {
                    boggleBoard = boggle25;
                }
            } else if (mode.equals("Foggle")) {
                boggleBoard = foggle16;
            } 
            return boggleBoard;
    }

}