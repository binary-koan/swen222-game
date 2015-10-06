package game;

import java.util.ArrayList;

public class Player implements Drawable {
	private String name;
	private String spriteName;
    private Room room;
    private Direction facingDirection;
    private ArrayList<Item> inventory;

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

    public void removeInventoryItem(Item item){
    	this.inventory.remove(item);
    }

    @Override
    public Point3D getPosition() {
    	//return this.boundingCube;
    	switch (this.facingDirection) {
    	case NORTH:
    		return new Point3D(80, 0, 0);
    	case SOUTH:
    		return new Point3D(80, 0, 160);
    	case EAST:
    		return new Point3D(160, 0, 80);
    	case WEST:
		default:
    		return new Point3D(0, 0, 80);
    	}
    }

    @Override
    public String getSpriteName() {
        return spriteName;
    }

}