package game;

import java.util.List;
import game.storage.GameData;

public class Game {
    public List<Player> players;
    private GameData data;

    public Game(List<Player> players, GameData data){
    	this.players = players;
    	this.data = data;
    }

    public Player getPlayer(String playerName) {
        return null;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameData getData(){
    	return data;
    }

}
