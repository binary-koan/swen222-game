package game;

public abstract class Item {
	private String name;
	private String spriteName;
	private boolean canBePickedUp;

	public Item(String name, String spriteName, boolean canBePickedUp){
		this.name = name;
		this.spriteName = spriteName;
		this.canBePickedUp = canBePickedUp;
	}

	public String getSpriteName() {
		return spriteName;
	}

	public String getName() {
		return this.name;
	}

	public boolean pickedUp() {
		return canBePickedUp;
	}
}
