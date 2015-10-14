package gui.actions;

import game.*;
import gui.renderer.Door;
import gui.renderer.InvisibleDoor;
import gui.renderer.VisibleDoor;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Actions related to the game state itself
 *
 * @author Jono Mingard
 */
public class GameActions {
    /**
     * Base class for actions which can be "applied", actually modifying the game state, and serialized into a string
     * for network transfer
     */
    public static abstract class GameAction extends Action {
        /**
         * Create an Action from a serialized string
         *
         * @param data a string created using {@link #serialize()}
         * @param game the game to load the action onto (will be modified when the action is applied)
         * @return the resulting action
         */
        public static GameAction deserialize(String data, Game game) {
            Map<String, String> values = new HashMap<>();

            String[] pairs = data.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                values.put(keyValue[0], keyValue[1]);
            }

            String subclassName = values.get("class");
            for (Class cls : GameActions.class.getDeclaredClasses()) {
                if (cls.getSimpleName().equals(subclassName)) {
                    try {
                        return (GameAction)cls.getMethod("deserialize", Map.class, Game.class).invoke(data, values, game);
                    }
                    catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassCastException e) {
                        System.out.println("Deserializing action failed: " + e.getMessage());
                        System.out.println(data);
                    }
                }
            }

            return null;
        }

        /**
         * Create a new game action
         *
         * @param player player performing the action
         * @param name   human-readable name for the action
         */
        public GameAction(Player player, String name) {
            super(player, name);
        }

        /**
         * Applies the action to its associated game. Returns true if the action succeeded, or false if the action
         * could not be performed
         *
         * @return whether the action was successful
         */
        public abstract boolean apply();

        /**
         * Turns the action into text which can be used as a URL query string and deserialized into another game
         *
         * @return the serialized action
         */
        public abstract String serialize();

        protected String serialize(Map<String, String> extraValues) {
            try {
                List<String> pairs = new ArrayList<>();
                for (Map.Entry<String, String> entry : extraValues.entrySet()) {
                    pairs.add(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
                }

                pairs.add("player=" + URLEncoder.encode(player.getName(), "UTF-8"));
                pairs.add("class=" + getClass().getSimpleName());
                return String.join("&", pairs);
            }
            catch (UnsupportedEncodingException e) {
                return "";
            }
        }
    }

    /**
     * Action representing the player moving to a particular point inside their current room
     */
    public static class Turn extends GameAction {
        public static GameAction deserialize(Map<String, String> data, Game game) {
            return new Turn(game.getPlayer(data.get("player")), Direction.fromString(data.get("direction")));
        }

        public final Direction direction;

        public Turn(Player player, Direction direction) {
            super(player, "Turn");

            this.direction = direction;
        }

        @Override
        public boolean apply() {
            return player.turn(direction);
        }

        @Override
        public String serialize() {
            Map<String, String> values = new HashMap<>();
            values.put("direction", direction.toString());
            return super.serialize(values);
        }
    }

    /**
     * Action representing the player moving to another room through a door
     */
    public static class GoThrough extends GameAction {
        public static GameAction deserialize(Map<String, String> data, Game game) {
            Door door = new InvisibleDoor(game.getRoom(data.get("roomID")), Direction.fromString(data.get("direction")));
            return new GoThrough(game.getPlayer(data.get("player")), door);
        }

        public final Door door;

        public GoThrough(Player player, Door door) {
            super(player, "Enter");

            this.door = door;
        }

        @Override
        public boolean apply() {
            return player.move(door.getLinkDirection());
        }

        @Override
        public String serialize() {
            Map<String, String> values = new HashMap<>();
            values.put("roomID", door.getRoom().getID());
            values.put("direction", door.getLinkDirection().toString());
            return super.serialize(values);
        }
    }

    /**
     * Action representing the player picking up an item from their current room
     */
    public static class PickUp extends GameAction {
        public static GameAction deserialize(Map<String, String> data, Game game) {
            return new PickUp(
                    game.getPlayer(data.get("player")),
                    (Holdable)game.getItem(data.get("target")),
                    (Container)game.getItem(data.get("container"))
            );
        }

        public final Holdable target;
        public final Container container;

        public PickUp(Player player, Holdable target, Container container) {
            super(player, "Pick up");

            this.target = target;
            this.container = container;
        }

        @Override
        public boolean apply() {
            return player.pickUp(target, container);
        }

        @Override
        public String serialize() {
            Map<String, String> values = new HashMap<>();
            values.put("target", target.getID());
            values.put("container", container.getID());
            return super.serialize(values);
        }
    }

    /**
     * Represents the player dropping an item (optionally into a container)
     */
    public static class Drop extends GameAction {
    	public final Container target;
    	
		public Drop(Player player, Container target) {
			super(player, "Drop Item");
			this.target = target;
		}

		@Override
		public boolean apply() {
			if (player.dropItem(target) != null) {
				return true;
			}
			return false;
		}

		@Override
		public String serialize() {
			Map<String, String> values = new HashMap<>();
			values.put("target", target.getID());
			return super.serialize(values);
		}
    	
    }

    /**
     * Action representing the player attacking a monster
     */
    public static class Attack extends GameAction {
        public static GameAction deserialize(Map<String, String> data, Game game) {
            return new Attack(
                    game.getPlayer(data.get("player")),
                    (Monster)game.getItem(data.get("monster"))
            );
        }

        public final Monster monster;

        public Attack(Player player, Monster monster) {
            super(player, "Attack");

            this.monster = monster;
        }

        @Override
        public boolean apply() {
            monster.fight(player);

            // Even if the player died, the action technically succeeded
            return true;
        }

        @Override
        public String serialize() {
            Map<String, String> values = new HashMap<>();
            values.put("monster", monster.getID());
            return super.serialize(values);
        }
    }

    /**
     * Action representing the player interacting with the control panel to win the game
     */
    public static class Interact extends GameAction {
        public static GameAction deserialize(Map<String, String> data, Game game) {
            return new Attack(
                    game.getPlayer(data.get("player")),
                    (Monster)game.getItem(data.get("controlPanel"))
            );
        }

        public final ControlPanel controlPanel;

        public Interact(Player player, ControlPanel controlPanel) {
            super(player, "Interact");

            this.controlPanel = controlPanel;
        }

        @Override
        public boolean apply() {
            controlPanel.interact(player);

            // Even if the player died, the action technically succeeded
            return true;
        }

        @Override
        public String serialize() {
            Map<String, String> values = new HashMap<>();
            values.put("controlPanel", controlPanel.getID());
            return super.serialize(values);
        }
    }
}
