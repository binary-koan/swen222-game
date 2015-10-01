package game;

public class Door extends Item  {

	private Room targetRoom;
	
	public Door(String name, String spriteName) {
		super(name, spriteName);
	}

	public Room getTargetRoom() {
		return targetRoom;
	}

	public void setTargetRoom(Room targetRoom) {
		this.targetRoom = targetRoom;
	}
	
	

}
