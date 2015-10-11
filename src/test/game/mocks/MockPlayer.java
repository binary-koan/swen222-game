package test.game.mocks;

import game.Player;
import game.Room;

public class MockPlayer {
    private static int playerCount = 0;

    public static Player create(Room room) {
        playerCount++;
        return new Player("Player " + playerCount, "", room);
    }

    private MockPlayer() { }
}
