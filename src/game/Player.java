package game;

import client.renderer.Drawable;

public class Player implements Drawable {
    private Room room;
    private Position position;

    public Room getRoom() {
        return room;
    }

    public Position getPosition() {
        return position;
    }

    public enum Position {
        ;

        public Position opposite() {
            return null;
        }
    }
}
