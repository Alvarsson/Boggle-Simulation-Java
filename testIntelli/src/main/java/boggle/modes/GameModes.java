package boggle.modes;

import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
/**
 * <h1>Current game mode class</h1>
 * This contains the active game mode settings and methods to manipulate them.
 * 
 * @author Axel Alvarsson
 * @version 1.0
 * @since 2020-10-25
 */
public class GameModes  {
    String boardSize;
    String language;
    Boolean generousBoggle = false;
    Boolean showSolution = false;
    int numberOfPlayers;
    int gameTime;
    String mode;
    ArrayList<String> gameModes = new ArrayList<String>();

    public GameModes() throws FileNotFoundException, IOException{
        this.boardSize = "4x4";
        this.language = "english";
        this.generousBoggle = false;
        this.showSolution = false;
        this.numberOfPlayers = 2;
        this.gameTime = 30;
        this.mode = "0";
        this.gameModes = loadJsonSettings("gameModes");
    }
    /*
    * Getters and setters for game mode values.
    */
    public void setBoardSize(String size) {
        this.boardSize = size;
    }
    public String getBoardSize() {
        return this.boardSize;
    }
    public void setLanguage(String language) {
        this.language = language;

    }
    public String getLanguage() {
        return this.language;
    }
    public void setGenerous(Boolean onOff) {
        this.generousBoggle = onOff;
    }
    public Boolean getGenerous() {
        return this.generousBoggle;
    }
    public void setNumberOfPlayers(int number) {
        if (number > 1) {
            this.numberOfPlayers = number;
        } else {
            System.out.println("Must be 2 or more players.");
        }
        
    }
    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }
    public void setGameTime(int time) {
        this.gameTime = time;
    }
    public int getGameTime() {
        return this.gameTime;
    }
    public void setGameMode(String mode) {
        this.mode = mode;
    }
    public String getGameMode(){
        return this.mode;
    }
    public ArrayList<String> getAllModes() {
        return gameModes;
    }
    /**
     * Method for loading setting options from Json file.
     * Using Json parsing tools to read .json file correctly.
     * Returning the asked for list of settings.
     * @param load
     * @return ArrayList
     * @throws FileNotFoundException
     * @throws JSONException
     * @throws IOException
     */
    public ArrayList<String> loadJsonSettings(String load) throws FileNotFoundException, JSONException, IOException  {
        String games = "games.json";
        ArrayList<String> settingArray = new ArrayList<String>();
        
        try {
            FileReader in = new FileReader(games);
            JSONTokener tokener = new JSONTokener(in);
            JSONObject obj = new JSONObject(tokener);
            JSONArray names = obj.getJSONArray(load);
            for (int i = 0; i < names.length(); i++) {
                settingArray.add(names.get(i).toString());
            }
            return settingArray;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Games file not found. Exception thrown: " + e);
        } catch (JSONException e) {
            throw new JSONException("Json error. Exception thrown: " + e);
        } catch (IOException e) {
            throw new IOException("IO reading error. Exception thrown: " + e);
        }
    }
    /**
     * Method to load Game mode settings from .json file.
     * Using Json parsing tools to read .json file correctly.
     * @param gameMode
     * @param setting
     * @return
     * @throws FileNotFoundException
     * @throws JSONException
     * @throws IOException
     */
    public ArrayList<String> loadJsonGameMode(String gameMode, String setting) throws FileNotFoundException, JSONException, IOException  {
        String games = "games.json";
        ArrayList<String> modeSetting = new ArrayList<String>();
        try {
            FileReader in = new FileReader(games);
            
            JSONTokener tokener = new JSONTokener(in);
            JSONObject obj = new JSONObject(tokener);
            JSONArray array = obj.getJSONObject(gameMode).getJSONArray(setting);
            for (int i = 0; i < array.length(); i++) {
                modeSetting.add(array.get(i).toString());
            }
            return modeSetting;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Games file not found. Exception thrown: " + e);
        } catch (JSONException e) {
            throw new JSONException("Json error. Exception thrown: " + e);
        } catch (IOException e) {
            throw new IOException("IO reading error. Exception thrown: " + e);
        }
    }
}