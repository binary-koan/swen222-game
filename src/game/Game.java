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

import game.storage.GameLoader;

public class Game {
	private HashMap<String, Item> items;
	private HashMap<String, Room> rooms;
	private HashMap<String, Player> players;
    private GameLoader data;
    private String XMLFilename;

    public Game(GameLoader data){
    	this.XMLFilename = data.getXMLFilename();
    	this.data = data;
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

    public GameLoader getData(){
    	return data;
    }


}

