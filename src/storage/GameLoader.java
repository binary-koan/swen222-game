package storage;
//Author: Scott Holdaway

import game.Container;
import game.Door;
import game.Furniture;
import game.Game;
import game.Item;
import game.Key;
import game.Monster;
import game.Player;
import game.Room;
import game.Weapon;

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

public class GameLoader {

	private HashMap<String, Item> items;
	private HashMap<String, Room> rooms;
	private HashMap<String, Player> players;
	private String XMLFilename;
	private Document gameDoc;
	private Game game;

	public GameLoader(Game game, String filename){
		this.game = game;
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
		this.players = loadPlayersInitial();
		//Now we have items and rooms constructed in basic form and able to be referenced,
		//we assign them all their associations by reading from the same XML doc.
		loadWholeGame();
	}



	public HashMap<String, Item> loadItemsInitial() {
		HashMap<String, Item> items = new HashMap<String, Item>();
		Element rootNode = gameDoc.getRootElement();
		// Create all the items from the document, put them in the Map items
		Element itemsRoot = rootNode.getChild("gameItems");
		for (Element e : itemsRoot.getChildren()) {
			Item currentItem = constructItemInitial(e);
			items.put(currentItem.getID(), currentItem);
		}
		return items;
	}

	private Item constructItemInitial(Element e){
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
		case "class game.Monster":
			currentItem = new Monster(id, name, description, spriteName);
			return currentItem;
		case "class game.Weapon":
			currentItem = new Weapon(id, name, description, spriteName);
			return currentItem;
		}
		return new Furniture(id, name, description, spriteName);
	}

	public HashMap<String, Room> loadRoomsInitial() {
		Element rootNode = gameDoc.getRootElement();
		// Create all the items from the document, put them in the Map items
		Element roomsRoot = rootNode.getChild("gameRooms");
		for (Element e : roomsRoot.getChildren()) {
			Room currentRoom = constructRoomInitial(e);
			rooms.put(currentRoom.getID(), currentRoom);
		}
		return rooms;
	}

	private Room constructRoomInitial(Element e){
		Room currentRoom = new Room(e.getChildText("id"), e.getChildText("name"));
		System.out.println(e.getChildText("id"));
		return currentRoom;
	}


	public HashMap<String, Player> loadPlayersInitial() {
		HashMap<String, Player> players = new HashMap<String, Player>();

		Element rootNode = gameDoc.getRootElement();
		// Create all the items from the document, put them in the Map items
		Element playersRoot = rootNode.getChild("gamePlayers");
		for (Element e : playersRoot.getChildren()) {
			Player currentPlayer = constructPlayerInitial(e);
			players.put(currentPlayer.getName(), currentPlayer);
		}
		return players;
	}

	private Player constructPlayerInitial(Element e){
		// Hmm ... is it possible to have the player's room passed in here? - Jono
		Player currentPlayer = new Player(e.getChildText("name"), e.getChildText("spriteName"), null);
		return currentPlayer;
	}










	public void saveWholeGame(Document gameDoc){
		Element toSave = new Element("game");
		Element gameItems = new Element("gameItems");
		Element gameRooms = new Element("gameRooms");
		Element gamePlayers = new Element("gamePlayers");
		for(Map.Entry<String, Item> item : this.items.entrySet()){
			gameItems.addContent(item.getValue().toXML());
		}
		for(Map.Entry<String, Room> room : this.rooms.entrySet()){
			gameRooms.addContent(room.getValue().toXML());
		}
		for(Map.Entry<String, Player> player : this.players.entrySet()){
			gamePlayers.addContent(player.getValue().toXML());
		}
		toSave.addContent(gameItems);
		toSave.addContent(gameRooms);
		toSave.addContent(gamePlayers);
		gameDoc.setRootElement(toSave);
	}

	public void loadWholeGame(){
		Element itemsRoot = gameDoc.getRootElement().getChild("gameItems");
		Element roomsRoot = gameDoc.getRootElement().getChild("gameRooms");
		Element playersRoot = gameDoc.getRootElement().getChild("gamePlayers");

		for(Map.Entry<String, Item> item : this.items.entrySet()){
			for(Element itemElement : itemsRoot.getChildren()){
				if(item.getKey() == itemElement.getChildText("id"));
				item.getValue().loadXML(game, itemElement);
			}
		}

		for(Map.Entry<String, Room> room : this.rooms.entrySet()){
			for(Element roomElement : itemsRoot.getChildren()){
				if(room.getKey() == roomElement.getChildText("id"));
				room.getValue().loadXML(game, roomElement);
			}
		}

		for(Element playerElement : playersRoot.getChildren()){
			for(Map.Entry<String, Player> player : this.players.entrySet()){
				if(player.getKey() == playerElement.getChildText("name"));
				player.getValue().loadXML(game, playerElement);
			}
			//The player is not found in the old collections so is new to the game.
			//We create him, set his associations with newPlayer.loadXML(), then
			//add him to the collection.
			Player newPlayer = constructPlayerInitial(playerElement);
			newPlayer.loadXML(game, playerElement);
			game.getPlayers().put(newPlayer.getName(), newPlayer);
		}
		game.setItems(this.items);
		game.setRooms(this.rooms);
		game.setPlayers(this.players);
	}

	public String getXMLFilename(){
		return this.XMLFilename;
	}

	public void setXMLFilename(String filename){
		this.XMLFilename = filename;
	}



}