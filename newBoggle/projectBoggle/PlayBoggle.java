package projectBoggle;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.script.ScriptException;

import projectBoggle.gameplay.GameController;

class PlayBoggle {
    public static void main(String argv[])
            throws FileNotFoundException, IOException, InterruptedException, ScriptException {
    	GameController.startNewGame();
    }
}
// TODO: Restructure the logic of gameController
// TODO: Make sure QU is counted correctly in the smart search
// TODO: Make sure smart search can handle generous boggle.

// BUGGAR:
//  1. Skickar man in "A" eller icke siffra som antar players i settings så castas NumberFormatException