package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Room {
    public static int ROOM_SIZE = 160;
    public static int CEILING_HEIGHT = 96;

    public class ItemInstance implements Drawable {
        private Item item;
        private Direction facingDirection;
        private Drawable.BoundingCube boundingCube;

        public ItemInstance(Item item, Direction facingDirection, BoundingCube boundingCube) {
            this.item = item;
            this.facingDirection = facingDirection;
            this.boundingCube = boundingCube;
        }

		@Override
		public Direction getFacingDirection() {
			return facingDirection;
		}

        @Override
        public BoundingCube getBoundingCube() {
            return boundingCube;
        }

        @Override
        public String getSpriteName() {
            return item.getSpriteName();
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
}
