package game;

import java.util.ArrayList;

public class Player implements Drawable {
	private String name;
    private Room room;
    private Direction facingDirection;
    private ArrayList<Item> inventory;

    public Player(String name, Room room, Direction facingDirection){
    	this.name = name;
    	this.room = room;
    	this.facingDirection = facingDirection;
    }

    public String getName(){
    	return name;
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public Direction getFacingDirection() {
        return facingDirection;
    }

    public ArrayList<Item> getInventory(){
    	return inventory;
    }

    public void setInventory(ArrayList<Item> inventory){
    	this.inventory = inventory;
    }

    @Override
    public BoundingCube getBoundingCube() {
        return null;
    }

    @Override
    public String getSpriteName() {
        return null;
    }

}