package game;

import game.Drawable.Point3D;
import game.storage.Serializable;

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
    public Map<Direction, Room> roomConnections = new HashMap<Direction, Room>();
    private Map<Direction, Boolean> wallConnections = new HashMap<Direction, Boolean>();

    public Room(String id, String name) {
    	this.id = id;
    	this.name = name;

    }

    public String getID(){
    	return id;
    }

	public boolean hasWall(Direction position) {
		return this.wallConnections.get(position);
	}

    public Room getConnection(Direction position) {
        return this.roomConnections.get(position);
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

    @Override
   	public Element toXML() {
   		Element room = new Element("room");
   		room.addContent("id").setText(this.id);
   		room.addContent("name").setText(this.name);
   		room.addContent("roomItems");
   		for(ItemInstance i : this.items){
   			room.getChild("roomItems").addContent("itemInstance");
   			room.getChild("roomItems").getChild("itemsInstance").addContent("item").setText(i.getItem().getID());
   			room.getChild("roomItems").getChild("itemsInstance").addContent("facingDirection").setText(i.getFacingDirection().toString());
   			room.getChild("roomItems").getChild("itemsInstance").addContent("boundingBox");
   			room.getChild("roomItems").getChild("itemsInstance").getChild("boundingBox").addContent("x").setText("x"+Integer.toString(i.getPosition().x));
   			room.getChild("roomItems").getChild("itemsInstance").getChild("boundingBox").addContent("y").setText("y"+Integer.toString(i.getPosition().y));
   			room.getChild("roomItems").getChild("itemsInstance").getChild("boundingBox").addContent("z").setText("z"+Integer.toString(i.getPosition().z));
   		}
   		room.addContent("roomPlayers");
   		for(Player roomPlayer : this.players){
   			room.getChild("roomPlayers").addContent("player").setText(roomPlayer.getName());
   		}
   		room.addContent("roomConnections");
   		for(Map.Entry<Direction, Room> roomConnection : this.roomConnections.entrySet()){
   			room.getChild("roomConnections").addContent("entry").setText((roomConnection.getKey().toString()+"-"+roomConnection.getValue().getID()));
   		}
   		room.addContent("wallConnections");
   		for(Map.Entry<Direction, Boolean> wallConnection : this.wallConnections.entrySet()){
   			room.getChild("wallConnections").addContent("entry").setText(wallConnection.getKey().toString()+"-"+wallConnection.toString());
   		}
   		return room;
   	}

    @Override
	public void loadXML(Game game, Element objectElement) {
		this.items.removeAll(items);
		for(Element roomItem : objectElement.getChild("roomItems").getChildren()){
			Item ir = game.getItem(roomItem.getChildText("item"));
			Direction dr = Direction.fromString(roomItem.getChildText("facingDirection"));
			int xr = Integer.parseInt(roomItem.getChild("boundingBox").getChildText("x").substring(1));
			int yr = Integer.parseInt(roomItem.getChild("boundingBox").getChildText("y").substring(1));
			int zr = Integer.parseInt(roomItem.getChild("boundingBox").getChildText("z").substring(1));
			Point3D pr = new Point3D(xr, yr, zr);
			ItemInstance itemI = new ItemInstance (ir, dr, pr);
			items.add(itemI);
		}
		this.roomConnections.clear();
		for(Element roomConnection : objectElement.getChild("roomConnections").getChildren()){
			String[] splitResult = roomConnection.getText().split("-", 2);
			String dir = splitResult[0];
			String room = splitResult[1];
			this.roomConnections.put(Direction.fromString(dir), game.getRoom(room));
		}
		this.wallConnections.clear();
		for(Element wallConnection : objectElement.getChild("wallConnections").getChildren()){
			String[] splitResult = wallConnection.getText().split("-", 2);
			String dir = splitResult[0];
			Boolean wall = Boolean.valueOf(splitResult[1]);
			this.wallConnections.put(Direction.fromString(dir), wall);
		}
	}
}
