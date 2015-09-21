package game;

import client.renderer.Drawable;
import client.renderer.RoomRenderer;

import java.awt.*;

public class Player implements Drawable {
    private Room room;
    private Position position;

    public Room getRoom() {
        return room;
    }

    public Position getPosition() {
        return position;
    }

    public void addMovementListener(MovementListener movementListener) {
    }

    @Override
    public BoundingCube getBoundingCube() {
        return null;
    }

    @Override
    public Image getSprite(Position position) {
        return null;
    }

    public enum Position {
        NORTH, SOUTH, EAST, WEST;

        public Position opposite() {
            return null;
        }
    }
}
