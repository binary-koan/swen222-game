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
	public Direction getFacingDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoundingCube getBoundingCube() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
