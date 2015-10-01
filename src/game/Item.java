package game;

public class Item {
	private String name;
	private String spriteName;
	private Room room;

	public Item(String name, String spriteName){
		this.name = name;
		this.spriteName = spriteName;
	}

	public String getSpriteName() {
		return spriteName;
	}

	public String getName() {
		return this.name;
	}

	public void setRoom(Room room){
		this.room = room;
	}

	public Room getRoom(){
		return this.room;
	}

}
