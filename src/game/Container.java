package game;

import java.util.*;

import org.jdom2.Element;

public class Container extends Item {
	private List<Holdable> containerItems;

	public Container(String id, String name, String description, String spriteName) {
		super(id, name, description, spriteName);
		this.containerItems = new ArrayList<>();
	}

	public List<Holdable> getItems() {
		return containerItems;
	}

	@Override
	public Element toXML() {
		Element container = super.toXML();
   		container.addContent(new Element("containerItems"));
   		for(Item item : this.containerItems) {
   			Element containerItem = new Element("item").setText(item.getID());
   			container.getChild("containerItems").addContent(containerItem);
   		}
   		return container;
	}

	@Override
	public void loadXML(Game game, Element objectElement) {
		super.loadXML(game, objectElement);
		this.containerItems.removeAll(containerItems);
		for(Element containerItem : objectElement.getChild("containerItems").getChildren()) {
            try {
                System.out.println(containerItem.getText());
                this.containerItems.add((Holdable) game.getItem(containerItem.getText()));
            }
            catch (ClassCastException e) {
                System.out.println('s');
            }
		}
	}
}
