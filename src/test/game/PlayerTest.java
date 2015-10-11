package test.game;

import game.Direction;
import game.Player;
import game.Room;
import org.junit.Test;
import test.game.mocks.MockPlayer;
import test.game.mocks.MockRoom;

import static org.junit.Assert.*;

public class PlayerTest {
    @Test
    public void testMovement() {
        Room room = MockRoom.createWithConnection(Direction.NORTH);
        Player player = MockPlayer.create(room);

        try {
            player.move(Direction.NORTH);
            assertEquals(room.getConnection(Direction.NORTH), player.getRoom());

            player.move(Direction.SOUTH);
            assertEquals(room, player.getRoom());
        }
        catch (Player.InvalidMovementException e) {
            fail("Player could not move: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidMovement() {
        Room room = MockRoom.create();
        Player player = MockPlayer.create(room);

        try {
            player.move(Direction.NORTH);
            fail("Player moved to a nonexistent room");
        }
        catch (Player.InvalidMovementException e) { }
    }
}
