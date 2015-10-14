package test.game.mocks;

import game.Holdable;
import game.Player;
import game.Room;

public class MockPlayer {
    private static int playerCount = 0;

    public static Player create() {
        return create(MockRoom.create(null));
    }

    public static Player create(Room room) {
        playerCount++;
        return new Player("Player " + playerCount, "", room);
    }

    public static Player createWithHeldItem(Holdable item) {
        Player player = create();
        player.pickUp(item, null);
        return player;
    }
}
