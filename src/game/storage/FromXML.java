package game.storage;
//Author: Scott Holdaway
import game.*;
import game.Drawable.Point3D;

import java.awt.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.*;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class FromXML {
//	Here is fromXML. Still a couple of errors where it outputs an XML file slightly different to
//	the one it should. Working on that. Changed a bunch of constructors/setters/getters/fields
//	again. Hope thats ok, let me know if it isnt. Once again I have no clue what Im doing, let
//	me know if im doing anything horribly wrong
//

	private String filename;
	private Document document;
	private HashMap<String, Room> rooms;
	private HashMap<String, Item> items;
	private HashMap<String, Player> players;
	private Element rootNode;

	public FromXML(String filename){
		this.filename = filename;
	}


	/**
	 * The "main" method of reading an xml file. Reads through the document, creating
	 * items, rooms and players collections in a Game (in that order). It then calls
	 * set data to allocate all the attributes/associations
	 * @return
	 */
	public Game readRoot(){
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(filename);

		rooms = new HashMap<String, Room>();
		items = new HashMap<String, Item>();
		players = new HashMap<String, Player>();

		try {
			document = builder.build(xmlFile);
			rootNode = document.getRootElement();

			//Create all the items from the document, put them in the Map items
			Element itemsRoot = rootNode.getChild("gameItems");
			for(Element e : itemsRoot.getChildren()){
				Item currentItem = readItem(e);
				items.put(currentItem.getName(), currentItem);
			}

			//Create all the rooms from the document, put them in the Map rooms
			Element roomsRoot = rootNode.getChild("gameRooms");
			for(Element e : roomsRoot.getChildren()){
				Room currentRoom = readRoom(e);

				rooms.put(currentRoom.getName(), currentRoom);
			}

			//Create all the items from the document, put them in the Map items
			Element playersRoot = rootNode.getChild("gamePlayers");
			for(Element e : playersRoot.getChildren()){
				Player currentPlayer = readPlayer(e, null);	//				//	//			//
				players.put(currentPlayer.getName(), currentPlayer);
			}

			setData(rootNode);
			//Not sure if final game and game data lists should be arrays or maps?
			ArrayList<Player> playerList = new ArrayList<Player>();
			for(Map.Entry<String, Player> p : players.entrySet()){
				playerList.add(p.getValue());
			}

			//GameData data = new GameData(rooms, items);
			//Game game = new Game(playerList, data);
			System.out.println("File Read!");
			return null;
			//return game;

		}catch (IOException io) {
			System.out.println(io.getMessage());
		}catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		return null;
	}

	public static Room readRoom(Element e){
		Room currentRoom = new Room(e.getChildText("name"));
		return currentRoom;
	}

	/**
	 * Reads an item, and depending on its actual subtype creates an item of that subtype
	 * @param e The element to be read
	 * @return
	 */
	public static Item readItem(Element e){
		//Working on a more robust, less error prone method.
		String currentClass = e.getChildText("subClass");
		Item currentItem;

		switch(currentClass){
		case "class game.Chest":
			currentItem = new Container(e.getChildText("name"), "", e.getChildText("spriteName"));
			return currentItem;
		case "class game.Key":
			currentItem = new Key(e.getChildText("name"), "", e.getChildText("spriteName"));
			return currentItem;
		case "class game.Bed":
			currentItem = new Furniture(e.getChildText("name"), "", e.getChildText("spriteName"));
			return currentItem;
		case "class game.Door":
			currentItem = new Door(e.getChildText("name"), "", e.getChildText("spriteName"));
			return currentItem;
		}
		return new Container(e.getChildText("name"), "", e.getChildText("spriteName"));
	}

	/**
	 * Reads a player and creates a player in basic form, leaving the setting of its attributes
	 * to later.
	 * @param e The element to be read
	 * @return
	 */
	public static Player readPlayer(Element e, GameData data){
		Player currentPlayer = new Player(e.getChildText("name"), e.getChildText("spriteName"));
		if(currentPlayer.getFacingDirection() != null){
			currentPlayer.setFacingDirection(Direction.fromString(e.getChildText("facingDirection")));
		}
		for(Element i : e.getChild("playerInventory").getChildren()){
			currentPlayer.addInventoryItem(data.getItem(i.getText()));
		}
		currentPlayer.setRoom(data.getRoom(e.getChildText("room")));
		return currentPlayer;
	}

	/**
	 * Reads through the XML document and gathers all the associations, such as an item's room or holder,
	 * and then sets that item's field to the actual object.
	 * @param rootNode
	 */
	private void setData(final Element rootNode){
		Element playersRoot = rootNode.getChild("gamePlayers");
		Element itemsRoot = rootNode.getChild("gameItems");
		Element roomsRoot = rootNode.getChild("gameRooms");

		Room placeHolderRoom = new Room("placeholder"){
		{
			Element roomsRoot = rootNode.getChild("gameRooms");

			for(Element room : roomsRoot.getChildren()){
				for(Element roomItems : room.getChildren("roomItems")){
					for(Element itemInstance : roomItems.getChildren()){
						Item itemTo = items.get(itemInstance.getChildText("item"));
						Direction itemDir = Direction.fromString(itemInstance.getChildText("facingDirection"));
						int bX = Integer.parseInt(itemInstance.getChild("boundingBox").getChildText("x").substring(1));
						int bY = Integer.parseInt(itemInstance.getChild("boundingBox").getChildText("y").substring(1));
						int bZ = Integer.parseInt(itemInstance.getChild("boundingBox").getChildText("z").substring(1));

						Point3D itemPoint = new Point3D(bX, bY, bZ);
						ItemInstance roomItem = new ItemInstance(itemTo, itemDir, itemPoint);
						rooms.get(room.getChildText("name")).getItems().add(roomItem);
					}
				}
			}
		}

		};

//
//
//		//For every item that has been added to the map of items, check through the XML document to see if
//		//it has a holder, and make that player its holder.
//		for(Element i : itemsRoot.getChildren()){
//			if(i.getChild("holder") != null){
//				//Set the items holder to be the player object, found in HashMap players.
//				items.get(i.getChildText("name")).setHolder(players.get((i.getChild("holder").getText())));
//				//Add the item to the player's inventory.
//				players.get(i.getChild("holder").getText()).addInventoryItem(items.get(i.getChildText("name")));
//			}
//			if(i.getChild("room") != null){
//				//Set the item's room to be the room object, found in HashMap rooms.
//				items.get(i.getChildText("name")).setRoom(rooms.get((i.getChild("room").getText())));
//				//Not sure how items in rooms will work
//				//rooms.get(i.getChild("room").getText()).addRoomItem(item.getValue());
//			}
//		}
		//For every player that has been added to the map of players, read its room from the XML document,
		//and make that room its room.

		for (Element p : playersRoot.getChildren()){
			if(rooms.containsKey(p.getChild("room").getText())){
				players.get(p.getChildText("name")).setRoom(rooms.get(p.getChildText("room")));
				//rooms.get(p.getChildText("room")).addPlayer(players.get(p.getChildText("name")));
			}

		}

	}

}
