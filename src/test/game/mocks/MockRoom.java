package test.game.mocks;

import game.Direction;
import game.Item;
import game.Key;
import game.Room;

public class MockRoom {
    private static int roomCount = 0;

    public static Room create(Key.Color color) {
        roomCount++;
        return new Room("testRoom" + roomCount, "Test Room " + roomCount, color);
    }

    public static Room createWithConnection(Direction connectionDirection) {
        Room room1 = create(null);
        Room room2 = create(null);
        room1.setConnection(connectionDirection, room2);
        room2.setConnection(connectionDirection.opposite(), room1);
        return room1;
    }

    public static Room createWithConnection(Direction connectionDirection, Room room2) {
        Room room1 = create(null);
        room1.setConnection(connectionDirection, room2);
        room2.setConnection(connectionDirection.opposite(), room1);
        return room1;
    }

    public static Room createWithItem(Item item) {
        Room room = create(null);
        room.addItem(item);
        return room;
    }
}
