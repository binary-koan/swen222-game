package game;

import game.Drawable.Point3D;
import game.storage.GameData;
import game.storage.Serializable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
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
    private List<Door> doors = new ArrayList<Door>();

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

    public void addRoomItemInstance(ItemInstance item){
    	items.add(item);
    }

    public void removeRoomItemInstance(ItemInstance item){
    	items.remove(item);
    }

	@Override
	public void toXML() {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("/u/students/holdawscot/saveFile1.xml");
		try{
			Document document = builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			for(Element room : rootNode.getChild("gameRooms").getChildren()){
				if(room.getChildText("id").equals(this.getID())){
					room.getChild("roomItems").removeContent();
					for(ItemInstance i : this.items){
						room.getChild("roomItems").addContent("itemInstance");
						room.getChild("roomItems").getChild("itemsInstance").addContent("item").setText(i.getItem().getID());
						room.getChild("roomItems").getChild("itemsInstance").addContent("facingDirection").setText(i.getFacingDirection().toString());
						room.getChild("roomItems").getChild("itemsInstance").addContent("boundingBox");
						room.getChild("roomItems").getChild("itemsInstance").getChild("boundingBox").addContent("x").setText("x"+Integer.toString(i.getPosition().x));
						room.getChild("roomItems").getChild("itemsInstance").getChild("boundingBox").addContent("y").setText("y"+Integer.toString(i.getPosition().y));
						room.getChild("roomItems").getChild("itemsInstance").getChild("boundingBox").addContent("z").setText("z"+Integer.toString(i.getPosition().z));
					}
					room.getChild("roomConnections").removeContent();
					for(Map.Entry<Direction, Room> roomConnection : this.roomConnections.entrySet()){
						room.getChild("roomConnections").addContent("entry").setText((roomConnection.getKey().toString()+"-"+roomConnection.getValue().getID()));
					}
					room.getChild("wallConnections").removeContent();
					for(Map.Entry<Direction, Boolean> wallConnection : this.wallConnections.entrySet()){
						room.getChild("wallConnections").addContent("entry").setText(wallConnection.getKey().toString()+"-"+wallConnection.toString());
					}
				}
			}
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(document, new FileWriter("/u/students/holdawscot/saveFile1.xml"));
		}catch (IOException io) {
			System.out.println(io.getMessage());
		}catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
	}

	@Override
	public Object loadXML(GameData gameData) {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("resources/mainGame.xml");
		try{
			Document document = builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			for(Element gameRoom : rootNode.getChild("gameRooms").getChildren()){
				if(gameRoom.getChildText("id").equals(this.getID())){
					this.items.removeAll(items);
					for(Element roomItem : gameRoom.getChild("roomItems").getChildren()){
						Item ir = gameData.getItem(roomItem.getChildText("item"));
						Direction dr = Direction.fromString(roomItem.getChildText("facingDirection"));
						int xr = Integer.parseInt(roomItem.getChild("boundingBox").getChildText("x").substring(1));
						int yr = Integer.parseInt(roomItem.getChild("boundingBox").getChildText("y").substring(1));
						int zr = Integer.parseInt(roomItem.getChild("boundingBox").getChildText("z").substring(1));
						Point3D pr = new Point3D(xr, yr, zr);
						ItemInstance itemI = new ItemInstance (ir, dr, pr);
						this.addRoomItemInstance(itemI);
					}
					for(Element roomConnection : gameRoom.getChild("roomConnections").getChildren()){
						String[] splitResult = roomConnection.getText().split("-", 2);
						String dir = splitResult[0];
						String room = splitResult[1];
						this.roomConnections.put(Direction.fromString(dir), gameData.getRoom(room));
					}
					for(Element wallConnection : gameRoom.getChild("wallConnections").getChildren()){
						String[] splitResult = wallConnection.getText().split("-", 2);
						String dir = splitResult[0];
						Boolean wall = Boolean.valueOf(splitResult[1]);
						this.wallConnections.put(Direction.fromString(dir), wall);
					}
				}
			}
			return null;
		}catch (IOException io) {
			System.out.println(io.getMessage());
		}catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		return null;
	}
}
