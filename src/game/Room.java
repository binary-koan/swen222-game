package game;

import game.Drawable.Point3D;
import storage.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

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
	private Key.Color color;
	private List<ItemInstance> items = new ArrayList<ItemInstance>();
	private List<Player> players = new ArrayList<Player>();
	private Map<Direction, Room> roomConnections = new HashMap<Direction, Room>();
	private Map<Direction, Boolean> wallConnections = new HashMap<Direction, Boolean>();

	private ItemInstance monster = null;

    public Room(String id, String name, Key.Color color) {
    	this.id = id;
    	this.name = name;
		this.color = color;
    }

	// Property access

    public String getID(){
    	return id;
    }

	public String getName(){
		return name;
	}

	public Key.Color getColor() {
		return color;
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

	public List<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player player){
		players.add(player);
	}

	public void removePlayer(Player player){
		players.remove(player);
	}

    public List<ItemInstance> getItems() {
        return items;
    }

	public boolean containsItem(Item item) {
		for (ItemInstance instance : items) {
			if (instance.getItem().equals(item)) {
				return true;
			}
		}
		return false;
	}

	public void addItem(Item item) {
		Point3D position = new Point3D(
				50 + (int)(Math.random() * (Room.ROOM_SIZE - 100)), 0,
				50 + (int)(Math.random() * (Room.ROOM_SIZE - 100))
		);

        ItemInstance instance = new ItemInstance(item, Direction.random(), position);
		items.add(instance);

		if (item instanceof Monster) {
			monster = instance;
		}
	}

	public void removeItem(Item item) {
		ItemInstance result = null;
		for (ItemInstance instance : items) {
			if (instance.getItem().equals(item)) {
				result = instance;
			}
		}
		items.remove(result);

		if (item instanceof Monster) {
			monster = null;
		}
	}

    public boolean containsMonster() {
        return monster != null;
    }

    public ItemInstance getMonster() {
        return monster;
    }

	public boolean allowsEntry(Player player, Direction entryDirection) {
		if (color == null || !hasWall(entryDirection)) {
			return true;
		}
		else {
			Item item = player.getHeldItem();
			return item instanceof Key && ((Key)item).getColor() == color;
		}
	}

	/**
	 * {@inheritDoc}
	 * @author holdawscot
	 */
    @Override
   	public Element toXML() {
   		Element room = new Element("room");
   		room.addContent(new Element("id").setText(this.id));
   		room.addContent(new Element("name").setText(this.name));
   		if(this.color != null){
   	   		room.addContent(new Element("color").setText(this.color.toString()));
   		}
   		else{
   			room.addContent(new Element("color").setText("null"));
   		}
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
   			room.getChild("wallConnections").addContent(new Element("entry").setText(wallConnection.getKey().toString()+"-"+wallConnection.getValue().toString()));
   		}
   		return room;
   	}

    /**
	 * {@inheritDoc}
	 * @author holdawscot
	 */
    @Override
	public void loadXML(Game game, Element objectElement) {
		//Set the room's items.
    	this.color = Key.Color.fromString(objectElement.getChildText("color"));
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

			if (ir instanceof Monster) {
				monster = itemI;
			}
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
