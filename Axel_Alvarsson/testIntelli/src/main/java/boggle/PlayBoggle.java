package boggle;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.script.ScriptException;
import boggle.gameplay.GameController;
/**
 * <h1> Start serverside Boggle</h1>
 * 
 * @author Axel Alvarsson
 * @version 1.0
 * @since 2020-10-25
 */
public class PlayBoggle {
    public static void main(String argv[])
            throws FileNotFoundException, IOException, InterruptedException, ScriptException {
    	GameController.startNewGame();
    }
}

