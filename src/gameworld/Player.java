package gameworld;


import game.Direction;

public class Player extends MoveableCharacter {

	private String name;
	private Room room;
	
	public Player(int x, int y, Direction d, String name) {
		super(x, y, d);
		this.name = name;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public String getPlayerName() {
		return name;
	}

}
