package game;

public class Chest extends Item{

	
	public Chest(String name, String spriteName) {
		super(name, spriteName, false);
	}

	public String quiz() {
		return "Whats 2 + 5?";
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
