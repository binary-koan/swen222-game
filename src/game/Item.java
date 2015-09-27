package game;

public class Item {
	private String name;
	private String spriteName;
	private Player holder;
	private Room room;
	private Direction facingDirection;

	public Item(String name, String spriteName){
		this.name = name;
		this.spriteName = spriteName;
	}

	public String getSpriteName() {
		// TODO Auto-generated method stub
		return spriteName;
	}

	public String getName() {
		return this.name;
	}

	public void setHolder(Player holder){
		this.holder = holder;
	}

	public Player getHolder() {
		return holder;
	}

	public void setRoom(Room room){
		this.room = room;
	}

	public Room getRoom(){
		return this.room;
	}

	public void setFacingDirection(Direction facingDirection){
		this.facingDirection = facingDirection;
	}

	public Direction getFacingDirection(){
		return this.facingDirection;
	}

}
