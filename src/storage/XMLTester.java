package storage;

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



		/*
        Game game = new Game("resources/mainGame.xml", "resources/continueGame.xml");
        Player playerTest = new Player("Player 1", "characters/alien1.png", game.getRoom("rx0y1"));
        System.out.println(playerTest.getName());
        game.getPlayers().put(playerTest.getName(), playerTest);
        System.out.println(game.getPlayer("Player 1"));
        //game.getRoom("rx0y1").addPlayer(playerTest);
        game.getData().saveWholeGame();
        game.getData().loadWholeGame();
        System.out.println("=============Items==============");
        for(Map.Entry<String, Item> i : game.getItems().entrySet()){
        	System.out.println("=============NewItem==============");
        	System.out.println(i.getValue().getID());
        	System.out.println(i.getValue().getName());
        	System.out.println(i.getValue().getDescription());
        	System.out.println(i.getValue().getSpriteName());
        	System.out.println("===========ContainerInventory================");
        	if(i.getValue() instanceof Container){
        		for(Item j : ((Container) i.getValue()).getItems()){
        			System.out.println(j.getID());
        		}
        	}
        }
        System.out.println("===========================");
        System.out.println("===========================");
        System.out.println("===========================");
        System.out.println("============Rooms===============");

        for(Map.Entry<String, Room> r : game.getRooms().entrySet()){
        	System.out.println("===========NewRoom================");
        	System.out.println(r.getValue().getID());
        	System.out.println(r.getValue().getName());
        	System.out.println("==========Players=================");
        	for(Player p : r.getValue().players){
        		System.out.println(p.getName());
        	}
        	System.out.println("==========Items=================");
        	for(ItemInstance i : r.getValue().items){
        		System.out.println(i.getItem().getName());

        	}
        	System.out.println("==========RoomConnections=================");
        	for(Map.Entry<Direction, Room> d : r.getValue().roomConnections.entrySet()){
        		System.out.println(d.getKey().toString());
        		System.out.println(d.getValue().getName());
        	}
        	System.out.println("=========WallConnections==================");
        	for(Map.Entry<Direction, Boolean> d : r.getValue().wallConnections.entrySet()){
        		System.out.println(d.getKey().toString());
        		System.out.println(d.getValue().toString());
        	}
        	System.out.println("===========================");


        }
        System.out.println("===========================");
        System.out.println("===========================");
        System.out.println("=========Players==================");

        for(Map.Entry<String, Player> pl : game.getPlayers().entrySet()){
        	System.out.println("===========NewPlayer================");
        	System.out.println(pl.getValue().getName());
        }
        System.out.println("==========End=================");
		 */

	}
}


