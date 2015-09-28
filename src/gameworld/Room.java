package gameworld;

public class Room {
	
	private final int height;
	private final int width;
	private String name;
	private String description;
	
	public Room(String name, String description, int height, int width ) {
		this.name = name;
		this.description = description;
		this.height = height;
		this.width = width;
	}
	
	/*
	 * Get Height of room
	 * @return 
	 */
	public int getHeight() {
		return height;
	}
	
	/*
	 * Get Width of room
	 * @return 
	 */
	public int getWidth() {
		return width;
	}
	
	/*
	 * Describes this room
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	
	/*
	 * Get name of room
	 * @return
	 */
	public String getName() {
		return name;
	}
	
}
