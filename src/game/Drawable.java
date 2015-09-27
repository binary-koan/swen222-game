package game;

import game.Player;

import java.awt.*;

public interface Drawable {
    class BoundingCube {
        public final int x;
        public final int width;
        public final int y;
        public final int height;
        public final int z;
        public final int depth;

        public BoundingCube(int x, int y, int z, int width, int height, int depth) {
            this.x = x;
            this.width = width;
            this.y = y;
            this.height = height;
            this.z = z;
            this.depth = depth;
        }
    }

    Direction getFacingDirection();
    BoundingCube getBoundingCube();
    String getSpriteName();
}
