package gameworld;

import game.Direction;

public class Player extends MoveableCharacter {

	private String name;
	private Room room;
	private Direction facingDirection;
	
	public Player(int x, int y, String name) {
		super(x, y);
		this.name = name;
	}
	
	public Room getRoom() throws NullPointerException  {
		try {
			return room;
		} catch (Exception e) {
			System.out.println("Player not assigned to room");
		}
		return null;
	}
	
	public void setRoom(Room room) {
		this.room = room;
		
	}
	public String getPlayerName() {
		return name;
	}

	@Override
	public Direction getFacingDirection() {
		return facingDirection;
		
	}
	
	public void setFacingDirection(Direction facingDirection) {
		this.facingDirection = facingDirection;
	}

	@Override
	public BoundingCube getBoundingCube() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSpriteName() {
		return "!@##@$";
	}

}
