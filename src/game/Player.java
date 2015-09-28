package game;

import java.util.ArrayList;
import java.util.HashMap;

public class Player implements Drawable {
	private String name;
	private String spriteName;
    private Room room;
    private Direction facingDirection;
    private HashMap<String, Item> inventory;

    public Player(String name, String spriteName){
    	this.name = name;
    	this.spriteName = spriteName;
    	inventory = new HashMap<String, Item>();
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

    public HashMap<String, Item> getInventory(){
    	return inventory;
    }

    public void addInventoryItem(Item item){
    	this.inventory.put(item.getName(), item);
    }

    @Override
    public BoundingCube getBoundingCube() {
        return null;
    }

    @Override
    public String getSpriteName() {
        return spriteName;
    }

}