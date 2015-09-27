package game.storage;
//Author: Scott Holdaway


import java.util.ArrayList;
import java.util.HashMap;

import game.Game;
import game.Item;
import game.Player;
import game.Room;

//I'm just using this to help with an xml object to parse, but 
//anyone is welcome to use it to test stuff for rendering, gamelogic 
//etc and fill in the blanks with getters/setters

public class XMLTester {

	public static void main(String[] args) {
		 // Just a very crude testing class to make sure the file is writing ok.

		Room r1 = new Room("room1");
		Room r2 = new Room("room2");
		Room r3 = new Room("room3");

		Item i1 = new Item("item1", "chest");
		Item i2 = new Item("item2", "door");
		Item i3 = new Item("item3", "fireplace");
		Item i4 = new Item("item4", "key");

		Player player1 = new Player("player1", "Image1");
		Player player2 = new Player("player2", "Image2");
		Player player3 = new Player("player3", "Image3");
		Player player4 = new Player("player4", "Image4");

		player1.addInventoryItem(i1);
		i1.setHolder(player1);
		player1.addInventoryItem(i3);
		i3.setHolder(player1);

		i2.setRoom(r1);
		i4.setRoom(r1);

		player1.setRoom(r1);
		r1.addPlayer(player1);
		player2.setRoom(r2);
		r2.addPlayer(player2);
		player3.setRoom(r3);
		r3.addPlayer(player3);
		player4.setRoom(r1);
		r1.addPlayer(player4);

		HashMap<String, Room> testRooms = new HashMap<String, Room>();
		testRooms.put(r1.getName(), r1);
		testRooms.put(r2.getName(), r2);
		testRooms.put(r3.getName(), r3);


		HashMap<String, Item> testItems = new HashMap<String, Item>();
		testItems.put(i1.getName(), i1);
		testItems.put(i2.getName(), i2);
		testItems.put(i3.getName(), i3);
		testItems.put(i4.getName(), i4);

		ArrayList<Player> testPlayers = new ArrayList<Player>();
		testPlayers.add(player1);
		testPlayers.add(player2);
		testPlayers.add(player3);
		testPlayers.add(player4);

		GameData gameData = new GameData(testRooms, testItems);

		Game gameTo = new Game(testPlayers, gameData);

		//==================Note=====================
		//Here change the file directory for ToXML(gameTo, ""), FromXML("") and
		//ToXML(gameFrom, "") to a valid directory.
		//Eg /u/students/studenname/workspace/swen222-game/saveFile1.xml
		//===========================================
		ToXML toTester1 = new ToXML(gameTo, "/u/students/holdawscot/workspace/saveFile1.xml");
		toTester1.writeRoot();

		FromXML fromTester = new FromXML("/u/students/holdawscot/workspace/saveFile1.xml");
		Game gameFrom = fromTester.readRoot();

		ToXML toTester2 = new ToXML(gameFrom, "/u/students/holdawscot/workspace/saveFile2.xml");
		toTester2.writeRoot();

	}

}
