package game;

import org.jdom2.Element;

public class Key extends Item implements Pickable {
	public enum Color {
		BLUE, GREEN, YELLOW, RED;

		public static Color fromString(String color){
			if(color.equals("BLUE")){
				return BLUE;
			}
			else if(color.equals("GREEN")){
				return GREEN;
			}
			else if(color.equals("YELLOW")){
				return YELLOW;
			}
			else if(color.equals("RED")){
				return RED;
			}
			else if(color.equals("null")){
				return null;
			}
			return BLUE;
		}
	}

	private Color color;

	public Key(String id, String name, String description, Color color) {
		super(id, name, description, "objects/key-" + color.toString().toLowerCase() + ".png");

		this.color = color;
	}

	public Color getColor(){
		return this.color;
	}

	@Override
	public Element toXML() {
		Element itemElement = super.toXML();
		if(this.color != null){
			itemElement.addContent(new Element("color").setText(this.color.toString()));
		}
		return itemElement;
	}

	@Override
	public void loadXML(Game game, Element objectElement) {
		super.loadXML(game, objectElement);
		if(objectElement.getChildText("color") != null) {
			this.color = Color.fromString(objectElement.getChildText("color"));
		}
	}
}
