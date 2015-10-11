package game;

import game.Drawable.Point3D;
import game.Room.ItemInstance;

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
	public Element toXML() {
		Element container = super.toXML();
   		container.addContent(new Element("containerItems"));
   		for(Item item : this.containerItems){
   			Element containerItem = new Element("item").setText(item.getID());
   			container.getChild("containerItems").addContent(containerItem);
   		}
   		container.addContent(new Element("hasOpened").setText(Boolean.toString(this.hasOpened)));
   		return container;
	}

	@Override
	public void loadXML(Game game, Element objectElement) {
		super.loadXML(game, objectElement);
		this.containerItems.removeAll(containerItems);
		//if(objectElement.getChild("containerItems").getChildren() != null){
			for(Element containerItem : objectElement.getChild("containerItems").getChildren()){
				this.containerItems.add(game.getItem(containerItem.getText()));
			}
		//}
		this.hasOpened = Boolean.getBoolean(objectElement.getChildText("hasOpened"));
	}
}
