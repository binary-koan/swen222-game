package test.game.mocks;

import game.Player;
import game.Room;

public class MockPlayer {
    private static int playerCount = 0;

    public static Player create() {
        playerCount++;
        return new Player("Player " + playerCount, "");
    }

    public static Player createInRoom(Room room) {
        Player player = create();
        player.setRoom(room);
        return player;
    }

    private MockPlayer() { }
}
