package game;

public class Item implements Drawable {
	private String name;

	public Item(String name){
		this.name = name;
	}

	@Override
	public BoundingCube getBoundingCube() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSpriteName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

}
