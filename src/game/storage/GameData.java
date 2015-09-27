package game.storage;
//Author: Scott Holdaway

import game.Item;
import game.Player;
import game.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameData {

	private HashMap<String, Room> rooms;
	private HashMap<String, Item> items;

	public GameData(HashMap<String, Room> rooms, HashMap<String, Item> items){
		this.rooms = rooms;
		this.items = items;
	}

	public HashMap<String , Room> getRooms(){
		return rooms;
	}

	public Room getRoom(String name){
		for(Map.Entry<String, Room> r : rooms.entrySet()){
			if(r.getKey() == name){
				return r.getValue();
			}
		}
		return null;
	}

	public HashMap<String, Item> getItems(){
		return items;
	}

	public Item getItem(String name){
		for(Map.Entry<String, Item> i : items.entrySet()){
			if(i.getKey() == name){
				return i.getValue();
			}
		}
		return null;
	}

}