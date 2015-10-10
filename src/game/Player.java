package game;

import game.storage.GameData;
import game.storage.Serializable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Player implements Drawable, Serializable{
	private String id;
	private String name;
	private String spriteName;
    private Room room;
    private Direction facingDirection;
    private ArrayList<Item> inventory;
    private Weapon weapon = null;

    public Player(String name, String spriteName){
    	this.name = name;
    	this.spriteName = spriteName;
    	//inventory = new ArrayList<Item>();
    }

    public String getName(){
    	return name;
    }

    public String getID(){
    	return id;
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

    public Weapon getWeapon(){
    	return this.weapon;
    }

    public void pickUpWeapon(Weapon weapon){
    	this.weapon = weapon;
    }

//    public ArrayList<Item> getInventory(){
//    	return inventory;
//    }

    public void addInventoryItem(Item item){
    	this.inventory.add(item);
    }

//    public void removeInventoryItem(Item item){
//    	this.inventory.remove(item);
//    }

    @Override
    public Point3D getPosition() {
    	switch (this.facingDirection) {
    	case NORTH:
    		return new Point3D(160, 0, 10);
    	case SOUTH:
    		return new Point3D(160, 0, 310);
    	case EAST:
    		return new Point3D(310, 0, 160);
    	case WEST:
		default:
    		return new Point3D(10, 0, 160);
    	}
    }

    @Override
    public String getSpriteName() {
        return spriteName;
    }

	@Override
	public void toXML() {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("/u/students/holdawscot/saveFile1.xml");
		try{
			Document document = builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			for(Element p : rootNode.getChild("gamePlayers").getChildren()){
				if(p.getChildText("name").equals(this.getName())){
					p.getChild("room").setText(this.getRoom().getID());
					p.getChild("facingDirection").setText(this.getFacingDirection().toString());
					p.getChild("weapon").setText(this.weapon.getID());
					p.getChild("playerInventory").removeContent();
					for(Item i : this.inventory){
						p.getChild("playerInventory").addContent(new Element("item").setText(i.getID()));
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
	public Player loadXML(GameData gameData) {
		// TODO Auto-generated method stub
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("/u/students/holdawscot/saveFile1.xml");
		try{
			Document document = builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			for(Element p : rootNode.getChild("gamePlayers").getChildren()){
				if(p.getChildText("name").equals(this.getName())){
					this.setRoom(gameData.getRoom(p.getChildText("room")));
					this.setFacingDirection(Direction.fromString(p.getChildText("facingDirection")));
					//if(p.getWeapon() != null){
						//this.setWeapongameData.getItem((p.getChildText("weapon")));
					//}
					this.inventory.removeAll(inventory);
					for(Element i : p.getChild("playerInventory").getChildren()){
						this.addInventoryItem(gameData.getItem(i.getText()));
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