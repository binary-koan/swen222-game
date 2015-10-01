package game;

public class Chest extends Item{

	private Player holder; //Which player currently holds this item 
	
	public Chest(String name, String spriteName) {
		super(name, spriteName);
	}
	
	public void addHolder(Player p) {
		if (holder == null) {
			holder = p;
		} else {
			System.out.println("A player alredy holds this item"); 
		}
	}
	
	public Player getHolder() {
		return holder; 
	}
	
	

}
