package game.storage;

//Author: Scott Holdaway

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import game.*;
import game.Drawable.Point3D;
import game.Room.ItemInstance;

//I'm just using this to help with an xml object to parse, but
//anyone is welcome to use it to test stuff for rendering, gamelogic
//etc and fill in the blanks with getters/setters

public class XMLTester {

	public static void main(String[] args) {
		// Just a very crude testing class to make sure the file is writing ok.

//		HashMap<String, Room> testRooms = new HashMap<String, Room>();
//        final HashMap<String, Item> testItems = new HashMap<String, Item>();
//        ArrayList<Player> testPlayers = new ArrayList<Player>();
//
//		Room testRoom1 = new Room("testRoom1"){
//        {
//		Item item = new Furniture("Bed", "", "objects/bed.png");
//		testItems.put(item.getName(), item);
//        getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Point3D(80, 0, 80)));
//
//        item = new Container("Chest", "", "objects/chest.png");
//        testItems.put(item.getName(), item);
//        getItems().add(new Room.ItemInstance(item, Direction.EAST, new Point3D(40, 0, 120)));
//
//        item = new Key("Key", "", "objects/key.png");
//        testItems.put(item.getName(), item);
//        getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Point3D(140, 60, 20)));
//
//        item = new Door("Door", "", "objects/door.png");
//        testItems.put(item.getName(), item);
//        getItems().add(new Room.ItemInstance(item, Direction.WEST, new Point3D(140, 0, 80)));
//
//        }
//		};
//
//		Room testRoom2 = new Room("testRoom2");
//
//        Player player1 = new Player("Player1","characters/o1.png");
//        player1.setRoom(testRoom1);
//        Player player2 = new Player("Player2", "characters/o2.png");
//        player2.setRoom(testRoom1);
//
//        player1.turn(Direction.NORTH);
//        player2.turn(Direction.SOUTH);
//
//
//        testRooms.put(testRoom1.getName(), testRoom1);
//        testRooms.put(testRoom2.getName(), testRoom2);
//        testPlayers.add(player1);
//        testPlayers.add(player2);


        //player2.setBoundingBox(new Drawable.BoundingCube(80, 80, 80, 32, 32, 32));




//		GameData gameData = new GameData(testRooms, testItems);
//
//		Game gameTo = new Game(testPlayers, gameData);

		//==================Note=====================
		//Here change the file directory for ToXML(gameTo, ""), FromXML("") and
		//ToXML(gameFrom, "") to a valid directory.
		//Eg /u/students/studenname/workspace/swen222-game/saveFile1.xml
		//===========================================
		//Here we are basically writing the game object defined above to XML,
		//reading a game object from that, and then writing back to XML from
		//that returned game object. The two files should be identical
//		ToXML toTester1 = new ToXML(gameTo, "/u/students/holdawscot/saveFile1.xml");
//		toTester1.writeRoot();


//		player1.addInventoryItem(new Chest("testiitem", "o4.png"));
//		player1.toXML();
//		player1.loadXML(gameTo);
//		System.out.println(player1.getRoom().getName());

//		FromXML fromTester = new FromXML("/u/students/holdawscot/saveFile1.xml");
//		Game gameFrom = fromTester.readRoot();



//		Item itemTest = new Bed("tester", "objects/bed.png");
//		gameFrom.getData().getItems().put(itemTest.getName(), itemTest);

//		ToXML toTester2 = new ToXML(gameFrom, "/u/students/holdawscot/saveFile2.xml");
//		toTester2.writeRoot();



        Game game = new Game("resources/mainGame.xml");
        GameLoader data = new GameLoader(game, "resources/mainGame.xml");

        Room room = game.getRoom("rx0y4");
        room.getName();
        for(Map.Entry<String, Item> i : game.getItems().entrySet()){
        	System.out.println(i.getValue().getName());
        }
        for(Map.Entry<String, Room> r : game.getRooms().entrySet()){
        	System.out.println(r.getValue().getName());
        }
        for(Map.Entry<Direction, Room> r : room.roomConnections.entrySet()){
        	System.out.println(r.getValue().getName());
        }
        Room room2 = room.getConnection(Direction.NORTH);
        Container i = (Container) game.getItem("containerrx0y3");
        for(Item j : i.getItems()){
        	System.out.println(j.getName());
        }

	}
}


