package game;

import game.Drawable.Point3D;
import game.Room.ItemInstance;
import game.storage.GameData;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Container extends Item implements Pickable {
	private List<Item> chestItems;
	private boolean hasOpened;

	public Container(String name, String description, String spriteName) {
		super(name, description, spriteName);
		this.chestItems = new ArrayList<>();
		this.hasOpened = false;
	}

	public String mathQuiz() {
		return "Whats 2 + 5?";
	}

	public List<Item> getItems() {
		return chestItems;
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
	public void toXML() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object loadXML(GameData gameData) {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("resources/mainGame.xml");
		try{
			Document document = builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			for(Element gameItem : rootNode.getChild("gameItems").getChildren()){
				if(gameItem.getChildText("name").equals(this.getName())){
					this.chestItems.removeAll(chestItems);
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
