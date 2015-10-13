package test.game;

import game.*;
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
        Room room = MockRoom.create(null);
        Player player = MockPlayer.create(room);

        assertFalse(player.move(Direction.NORTH));
    }

    @Test
    public void testMovement_key() {
        Room room1 = MockRoom.create(Key.Color.BLUE);
        Room room2 = MockRoom.createWithConnection(Direction.NORTH, room1);
        Player player = MockPlayer.create(room2);
        player.pickUp(MockItem.createKey(Key.Color.BLUE));

        assertTrue(player.move(Direction.NORTH));
    }

    @Test
    public void testMovement_noKey() {
        Room room1 = MockRoom.create(Key.Color.BLUE);
        Room room2 = MockRoom.createWithConnection(Direction.NORTH, room1);
        Player player = MockPlayer.create(room2);

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
    public void testPickUpItem_removeFromRoom() {
        Item item = MockItem.createKey();
        Room room = MockRoom.createWithItem(item);
        Player player = MockPlayer.create(room);

        assertTrue(player.pickUp(item, room));
        assertFalse(room.containsItem(item));
    }

    @Test
    public void testPickUpItem_removeFromContainer() {
        Item item = MockItem.createKey();
        Container container = MockItem.createContainer(item);
        Player player = MockPlayer.create();

        assertTrue(player.pickUp(item, container));
        assertFalse(container.containsItem(item));
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
        Player player = MockPlayer.createWithHeldItem(MockItem.createKey());
        Item item = player.getHeldItem();

        assertEquals(item, player.dropItem());
        assertTrue(player.getRoom().containsItem(item));
    }

    @Test
    public void testDropItem_notHoldingAnything() {
        Player player = MockPlayer.create();

        assertNull(player.dropItem());
        assertFalse(player.getRoom().containsItem(null));
    }
}
