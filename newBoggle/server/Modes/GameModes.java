package server.Modes;

import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class GameModes  {
    String boardSize;
    String language;
    Boolean generousBoggle = false;
    Boolean showSolution = false;
    int numberOfPlayers;
    int gameTime;
    String mode;
    ArrayList<String> gameModes = new ArrayList();

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

    // SETTERS AND GETTERS
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
    public void setShowSolution(Boolean onOff) {
        this.showSolution = onOff;
    }
    public Boolean getShowSolution() {
        return this.showSolution;
    }
    public void setNumberOfPlayers(int number) {
        this.numberOfPlayers = number;
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
    //TODO: set game mode
    public String getGameMode(){
        return this.mode;
    }
    public ArrayList<String> getAllModes() {
        return gameModes;
    }

    // instead return a list so we can ask for any of the possible settings.
    public ArrayList<String> loadJsonSettings(String load) throws FileNotFoundException, JSONException, IOException  {
        String games = "/Users/axelalvarsson/Dropbox/Skola/D7032E Programvaruteknik/newBoggle/server/Modes/games.json";
        ArrayList<String> settingArray = new ArrayList();
        try (FileReader in = new FileReader(games)){
            
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

    // make so that only loads asked for gamemode
    public void loadJsonGameMode(String load) throws FileNotFoundException, JSONException, IOException  {
        String games = "/Users/axelalvarsson/Dropbox/Skola/D7032E Programvaruteknik/newBoggle/server/Modes/games.json";
        try (FileReader in = new FileReader(games)){
            
            JSONTokener tokener = new JSONTokener(in);
            JSONObject obj = new JSONObject(tokener);
            JSONArray names = obj.getJSONArray(load);
            for (int i = 0; i < names.length(); i++) {
                gameModes.add(names.get(i).toString());
            }
            
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Games file not found. Exception thrown: " + e);
        } catch (JSONException e) {
            throw new JSONException("Json error. Exception thrown: " + e);
        } catch (IOException e) {
            throw new IOException("IO reading error. Exception thrown: " + e);
        }
    }


}