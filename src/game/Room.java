package game;

import java.awt.*;
import java.util.List;

public class Room {
	private String name;
    private Image wallTexture;
    private List<Item> items;
    private int size;

    public Room(String name, List<Item> items){
    	this.name = name;
    	this.items = items;
    }

    public Image getWallTexture() {
        return wallTexture;
    }

    public Room getConnection(Player.Position position) {
        return null;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getSize() {
        return size;
    }

    public String getName(){
    	return name;
    }
}
