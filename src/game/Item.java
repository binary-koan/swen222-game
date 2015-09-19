package game;

import client.renderer.Drawable;

import java.awt.*;

public class Item implements Drawable {
    public class BoundingBox {
        public boolean contains(Point point) {
            return false;
        }
    }
}
