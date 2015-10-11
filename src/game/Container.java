package game;

import game.Drawable.Point3D;
import game.Room.ItemInstance;
import game.storage.GameData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Container extends Item implements Pickable {
	private List<Item> containerItems;
	private boolean hasOpened;

	public Container(String id, String name, String description, String spriteName) {
		super(id, name, description, spriteName);
		this.containerItems = new ArrayList<>();
		this.hasOpened = false;
	}

	public String mathQuiz() {
		return "Whats 2 + 5?";
	}

	public List<Item> getItems() {
		return containerItems;
	}

	public void openChest() {
		hasOpened = true;
	}

    @Override
    public List<Action> getAllowedActions() {
        List<Action> list = super.getAllowedActions();
        list.add(0, Action.SEARCH);
        list.add(0, Action.PICK_UP); //TODO remove (this one is just for testing)
        return list;
    }

	@Override
	public void toXML(Document gameDoc) {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("/u/students/holdawscot/saveFile1.xml");
		try{
			Document document = builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			for(Element container : rootNode.getChild("gameRooms").getChildren()){
				if(container.getChildText("id").equals(this.getID())){
					container.getChild("containerItems").removeContent();
					for(Item i : this.containerItems){
						container.getChild("containerItems").addContent("item").setText(i.getID());
					}
					container.getChild("hasOpened").setText(Boolean.toString(this.hasOpened));
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
			for(Element gameItem : rootNode.getChild("gameItems").getChildren()){
				if(gameItem.getChildText("id").equals(this.getID())){
					this.containerItems.removeAll(containerItems);
					for(Element chestItem : gameItem.getChild("chestItems").getChildren()){
						this.getItems().add(gameData.getItem(chestItem.getText()));
					}
					this.hasOpened = Boolean.getBoolean(gameItem.getChildText("hasOpened"));
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
