package game;

import java.util.ArrayList;
import java.util.List;

public class Room {
    public static int ROOM_SIZE = 160;
    public static int CEILING_HEIGHT = 96;

    public class ItemInstance implements Drawable {
        private Item item;
        private Direction facingDirection;
        private Point3D position;

        public ItemInstance(Item item, Direction facingDirection, Point3D position) {
            this.item = item;
            this.facingDirection = facingDirection;
            this.position = position;
        }

		@Override
		public Direction getFacingDirection() {
			return facingDirection;
		}

        @Override
        public Point3D getPosition() {
            return position;
        }

        @Override
        public String getSpriteName() {
            return item.getSpriteName();
        }

        public Item getItem() {
        	return item;
        }
    }

	private String name;
    private String wallImage;
    private List<ItemInstance> items = new ArrayList<>();
    private List<Player> players;

    public Room(String name) {
    	this.name = name;
    	players = new ArrayList<>();
    }

    public String getWallImage() {
        return wallImage;
    }

    public Room getConnection(Direction position) {
        return null;
    }

    public List<ItemInstance> getItems() {
        return items;
    }

    public String getName(){
    	return name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player){
    	players.add(player);
    }

    public void removePlayer(Player player){
    	players.remove(player);
    }

    public void addRoomItemInstance(ItemInstance item){
    	items.add(item);
    }

    public void removeRoomItemInstance(ItemInstance item){
    	items.remove(item);
    }
}
