package testing;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javax.script.*;
class Test {
    public static String[][] boggle16 = {{"R", "I", "F", "O", "B", "X"},
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
    public static void main(String []args){
        System.out.println("Hello World");
        randomBoggle(boggle16);
        
    }
    public static void randomBoggle(final String[][] boggle16) {
        final int size = (int) Math.sqrt(boggle16.length);
        int returnRow=0, returnColumn = 0;
        final Random rnd = new Random();
        final String[][] returnBoggle = new String[size][size];
        final List<String[]> rows = Arrays.asList(boggle16); //put into list for shuffleing 
        Collections.shuffle(rows); //shuffles the list rows.
        for(final String[] row : rows) {
            returnBoggle[returnRow][returnColumn] = row[rnd.nextInt(6)];
            System.out.println(returnBoggle[returnRow][returnColumn]);
            returnColumn=(returnColumn<(size-1)?returnColumn+1:0);
            returnRow=(returnColumn==0?returnRow+1:returnRow);
        }
    }
    


}