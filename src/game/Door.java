package game;

public class Door extends Item  {

	private Room targetRoom;

	public Door(String name, String spriteName) {
		super(name, spriteName, false);
	}

	public Room getTargetRoom() {
		return targetRoom;
	}

	public void setTargetRoom(Room targetRoom) {
		this.targetRoom = targetRoom;
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadXML(String xml) {
		// TODO Auto-generated method stub

	}
}
