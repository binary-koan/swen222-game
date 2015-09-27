package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Room {
    public class ItemInstance implements Drawable {
        private Item item;
        private Direction facingDirection;
        private Drawable.BoundingCube boundingCube;

        public ItemInstance(Item item, Direction facingDirection, Drawable.BoundingCube boundingCube) {
            this.item = item;
            this.facingDirection = facingDirection;
            this.boundingCube = boundingCube;
        }

		@Override
		public Direction getFacingDirection() {
			// TODO Auto-generated method stub
			return null;
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
    private Image wallTexture;
    private List<ItemInstance> items;
    private int size;
    private List<Player> players;

    public Room(String name){
    	this.name = name;
    	players = new ArrayList<Player>();
    }

    public Image getWallTexture() {
        return wallTexture;
    }

    public Room getConnection(Direction position) {
        return null;
    }

    public List<ItemInstance> getItems() {
        return items;
    }

    public int getSize() {
        return size;
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
