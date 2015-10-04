package game.storage;
//Author: Scott Holdaway
import game.Game;
import game.Item;
import game.Player;
import game.Room;
import game.Room.ItemInstance;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ToXML {

	// Hey guys, just uploaded a super basic write to XML class (read XML coming soon),
	// along with a class to test it if it works and also a game data class. I'm not
	// sure what the plans are on how to implement the game state and all, so this can
	// be changed no prob. Also I'm not sure if this is good git etiquette but I went
	// and made a bunch of basic fields/constructors/getters for game, player, item and
	// room to help make my thing work, but these can be changed later ofc. Also made
	// XMLTester, which crudely makes a very basic Game object to see if my stuff worked.
	// You can run that, but first you will need to change the file directory in class
	// XML tester in to a place on your machine (commented). Right now I'm
	// not sure on what fields we've decided for each class, so ofc its all pretty simple
	// in terms of what it actually records in XML. Basically I have no idea what I'm doing
	// and if I am completely on the wrong track or doing something real bad pls tell me haha.
	// Let me know if this doesn't work/messes up your plans.

	private Game game;
	private String filename;

	public ToXML(Game game, String filename){
		this.game = game;
		this.filename = filename;
	}

	/**
	 * The main write method. Creates a new xml document, and takes the game field as its starting point.
	 */
	public void writeRoot(){

		try{
			Element saveGame = new Element("saveGame");
			Document saveDoc = new Document(saveGame);


			Element gameRooms = new Element("gameRooms");
			for(Map.Entry<String, Room> r : game.getData().getRooms().entrySet()){
				gameRooms.addContent(writeRoom(r.getValue()));
			}
			saveDoc.getRootElement().addContent(gameRooms);



			Element gameItems = new Element("gameItems");
			for(Map.Entry<String, Item> i : game.getData().getItems().entrySet()){
				gameItems.addContent(writeItem(i.getValue()));
			}
			saveDoc.getRootElement().addContent(gameItems);



			Element gamePlayers = new Element("gamePlayers");
			//Sc\\ Loops through all the players in the Game Object's player list,
			//putting them and their attributes into the players element.
			for(Player p: this.game.getPlayers()){
				gamePlayers.addContent(writePlayer(p));
			}
			saveDoc.getRootElement().addContent(gamePlayers);



			XMLOutputter xmlOutput = new XMLOutputter();
			//Sc\\ Display nicely (for XML).
			xmlOutput.setFormat(Format.getPrettyFormat());

			xmlOutput.output(saveDoc, new FileWriter(filename));
			System.out.println("File Saved!");

		}catch (IOException io) {
			System.out.println(io.getMessage());
		  }
	}


	/**
	 * Reads a player, creates a player element and adds all of its data as child elements of it.
	 * @param p The player to be read
	 * @return
	 */
	private Element writePlayer(Player p){
			Element currentPlayer = new Element("player");

			currentPlayer.addContent(new Element("name").setText(p.getName()));
			currentPlayer.addContent(new Element("spriteName").setText(p.getSpriteName()));
			currentPlayer.addContent(new Element("room").setText(p.getRoom().getName()));
			if(p.getFacingDirection() != null){
				currentPlayer.addContent(new Element("facingDirection").setText(p.getFacingDirection().toString()));
			}
			Element inventory = new Element("playerInventory");
			if(p.getInventory()!= null){
				for(Item i : p.getInventory()){
					inventory.addContent(new Element("name").setText(i.getName()));
				}
			}
			currentPlayer.addContent(inventory);
			return currentPlayer;
	}

	/**
	 * Reads an item, adds its data (just name atm) as child elements.
	 * @param i The item to be read
	 * @return
	 */
	private Element writeItem(Item i){
		Element currentItem = new Element("item");

		currentItem.addContent(new Element("name").setText( i.getName()));
		currentItem.addContent(new Element("spriteName").setText(i.getSpriteName()));
		currentItem.addContent(new Element("subClass").setText(i.getClass().toString()));
		//currentItem.addContent(new Element("facingDirection", i.getFacingDirection().toString()));
//		if(i.getHolder() != null){
//			currentItem.addContent(new Element("holder").setText(i.getHolder().getName()));
//		}
//		if(i.getRoom() != null){
//			currentItem.addContent(new Element("room").setText(i.getRoom().getName()));
//		}
		return currentItem;
	}


	/**
	 * Reads an item instance form a room, adds its data as child elements
	 * @param i roomInstance item to be read
	 * @return
	 */
	private Element writeItemInstance(ItemInstance i){
		Element currentItemInstance = new Element("itemInstance");
		currentItemInstance.addContent(new Element("item").setText(i.getItem().getName()));
		currentItemInstance.addContent(new Element("facingDirection").setText(i.getFacingDirection().toString()));

		//The item's bounding box
		Element boundingBox = new Element("boundingBox");
		boundingBox.addContent(new Element("x").setText("x"+i.getBoundingCube().x));
		boundingBox.addContent(new Element("y").setText("y"+i.getBoundingCube().x));
		boundingBox.addContent(new Element("z").setText("z"+i.getBoundingCube().x));
		boundingBox.addContent(new Element("width").setText("w"+i.getBoundingCube().x));
		boundingBox.addContent(new Element("height").setText("h"+i.getBoundingCube().x));
		boundingBox.addContent(new Element("depth").setText("d"+i.getBoundingCube().x));

		currentItemInstance.addContent(boundingBox);


		return currentItemInstance;
	}

	/**
	 * Reads a room, creates a new room element and adds all of its data as child elements of it.
	 * @param r The room to be read
	 * @return
	 */
	private Element writeRoom(Room r){
		Element currentRoom = new Element("room");

		currentRoom.addContent(new Element("name").setText(r.getName()));

		Element roomItems = new Element("roomItems");
		for(ItemInstance i : r.getItems()){
			roomItems.addContent(writeItemInstance(i));
		}
		currentRoom.addContent(roomItems);
//		Element roomPlayers = new Element("roomPlayers");
//		for(Player p : r.getPlayers()){
//			roomPlayers.addContent(new Element("name").setText(p.getName()));
//		}
//		currentRoom.addContent(roomPlayers);
		return currentRoom;
	}

}
