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
     * Base class for actions which can be "applied", actually modifying the game state
     */
    public static abstract class GameAction extends Action {
        /**
         * Create a new game action
         *
         * @param player player performing the action
         * @param name   human-readable name for the action
         */
        public GameAction(Player player, String name) {
            super(player, name);
        }

        public abstract boolean apply();
    }

    /**
     * Action representing the player moving to a particular point inside their current room
     */
    public static class Turn extends GameAction {
        public final Direction direction;

        public Turn(Player player, Direction direction) {
            super(player, "Turn");

            this.direction = direction;
        }

        @Override
        public boolean apply() {
            return player.turn(direction);
        }
    }

    /**
     * Action representing the player moving to another room through a door
     */
    public static class GoThrough extends GameAction {
        public final Door door;

        public GoThrough(Player player, Door door) {
            super(player, "Enter");

            this.door = door;
        }

        @Override
        public boolean apply() {
            return player.move(door.getLinkDirection());
        }
    }

    /**
     * Action representing the player picking up an item from their current room
     */
    public static class PickUp extends GameAction {
        public final Item target;
        public final Container container;

        public PickUp(Player player, Item target, Container container) {
            super(player, "Pick up");

            this.target = target;
            this.container = container;
        }

        @Override
        public boolean apply() {
            return player.pickUp(target, container);
        }
    }

    /**
     * Action representing the player attacking a monster
     */
    public static class Attack extends GameAction {
        public final Monster monster;

        public Attack(Player player, Monster monster) {
            super(player, "Attack");

            this.monster = monster;
        }

        @Override
        public boolean apply() {
            return monster.fight(player);
        }
    }
}
