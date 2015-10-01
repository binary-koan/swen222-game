package game.storage;

//Author: Scott Holdaway

import java.util.ArrayList;
import java.util.HashMap;

import game.Direction;
import game.Drawable;
import game.Game;
import game.Item;
import game.Player;
import game.Room;
import game.Room.ItemInstance;

//I'm just using this to help with an xml object to parse, but
//anyone is welcome to use it to test stuff for rendering, gamelogic
//etc and fill in the blanks with getters/setters

public class XMLTester {

	public static void main(String[] args) {
		// Just a very crude testing class to make sure the file is writing ok.

		HashMap<String, Room> testRooms = new HashMap<String, Room>();
        HashMap<String, Item> testItems = new HashMap<String, Item>();
        ArrayList<Player> testPlayers = new ArrayList<Player>();

		Room testRoom1 = new Room("testRoom1"){
		@Override
        public String getWallImage() {
            return "backgrounds/room.png";
        }
        {
		Item item = new Item("Bed", "objects/bed.png");
		testItems.put(item.getName(), item);
        getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.BoundingCube(80, 0, 80, 48, 32, 48)));

        item = new Item("Chest", "objects/chest.png");
        testItems.put(item.getName(), item);
        getItems().add(new Room.ItemInstance(item, Direction.EAST, new Drawable.BoundingCube(40, 0, 120, 48, 32, 48)));

        item = new Item("Key", "objects/key.png");
        testItems.put(item.getName(), item);
        getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.BoundingCube(140, 60, 20, 32, 32, 32)));

        item = new Item("Door", "objects/door.png");
        testItems.put(item.getName(), item);
        getItems().add(new Room.ItemInstance(item, Direction.WEST, new Drawable.BoundingCube(140, 0, 80, 32, 48, 32)));

        }
		};


        Player player1 = new Player("Player1","characters/o1.png");
        player1.setRoom(testRoom1);
        Player player2 = new Player("Player2", "characters/o2.png");
        player2.setRoom(testRoom1);



        testRooms.put(testRoom1.getName(), testRoom1);
        testPlayers.add(player1);
        testPlayers.add(player2);


        //player2.setBoundingBox(new Drawable.BoundingCube(80, 80, 80, 32, 32, 32));




		GameData gameData = new GameData(testRooms, testItems);

		Game gameTo = new Game(testPlayers, gameData);

		//==================Note=====================
		//Here change the file directory for ToXML(gameTo, ""), FromXML("") and
		//ToXML(gameFrom, "") to a valid directory.
		//Eg /u/students/studenname/workspace/swen222-game/saveFile1.xml
		//===========================================
		//Here we are basically writing the game object defined above to XML,
		//reading a game object from that, and then writing back to XML from
		//that returned game object. The two files should be identical
		ToXML toTester1 = new ToXML(gameTo, "/u/students/holdawscot/saveFile1.xml");
		toTester1.writeRoot();

		FromXML fromTester = new FromXML("/u/students/holdawscot/saveFile1.xml");
		Game gameFrom = fromTester.readRoot();

		ToXML toTester2 = new ToXML(gameFrom, "/u/students/holdawscot/saveFile2.xml");
		toTester2.writeRoot();

	}
}

