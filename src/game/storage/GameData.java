package game.storage;
//Author: Scott Holdaway

import game.Container;
import game.Door;
import game.Furniture;
import game.Item;
import game.Key;
import game.Player;
import game.Room;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class GameData {

	private HashMap<String, Room> rooms;
	private HashMap<String, Item> items;
	private String XMLFilename;
	private Document gameDoc;

	public GameData(String filename){
		this.XMLFilename = filename;
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(this.XMLFilename);
		try {
			this.gameDoc = builder.build(xmlFile);
		}
		catch (IOException io) {
			System.out.println(io.getMessage());
		}catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}





		this.items = loadItemsInitial();
		this.rooms = loadRoomsInitial();
		//Now we have items and rooms constructed in basic form and able to be referenced,
		//we assign them all their associations by reading from the same XML doc.
		loadWholeGame(gameDoc);
	}

	public HashMap<String , Room> getRooms(){
		return rooms;
	}

	public Room getRoom(String id){
		for(Map.Entry<String, Room> r : rooms.entrySet()){
			if(r.getKey().equals(id)){
				return r.getValue();
			}
		}
		return null;
	}

	public HashMap<String, Item> getItems(){
		return items;
	}

	public Item getItem(String id){
		for(Map.Entry<String, Item> i : items.entrySet()){
			if(i.getKey().equals(id)){
				return i.getValue();
			}
		}
		return null;
	}

	public HashMap<String, Item> loadItemsInitial() {
		HashMap<String, Item> items = new HashMap<String, Item>();
		Element rootNode = gameDoc.getRootElement();
		// Create all the items from the document, put them in the Map items
		Element itemsRoot = rootNode.getChild("gameItems");
		for (Element e : itemsRoot.getChildren()) {
			Item currentItem = readItemInitial(e);
			items.put(currentItem.getID(), currentItem);
		}
		return items;
	}

	private Item readItemInitial(Element e){
		//Working on a more robust, less error prone method.
		String currentClass = e.getChildText("subClass");
		Item currentItem;
		String id = e.getChildText("id");
		String name = e.getChildText("name");
		String description = e.getChildText("description");
		String spriteName = e.getChildText("spriteName");

		switch(currentClass){
		case "class game.Container":
			currentItem = new Container(id, name, description, spriteName);
			return currentItem;
		case "class game.Key":
			currentItem = new Key(id, name, description, spriteName);
			return currentItem;
		case "class game.Furniture":
			currentItem = new Furniture(id, name, description, spriteName);
			return currentItem;
		case "class game.Door":
			currentItem = new Door(id, name, description, spriteName);
			return currentItem;
		}
		return new Furniture(id, name, description, spriteName);
	}

	public HashMap<String, Room> loadRoomsInitial() {
		Element rootNode = gameDoc.getRootElement();
		// Create all the items from the document, put them in the Map items
		Element roomsRoot = rootNode.getChild("gameRooms");
		for (Element e : roomsRoot.getChildren()) {
			Room currentRoom = readRoomInitial(e);
			rooms.put(currentRoom.getID(), currentRoom);
		}
		return rooms;
	}

	private Room readRoomInitial(Element e){
		Room currentRoom = new Room(e.getChildText("id"), e.getChildText("name"));
		System.out.println(e.getChildText("id"));
		return currentRoom;
	}



	public void saveWholeGame(Document gameDoc){
		for(Map.Entry<String, Item> item : this.items.entrySet()){
			item.getValue().toXML(null);
		}

		for(Map.Entry<String, Room> room : this.rooms.entrySet()){
			room.getValue().toXML(null);
		}
	}

	public void loadWholeGame(Document gameDoc){
		for(Map.Entry<String, Item> item : this.items.entrySet()){
			item.getValue().loadXML(this);
		}
		for(Map.Entry<String, Room> room : this.rooms.entrySet()){
			room.getValue().loadXML(this);
		}
	}







	public String getXMLFilename(){
		return this.XMLFilename;
	}

	public void setXMLFilename(String filename){
		this.XMLFilename = filename;
	}



}