package server.Modes;

public class GameModes {
    String boardSize;
    String language;
    Boolean generousBoggle = false;
    Boolean showSolution = false;
    int numberOfPlayers;
    int gameTime;
    String mode;

    public GameModes() {
        this.boardSize = "4x4";
        this.language = "English";
        this.generousBoggle = false;
        this.showSolution = false;
        this.numberOfPlayers = 2;
        this.gameTime = 30;
        this.mode = "0";
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
    public void setGameMode(String mode) {
        try {
            this.mode = mode;
        } catch () {
            
        }
    }
    public String getGameMode(){
        return this.mode;
    }


}