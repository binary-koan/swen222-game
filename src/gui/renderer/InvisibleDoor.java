package gui.renderer;

import game.Direction;

public class InvisibleDoor extends Door {
    public InvisibleDoor(Direction facingDirection) {
        super(facingDirection);
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
