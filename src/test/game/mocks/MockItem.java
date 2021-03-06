package test.game.mocks;

import game.*;

public class MockItem {
    private static int itemCount = 0;

    public static Key createKey() {
        return createKey(Key.Color.RED);
    }

    public static Key createKey(Key.Color color) {
        itemCount++;
        return new Key("testItem" + itemCount, "Key (Item " + itemCount + ")", "Test key", color);
    }

    public static Container createContainer(Holdable contents) {
        Container result = new Container("testItem" + itemCount, "Container (Item " + itemCount + ")", "Test container", "");
        result.getItems().add(contents);
        return result;
    }

    public static Weapon createWeapon() {
        return new Weapon("testItem" + itemCount, "Weapon (Item " + itemCount + ")", "Test weapon", "");
    }

    public static Monster createMonsterWithWeakness(Room room, Weapon weapon) {
        Monster monster = new Monster("testItem" + itemCount, "Weapon (Item " + itemCount + ")", "Test weapon", "", weapon);
        room.addItem(monster);
        return monster;
    }
}
