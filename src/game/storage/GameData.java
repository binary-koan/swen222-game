package game.storage;
//Author: Scott Holdaway

import game.Item;
import game.Player;
import game.Room;

import java.util.ArrayList;

public class GameData {

	private ArrayList<Room> rooms;
	private ArrayList<Item> items;

	public GameData(ArrayList<Room> rooms, ArrayList<Item> items){
		this.rooms = rooms;
		this.items = items;
	}

	public ArrayList<Room> getRooms(){
		return rooms;
	}

	public Room getRoom(String name){
		for(Room r : rooms){
			if(r.getName() == name){
				return r;
			}
		}
		return null;
	}

	public ArrayList<Item> getItems(){
		return items;
	}

	public Item getItem(String name){
		for(Item i : items){
			if(i.getName() == name){
				return i;
			}
		}
		return null;
	}

}
