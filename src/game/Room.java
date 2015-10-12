package game;

import game.Drawable.Point3D;
import storage.Serializable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Room implements Serializable{
    public static int ROOM_SIZE = 320;
    public static int CEILING_HEIGHT = 192;

	public class ItemInstance implements Drawable{
        private Item item;
        private Direction facingDirection;
        private Point3D position;

        public ItemInstance(Item item, Direction facingDirection, Point3D position) {
            this.item = item;
            this.facingDirection = facingDirection;
            this.position = position;
        }

        @Override
        public String getName() {
            return item.getName();
        }

        @Override
		public Direction getFacingDirection() {
			return facingDirection;
		}

        @Override
        public Point3D getPosition() {
            return position;
        }

        @Override
        public String getSpriteName() {
            return item.getSpriteName();
        }

        public Item getItem() {
        	return item;
        }
    }

    private String id;
	private String name;
    private List<ItemInstance> items = new ArrayList<ItemInstance>();
    private List<Player> players = new ArrayList<Player>();
    private Map<Direction, Room> roomConnections = new HashMap<Direction, Room>();
    private Map<Direction, Boolean> wallConnections = new HashMap<Direction, Boolean>();

    public Room(String id, String name) {
    	this.id = id;
    	this.name = name;
    }

    public String getID(){
    	return id;
    }

	public boolean hasWall(Direction position) {
		Boolean result = wallConnections.get(position);
		return result == null ? false : result;
	}

    public Room getConnection(Direction direction) {
        return roomConnections.get(direction);
    }

	public void setConnection(Direction direction, Room other) {
		roomConnections.put(direction, other);
	}

    public List<ItemInstance> getItems() {
        return items;
    }

    public String getName(){
    	return name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player){
    	players.add(player);
    }

    public void removePlayer(Player player){
    	players.remove(player);
    }

	public void addItem(Item item) {
		Point3D position = new Point3D(
				50 + (int)(Math.random() * (Room.ROOM_SIZE - 100)), 0,
				50 + (int)(Math.random() * (Room.ROOM_SIZE - 100))
		);
		items.add(new ItemInstance(item, Direction.random(), position));
	}

	/**
	 * Author: Scott Holdaway
	 * Creates an XML element of the room by reading through all the fields
	 * of the room.
	 */
    @Override
   	public Element toXML() {
   		Element room = new Element("room");
   		room.addContent(new Element("id").setText(this.id));
   		room.addContent(new Element("name").setText(this.name));
   		room.addContent(new Element("roomItems"));
   		//Add the itemInstances in the room.
   		for(ItemInstance i : this.items){
   			Element itemInstance = new Element("itemInstance");
   			itemInstance.addContent(new Element("item").setText(i.getItem().getID()));
   			itemInstance.addContent(new Element("facingDirection").setText(i.getFacingDirection().toString()));
   			itemInstance.addContent(new Element("point"));
   			itemInstance.getChild("point").addContent(new Element("x").setText("x"+Integer.toString(i.getPosition().x)));
   			itemInstance.getChild("point").addContent(new Element("y").setText("y"+Integer.toString(i.getPosition().y)));
   			itemInstance.getChild("point").addContent(new Element("z").setText("z"+Integer.toString(i.getPosition().z)));
   			room.getChild("roomItems").addContent(itemInstance);
   		}
   		//Add the players in the room.
   		room.addContent(new Element("roomPlayers"));
   		for(Player roomPlayer : this.players){
   			room.getChild("roomPlayers").addContent(new Element("name").setText(roomPlayer.getName()));
   		}
   		//Add the room connections.
   		room.addContent(new Element("roomConnections"));
   		for(Map.Entry<Direction, Room> roomConnection : this.roomConnections.entrySet()){
   			room.getChild("roomConnections").addContent(new Element("entry").setText((roomConnection.getKey().toString()+"-"+roomConnection.getValue().getID())));
   		}
   		//Add the wall connections.
   		room.addContent(new Element("wallConnections"));
   		for(Map.Entry<Direction, Boolean> wallConnection : this.wallConnections.entrySet()){
   			room.getChild("wallConnections").addContent(new Element("entry").setText(wallConnection.getKey().toString()+"-"+wallConnection.toString()));
   		}
   		return room;
   	}

    /**
     * Author: Scott Holdaway
     * Sets all the fields in this room based on an XML element of this room.
     */
    @Override
	public void loadXML(Game game, Element objectElement) {
		//Set the room's items.
		this.items.removeAll(items);
		for(Element roomItem : objectElement.getChild("roomItems").getChildren()){
			Item ir = game.getItem(roomItem.getChildText("item"));
			Direction dr = Direction.fromString(roomItem.getChildText("facingDirection"));
			int xr = Integer.parseInt(roomItem.getChild("point").getChildText("x").substring(1));
			int yr = Integer.parseInt(roomItem.getChild("point").getChildText("y").substring(1));
			int zr = Integer.parseInt(roomItem.getChild("point").getChildText("z").substring(1));
			Point3D pr = new Point3D(xr, yr, zr);
			ItemInstance itemI = new ItemInstance (ir, dr, pr);
			items.add(itemI);
		}
		//Set the room's players.
		this.players.removeAll(players);
		for(Element player : objectElement.getChild("roomPlayers").getChildren()){
			this.players.add(game.getPlayer(player.getText()));
		}
		//Set the room's connections.
		this.roomConnections.clear();
		for(Element roomConnection : objectElement.getChild("roomConnections").getChildren()){
			String[] splitResult = roomConnection.getText().split("-", 2);
			String dir = splitResult[0];
			String room = splitResult[1];
			this.roomConnections.put(Direction.fromString(dir), game.getRoom(room));
		}
		//Set the room's walls.
		this.wallConnections.clear();
		for(Element wallConnection : objectElement.getChild("wallConnections").getChildren()){
			String[] splitResult = wallConnection.getText().split("-", 2);
			String dir = splitResult[0];
			Boolean wall = Boolean.valueOf(splitResult[1]);
			this.wallConnections.put(Direction.fromString(dir), wall);
		}
	}
}
