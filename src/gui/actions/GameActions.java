package gui.actions;

import game.*;
import gui.renderer.Door;

public class GameActions {
    public static class Turn extends Action {
        public final Direction direction;

        public Turn(Player player, Direction direction) {
            super(player, "Turn");

            this.direction = direction;
        }
    }

    public static class PickUp extends Action {
        public final Room.ItemInstance target;

        public PickUp(Player player, Room.ItemInstance target) {
            super(player, "Pick up");

            this.target = target;
        }
    }

    public static class GoThrough extends Action {
        public final Door door;

        public GoThrough(Player player, Door door) {
            super(player, "Go through");

            this.door = door;
        }
    }

    public static class Take extends Action {
        public final Container container;
        public final Item takenItem;

        public Take(Player player, Container container, Item takenItem) {
            super(player, "Take");

            this.container = container;
            this.takenItem = takenItem;
        }
    }
}
