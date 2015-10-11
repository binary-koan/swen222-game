package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import storage.GameLoader;

public class Game {
	private HashMap<String, Item> items;
	private HashMap<String, Room> rooms;
	private HashMap<String, Player> players;
    private GameLoader loader;

    public Game(String filenameBase, String filenameGame){
    	this.loader = new GameLoader(this, filenameBase);
    	this.loader.setXMLFilename(filenameGame);
    	this.loader.saveWholeGame();
    }

    public void setItems(HashMap<String, Item> items){
		this.items = items;
	}

	public HashMap<String, Item> getItems(){
		return items;
	}

	public Item getItem(String id){
		for(Map.Entry<String, Item> item : items.entrySet()){
			if(item.getKey().equals(id)){
				return item.getValue();
			}
		}
		return null;
	}

    public void setRooms(HashMap<String, Room> rooms){
    	this.rooms = rooms;
    }

    public HashMap<String , Room> getRooms(){
		return rooms;
	}

	public Room getRoom(String id){
		for(Map.Entry<String, Room> room : rooms.entrySet()){
			if(room.getKey().equals(id)){
				return room.getValue();
			}
		}
		return null;
	}

	public void setPlayers(HashMap<String, Player> players){
    	this.players = players;
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public Player getPlayer(String playerName) {
        for(Map.Entry<String, Player> player : players.entrySet()){
        	if(player.getKey().equals(playerName)){
        		return player.getValue();
        	}
        }
        return null;
    }

    public GameLoader getData(){
    	return loader;
    }

}

