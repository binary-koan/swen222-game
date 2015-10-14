package gui.renderer;

import game.Direction;
import game.Room;

public class InvisibleDoor extends Door {
    public InvisibleDoor(Room room, Direction linkDirection) {
        super(room, linkDirection);
    }

    @Override
    public String getName() {
        return "Passage";
    }

    @Override
    public Point3D getPosition() {
        return null;
    }

    @Override
    public String getSpriteName() {
        return null;
    }
}
