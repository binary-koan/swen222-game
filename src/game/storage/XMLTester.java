package game.storage;
//Author: Scott Holdaway

import java.util.ArrayList;

import game.Game;
import game.Item;
import game.Player;
import game.Room;

public class XMLTester {

	public static void main(String[] args) {
		// Just a very crude testing class to make sure the file is writing ok.

		Item i1 = new Item("item1");
		Item i2 = new Item("item2");
		Item i3 = new Item("item3");

		ArrayList<Item> testItems = new ArrayList<Item>();
		testItems.add(i1);
		testItems.add(i2);
		testItems.add(i3);

		ArrayList<Item> testInventory = new ArrayList<Item>();
		testInventory.add(i1);
		testInventory.add(i2);
		testInventory.add(i3);

		Room r1 = new Room("room1", testInventory);
		Room r2 = new Room("room1", testInventory);
		Room r3 = new Room("room2", testInventory);

		ArrayList<Room> testRooms = new ArrayList<Room>();
		testRooms.add(r1);
		testRooms.add(r2);
		testRooms.add(r3);

		Player player1 = new Player("player1", r1, Player.Position.NORTH);
		Player player2 = new Player("player2", r1, Player.Position.EAST);
		Player player3 = new Player("player3", r2, Player.Position.SOUTH);

		player1.setInventory(testInventory);

		ArrayList<Player> testPlayers = new ArrayList<Player>();
		testPlayers.add(player1);
		testPlayers.add(player2);
		testPlayers.add(player3);

		GameData gameData = new GameData(testRooms, testItems);

		Game game = new Game(testPlayers, gameData);

		ToXML tester = new ToXML(game);
		tester.writeRoot();

	}

}
