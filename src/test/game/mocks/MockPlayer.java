package test.game.mocks;

import game.Item;
import game.Player;
import game.Room;

public class MockPlayer {
    private static int playerCount = 0;

    public static Player create() {
        return create(MockRoom.create());
    }

    public static Player create(Room room) {
        playerCount++;
        return new Player("Player " + playerCount, "", room);
    }

    public static Player createWithHeldItem() {
        Player player = create();
        Item item = MockItem.createKey();
        player.pickUp(item);
        return player;
    }
}
