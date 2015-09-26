package client.renderer;

import game.Player;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ResourceLoader {
    private String root;
    private Map<String, Image> imageCache = new HashMap<>();

    public ResourceLoader(String root) {
        this.root = root;
    }

    public Image getImage(String filename) {
        //TODO
        return null;
    }

    public Image getSprite(String filename, Player.Position viewingDirection) {
        //TODO
        return null;
    }
}
