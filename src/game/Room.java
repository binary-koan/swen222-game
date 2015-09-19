package game;

import java.awt.*;
import java.util.List;

public class Room {
    private Image wallTexture;
    private List<Item> items;

    public Image getWallTexture() {
        return wallTexture;
    }

    public Room getConnection(Player.Position position) {
        return null;
    }

    public List<Item> getItems() {
        return items;
    }
}
