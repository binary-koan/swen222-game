package game.storage;
//Author: Scott Holdaway

import game.Item;
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

	public GameData(String filename){
		this.XMLFilename = filename;
		this.items = loadItems();
		this.rooms = loadRooms();
		for(Map.Entry<String, Room> r : rooms.entrySet()){
			r.getValue().loadXML(this);
		}
	}

	public HashMap<String , Room> getRooms(){
		return rooms;
	}

	public Room getRoom(String name){
		for(Map.Entry<String, Room> r : rooms.entrySet()){
			if(r.getKey().equals(name)){
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
			if(i.getKey().equals(name)){
				return i.getValue();
			}
		}
		return null;
	}

	public HashMap<String, Item> loadItems(){
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(this.XMLFilename);
		HashMap<String, Item> items = new HashMap<String, Item>();
		try {
			Document document = builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			//Create all the items from the document, put them in the Map items
			Element itemsRoot = rootNode.getChild("gameItems");
			for(Element e : itemsRoot.getChildren()){
				Item currentItem = game.storage.FromXML.readItem(e);
				items.put(currentItem.getName(), currentItem);
			}
			return items;
		}catch (IOException io) {
			System.out.println(io.getMessage());
		}catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		return null;
	}

	public HashMap<String, Room> loadRooms(){
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(this.XMLFilename);
		HashMap<String, Room> rooms = new HashMap<String, Room>();
		try {
			Document document = builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			//Create all the items from the document, put them in the Map items
			Element roomsRoot = rootNode.getChild("gameRooms");
			for(Element e : roomsRoot.getChildren()){
				Room currentRoom = game.storage.FromXML.readRoom(e);
				rooms.put(currentRoom.getName(), currentRoom);
			}
			return rooms;
		}catch (IOException io) {
			System.out.println(io.getMessage());
		}catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		return null;
	}

	public String getXMLFilename(){
		return this.XMLFilename;
	}

	public void setXMLFilename(String filename){
		this.XMLFilename = filename;
	}



}