package test.game.mocks;

import game.Holdable;
import game.Player;
import game.Room;
import game.StateChangeListener;

import java.util.HashMap;
import java.util.Map;

public class MockPlayer {
    private static class Listener implements StateChangeListener {
        @Override
        public void onStateChanged(Player player, Type type, String message) {
            lastEventTypes.put(player, type);
        }
    }

    private static int playerCount = 0;

    private static StateChangeListener playerListener = new Listener();
    private static Map<Player, StateChangeListener.Type> lastEventTypes = new HashMap<>();

    public static StateChangeListener.Type getLastEventType(Player player) {
        return lastEventTypes.get(player);
    }

    public static Player create() {
        return create(MockRoom.create(null));
    }

    public static Player create(Room room) {
        playerCount++;
        Player player = new Player("Player " + playerCount, "", room);
        player.addStateChangeListener(playerListener);
        return player;
    }

    public static Player createWithHeldItem(Holdable item) {
        Player player = create();
        player.pickUp(item, null);
        return player;
    }
}
