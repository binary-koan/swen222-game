package gui.actions;

import game.*;
import gui.renderer.Door;
import gui.renderer.InvisibleDoor;
import gui.renderer.VisibleDoor;

import java.lang.reflect.InvocationTargetException;
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
                        return (GameAction)cls.getMethod("deserialize", Map.class, Game.class).invoke(data, game);
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

        public abstract boolean apply();

        public abstract String serialize();

        public String serialize(Map<String, String> extraValues) {
            List<String> pairs = new ArrayList<>();
            for (Map.Entry<String, String> entry : extraValues.entrySet()) {
                pairs.add(entry.getKey() + "=" + entry.getValue());
            }

            pairs.add("player=" + player.getName());
            return String.join("&", pairs);
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
            return monster.fight(player);
        }

        @Override
        public String serialize() {
            Map<String, String> values = new HashMap<>();
            values.put("monster", monster.getID());
            return super.serialize(values);
        }
    }
}
