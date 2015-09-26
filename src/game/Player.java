package game;

import client.renderer.Drawable;
import client.renderer.RoomRenderer;

import java.awt.*;
import java.util.ArrayList;

public class Player implements Drawable {
	private String name;
    private Room room;
    private Position position;
    private ArrayList<Item> inventory;

    public Player(String name, Room room, Position position){
    	this.name = name;
    	this.room = room;
    	this.position = position;
    }

    public String getName(){
    	return name;
    }

    public Room getRoom() {
        return room;
    }

    public Position getPosition() {
        return position;
    }

    public ArrayList<Item> getInventory(){
    	return inventory;
    }

    public void setInventory(ArrayList<Item> inventory){
    	this.inventory = inventory;
    }

    public void addMovementListener(MovementListener movementListener) {
    }

    @Override
    public BoundingCube getBoundingCube() {
        return null;
    }

    @Override
    public String getSpriteName() {
        return null;
    }

    public enum Position {
        NORTH, SOUTH, EAST, WEST;

        public Position opposite() {
            return null;
        }
    }
}