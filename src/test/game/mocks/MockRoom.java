package test.game.mocks;

import game.Direction;
import game.Room;

public class MockRoom {
    private static int roomCount = 0;

    public static Room create() {
        roomCount++;
        return new Room("testRoom" + roomCount, "Test Room " + roomCount);
    }

    public static Room createWithConnection(Direction connectionDirection) {
        Room room1 = create();
        Room room2 = create();
        room1.roomConnections.put(connectionDirection, room2);
        room2.roomConnections.put(connectionDirection.opposite(), room1);
        return room1;
    }
}
