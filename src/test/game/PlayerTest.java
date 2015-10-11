package test.game;

import game.Direction;
import game.Item;
import game.Player;
import game.Room;
import org.junit.Test;
import test.game.mocks.MockItem;
import test.game.mocks.MockPlayer;
import test.game.mocks.MockRoom;

import static org.junit.Assert.*;

public class PlayerTest {
    @Test
    public void testMovement_valid() {
        Room room = MockRoom.createWithConnection(Direction.NORTH);
        Player player = MockPlayer.create(room);

        assertTrue(player.move(Direction.NORTH));
        assertEquals(room.getConnection(Direction.NORTH), player.getRoom());

        assertTrue(player.move(Direction.SOUTH));
        assertEquals(room, player.getRoom());
    }

    @Test
    public void testMovement_invalidRoom() {
        Room room = MockRoom.create();
        Player player = MockPlayer.create(room);

        assertFalse(player.move(Direction.NORTH));
    }

    @Test
    public void testPickUpItem_valid() {
        Item item = MockItem.createKey();
        Player player = MockPlayer.create();

        assertTrue(player.pickUp(item));
        assertEquals(item, player.getHeldItem());
    }

    @Test
    public void testPickUpItem_alreadyHolding() {
        Item item = MockItem.createKey();
        Item item2 = MockItem.createKey();
        Player player = MockPlayer.create();

        assertTrue(player.pickUp(item));
        assertFalse(player.pickUp(item2));
        assertEquals(item, player.getHeldItem());
    }

    @Test
    public void testDropItem_valid() {
        Player player = MockPlayer.createWithHeldItem();
        Item item = player.getHeldItem();

        assertEquals(item, player.dropItem());
    }

    @Test
    public void testDropItem_notHoldingAnything() {
        Player player = MockPlayer.create();

        assertNull(player.dropItem());
        for (Room.ItemInstance instance : player.getRoom().getItems()) {
            assertNotNull(instance.getItem());
        }
    }
}
