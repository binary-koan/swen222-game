package gui.actions;

import game.*;
import gui.renderer.Door;

/**
 * Actions related to the game state itself
 *
 * @author Jono Mingard
 */
public class GameActions {
    /**
     * Action representing the player moving to a particular point inside their current room
     */
    public static class Turn extends Action {
        public final Direction direction;

        public Turn(Player player, Direction direction) {
            super(player, "Turn");

            this.direction = direction;
        }
    }

    /**
     * Action representing the player moving to another room through a door
     */
    public static class GoThrough extends Action {
        public final Door door;

        public GoThrough(Player player, Door door) {
            super(player, "Enter");

            this.door = door;
        }
    }

    /**
     * Action representing the player picking up an item from their current room
     */
    public static class PickUp extends Action {

        public final Room.ItemInstance target;
        public PickUp(Player player, Room.ItemInstance target) {
            super(player, "Pick up");

            this.target = target;
        }

    }

    /**
     * Action representing the player taking an item out of a container
     */
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
