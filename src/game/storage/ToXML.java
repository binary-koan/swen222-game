package game.storage;
//Author: Scott Holdaway

// Hey guys, just uploaded a super basic write to XML class (read XML coming soon), 
// along with a class to test it if it works and also a game data class. I'm not 
// sure what the plans are on how to implement the game state and all, so this can 
// be changed no prob. Also I'm not sure if this is good git etiquette but I went 
// and made a bunch of basic fields/constructors/getters for game, player, item and 
// room to help make my thing work, but these can be changed later ofc. Also made 
// XMLTester, which crudely makes a very basic Game object to see if my stuff worked. 
// You can run that, but first you will need to change the file directory in class 
// ToXML in method writeRoot() to a place on your machine (commented). Right now I'm 
// not sure on what fields we've decided for each class, so ofc its all pretty simple 
// in terms of what it actually records in XML. Basically I have no idea what I'm doing 
// and if I am completely on the wrong track or doing something real bad pls tell me haha. 
// Let me know if this doesn't work/messes up your plans.

import game.Game;
import game.Item;
import game.Player;
import game.Room;

import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ToXML {

	private Game game;

	public ToXML(Game game){
		this.game = game;
	}

	/**
	 * The main write method. Creates a new xml document, and takes the game field as its starting point.
	 */
	public void writeRoot(){

		try{
			Element saveGame = new Element("saveGame");
			Document saveDoc = new Document(saveGame);

			Element gamePlayers = new Element("gamePlayers");
			//Sc\\ Loops through all the players in the Game Object's player list,
			//putting them and their attributes into the players element.
			for(Player p: this.game.getPlayers()){
				gamePlayers.addContent(writePlayer(p));
			}

			saveDoc.getRootElement().addContent(gamePlayers);

			Element gameData = new Element("gameData");
			gameData = writeData(gameData);

			saveDoc.getRootElement().addContent(gameData);

			XMLOutputter xmlOutput = new XMLOutputter();
			//Sc\\ Display nicely (for XML).
			xmlOutput.setFormat(Format.getPrettyFormat());
			//==================Note=====================
			//Here change the file directory for xml.output(saveDoc, new FileWriter("") to a valid directory.
			//Eg /u/students/studenname/workspace/swen222-game/saveFile1.xml
			//===========================================
			xmlOutput.output(saveDoc, new FileWriter(""));
			System.out.println("File Saved!");

		}catch (IOException io) {
			System.out.println(io.getMessage());
		  }
	}

	/**
	 * This will read the various collections in game data. Each collection will have one element, with the
	 * content of the collections making up child elements. Each collection is added back to the element that
	 * is returned to the root element.
	 * @param data The game data object
	 * @return The element to be added to the root element
	 */
	private Element writeData(Element data){
		//Add the list of rooms and its contents.
		Element gameRooms = new Element("gameRooms");
		for(Room r : game.getData().getRooms()){
			gameRooms.addContent(writeRoom(r));
		}
		data.addContent(gameRooms);
		//Add the list of items and its contents.
		Element gameItems = new Element("gameItems");
		for(Item i : game.getData().getItems()){
			gameItems.addContent(writeItem(i));
		}
		data.addContent(gameItems);

		return data;
	}

	/**
	 * Reads a player, creates a player element and adds all of its data as child elements of it.
	 * @param p The player to be read
	 * @return
	 */
	private Element writePlayer(Player p){
		//Sc\\ Error checking needed here.
			Element currentPlayer = new Element("player");

			currentPlayer.addContent(new Element("name", p.getName()));
			currentPlayer.addContent(writeRoom(p.getRoom()));
			currentPlayer.addContent(new Element("position", p.getFacingDirection().toString()));
			Element playerInventory = new Element("playerInventory");
			if(p.getInventory()!= null){
				for(Item i : p.getInventory()){
					playerInventory.addContent(writeItem(i));
				}
			}
			currentPlayer.addContent(playerInventory);

			return currentPlayer;
	}

	/**
	 * Reads an item, adds its data (just name atm) as child elements.
	 * @param i The item to be read
	 * @return
	 */
	private Element writeItem(Item i){
		Element currentItem = new Element("item");
		currentItem.addContent(new Element("name", i.getName()));

		return currentItem;
	}

	/**
	 * Reads a room, creates a new room element and adds all of its data as child elements of it.
	 * @param r The room to be read
	 * @return
	 */
	private Element writeRoom(Room r){
		Element currentRoom = new Element("room");
		//Add the name of the room.
		currentRoom.addContent(new Element("name", r.getName()));
		//Add the roomItems element, and then to that element add the items.
		Element roomItems = new Element("roomItems");
		for(Item i : r.getItems()){
			roomItems.addContent(writeItem(i));
		}
		currentRoom.addContent(roomItems);

		return currentRoom;
	}

}
