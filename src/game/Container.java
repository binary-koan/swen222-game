package game;

import java.util.*;

import org.jdom2.Element;

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
			for(Element containerItem : objectElement.getChild("containerItems").getChildren()){
				this.containerItems.add(game.getItem(containerItem.getText()));
			}
		this.hasOpened = Boolean.getBoolean(objectElement.getChildText("hasOpened"));
	}
}
