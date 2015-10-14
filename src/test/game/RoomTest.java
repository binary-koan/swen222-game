package test.game;

import game.Key;
import game.Player;
import game.Room;
import org.junit.Test;
import test.game.mocks.MockItem;
import test.game.mocks.MockPlayer;
import test.game.mocks.MockRoom;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RoomTest {
    @Test
    public void testAllowEntry_basic() {
        Room room = MockRoom.create(null);
        Player player = MockPlayer.create();

        assertTrue(room.allowsEntry(player, null));
    }

    @Test
    public void testAllowEntry_holdingKey() {
        Room room = MockRoom.create(Key.Color.RED);
        Player player = MockPlayer.createWithHeldItem(MockItem.createKey(Key.Color.RED));

        assertTrue(room.allowsEntry(player, null));
    }

    @Test
    public void testAllowEntry_wrongColorKey() {
        Room room = MockRoom.create(Key.Color.RED);
        Player player = MockPlayer.createWithHeldItem(MockItem.createKey(Key.Color.BLUE));

        assertFalse(room.allowsEntry(player, null));
    }

    @Test
    public void testAllowEntry_noKey() {
        Room room = MockRoom.create(Key.Color.RED);
        Player player = MockPlayer.create();

        assertFalse(room.allowsEntry(player, null));
    }
}
