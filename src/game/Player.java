package game;

import game.Drawable.BoundingCube;

import java.util.ArrayList;
import java.util.HashMap;

public class Player implements Drawable {
	private String name;
	private String spriteName;
    private Room room;
    private Direction facingDirection;
    private ArrayList<Item> inventory;
    private BoundingCube boundingCube;

    public Player(String name, String spriteName){
    	this.name = name;
    	this.spriteName = spriteName;
    	inventory = new ArrayList<Item>();
    }

    public String getName(){
    	return name;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room){
    	this.room = room;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(Direction facingDirection) {
        this.facingDirection = facingDirection;
    }

    public ArrayList<Item> getInventory(){
    	return inventory;
    }

    public void addInventoryItem(Item item){
    	this.inventory.add(item);
    }

    @Override
    public BoundingCube getBoundingCube() {
    	//return this.boundingCube;
    	switch (this.facingDirection) {
    	case NORTH:
    		return new BoundingCube(80, 0, 0, 32, 32, 32);
    	case SOUTH:
    		return new BoundingCube(80, 0, 160, 32, 32, 32);
    	case EAST:
    		return new BoundingCube(160, 0, 80, 32, 32, 32);
    	case WEST:
    		return new BoundingCube(0, 0, 80, 32, 32, 32);
    	default:
    		return new BoundingCube(80, 40, 80, 32, 32, 32);
    	}
    }

    @Override
    public String getSpriteName() {
        return spriteName;
    }

	public void setBoundingBox(BoundingCube boundingCube) {
		this.boundingCube = boundingCube;
	}

}