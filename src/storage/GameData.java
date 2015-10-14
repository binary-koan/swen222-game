package storage;
//Author: Scott Holdaway

import game.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class GameData {
	private String XMLFilename;
	private Document gameDoc;
	private Game game;

	/**
	 * When created, the game loader populates a game's collections of items, players
	 * and rooms with those in the XML file.
	 * @param game The game whose collectons it will populate.
	 * @param filename The xml file to be read
	 */
	public GameData(Game game, String filename){
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

		game.setItems(loadItemsInitial());
		game.setRooms(loadRoomsInitial());

		for (Player player : loadPlayersInitial().values()) {
			game.addPlayer(player);
		}
		//Now we have items and rooms constructed in basic form and able to be referenced,
		//we assign them all their associations by reading from the same XML doc.
		loadWholeGame();
	}

	/**
	 * Looks through all item elements and creates them, and puts them in
	 * a map to be given to the game, which will now have items other
	 * objects can reference.
	 * @return A map of the game's items.
	 */
	private HashMap<String, Item> loadItemsInitial() {
		HashMap<String, Item> items = new HashMap<String, Item>();
		Element rootNode = gameDoc.getRootElement();
		// Create all the items from the document, put them in the Map items.
		Element itemsRoot = rootNode.getChild("gameItems");
		for (Element e : itemsRoot.getChildren()) {
			Item currentItem = constructItemInitial(e);
			items.put(currentItem.getID(), currentItem);
		}
		return items;
	}

	/**
	 * Creates an item. Note that its associations with items etc have not yet been set.
	 * Checks the element's sublcass to see what item it should create.
	 * @param e The element to be read
	 * @return A new item.
	 */
	private Item constructItemInitial(Element e){
		Item currentItem;
		String id = e.getChildText("id");
		String name = e.getChildText("name");
		String description = e.getChildText("description");
		String spriteName = e.getChildText("spriteName");
		String currentClass = e.getChildText("subClass");

		switch(currentClass){
		case "class game.Container":
			currentItem = new Container(id, name, description, spriteName);
			return currentItem;
		case "class game.Holdable":
			currentItem = new Holdable(id, name, description, spriteName);
			return currentItem;
		case "class game.Key":
			currentItem = new Key(id, name, description, Key.Color.RED); //TODO
			return currentItem;
		case "class game.Furniture":
			currentItem = new Furniture(id, name, description, spriteName);
			return currentItem;
		case "class game.Monster":
			currentItem = new Monster(id, name, description, spriteName, null); //TODO
			return currentItem;
		case "class game.Weapon":
			currentItem = new Weapon(id, name, description, spriteName);
			return currentItem;
		}
		System.out.println(e.getChildText("WRONG"));
		return new Furniture(id, name, description, spriteName);
	}

	/**
	 * Looks through all room elements and creates them, and puts them in
	 * a map to be given to the game, which will now have rooms other
	 * objects can reference.
	 * @return A map of the game's rooms.
	 */
	private HashMap<String, Room> loadRoomsInitial() {
		HashMap<String, Room> rooms = new HashMap<String, Room>();
		Element rootNode = gameDoc.getRootElement();
		// Create all the rooms from the document, put them in the Map rooms.
		Element roomsRoot = rootNode.getChild("gameRooms");
		for (Element e : roomsRoot.getChildren()) {
			Room currentRoom = constructRoomInitial(e);
			rooms.put(currentRoom.getID(), currentRoom);
		}
		return rooms;
	}

	/**
	 * Creates a room. Note that its associations with items etc have not yet been set.
	 * @param e The element to be read.
	 * @return A new room.
	 */
	private Room constructRoomInitial(Element e) {
		Room currentRoom = new Room(e.getChildText("id"), e.getChildText("name"), null);
		return currentRoom;
	}

	/**
	 * Looks through all player elements and creates them, and puts them in
	 * a map to be given to the game, which will now have players other
	 * objects can reference.
	 * @return A map of the game's players.
	 */
	private HashMap<String, Player> loadPlayersInitial() {
		HashMap<String, Player> players = new HashMap<String, Player>();
		Element rootNode = gameDoc.getRootElement();
		// Create all the players from the document, put them in the Map players.
		Element playersRoot = rootNode.getChild("gamePlayers");
		for (Element e : playersRoot.getChildren()) {
			Player currentPlayer = constructPlayerInitial(e);
			players.put(currentPlayer.getName(), currentPlayer);
		}
		return players;
	}

	/**
	 * Creates a player. Note that its associations with items etc have not yet been set.
	 * @param e The player element to be read.
	 * @return A new player.
	 */
	private Player constructPlayerInitial(Element e){
		Player currentPlayer = new Player(e.getChildText("name"), e.getChildText("spriteName"), this.game.getRooms().get(e.getChildText("room")));
		return currentPlayer;
	}

	/**
	 * Saves the whole game to the current filename specified. Every object in the
	 * game has its toXML method called, returning elements to add to the file.
	 */
	public void saveWholeGame(){
		Element toSave = new Element("game");
		Element gameItems = new Element("gameItems");
		Element gameRooms = new Element("gameRooms");
		Element gamePlayers = new Element("gamePlayers");

		for(Map.Entry<String, Item> item : this.game.getItems().entrySet()){
			gameItems.addContent(item.getValue().toXML());
		}
		for(Map.Entry<String, Room> room : this.game.getRooms().entrySet()){
			gameRooms.addContent(room.getValue().toXML());
		}
		for(Map.Entry<String, Player> player : this.game.getPlayers().entrySet()){
			//Dont save to XML is they're dead
			if(player.getValue().isAlive() == true){
				gamePlayers.addContent(player.getValue().toXML());
			}
		}
		toSave.addContent(gameItems);
		toSave.addContent(gameRooms);
		toSave.addContent(gamePlayers);
		//Set the gameDoc's root element as the one we added all the stuff to,
		//essentially overwriting the previous file.
		gameDoc.setRootElement(toSave);

		//Format nicely, and write the new file to the destination.
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		try {
			xmlOutput.output(gameDoc, new FileWriter(XMLFilename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("File Saved!");
	}

	/**
	 * Every object in the game reads its entry in the XML file. That object's
	 * loadXML method is called, which will update any changes seen in the XML file.
	 */
	public void loadWholeGame(){
		Element itemsRoot = gameDoc.getRootElement().getChild("gameItems");
		Element roomsRoot = gameDoc.getRootElement().getChild("gameRooms");
		Element playersRoot = gameDoc.getRootElement().getChild("gamePlayers");

		for(Element itemElement : itemsRoot.getChildren()){
			for(Map.Entry<String, Item> item : this.game.getItems().entrySet()){
				if(item.getKey().equals(itemElement.getChildText("id"))){
					item.getValue().loadXML(game, itemElement);
				}
			}
		}

		for(Element roomElement : roomsRoot.getChildren()){
			for(Map.Entry<String, Room> room : this.game.getRooms().entrySet()){
				if(room.getKey().equals(roomElement.getChildText("id"))){
					room.getValue().loadXML(game, roomElement);
				}
			}
		}

		for(Element playerElement : playersRoot.getChildren()){
			Boolean isInMap = false;
			for(Map.Entry<String, Player> player : this.game.getPlayers().entrySet()){
				if(player.getKey().equals(playerElement.getChildText("name"))){
					isInMap = true;
					player.getValue().loadXML(game, playerElement);
				}
			}
			if(isInMap == false){
				Player newPlayer = constructPlayerInitial(playerElement);
				newPlayer.loadXML(game, playerElement);
				game.getPlayers().put(newPlayer.getName(), newPlayer);
			}
			//The player is not found in the old collections so is new to the game.
			//We create him, set his associations with newPlayer.loadXML(), then
			//add him to the collection.
		}
	}

	/**
	 * Returns the current filename the class writes and reads to.
	 * @return
	 */
	public String getXMLFilename(){
		return this.XMLFilename;
	}

	/**
	 * Sets the current filename the class writes and reads to.
	 * @param filename The filename to be set.
	 */
	public void setXMLFilename(String filename){
		this.XMLFilename = filename;
	}

}