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

    public static Container createContainer(Item contents) {
        Container result = new Container("testItem" + itemCount, "Container (Item " + itemCount + ")", "Test container", "");
        result.addItem(contents);
        return result;
    }

    public static Weapon createWeapon() {
        return new Weapon("testItem" + itemCount, "Weapon (Item " + itemCount + ")", "Test weapon", "");
    }

    public static Monster createMonsterWithWeakness(Weapon weapon) {
        return new Monster("testItem" + itemCount, "Weapon (Item " + itemCount + ")", "Test weapon", "", weapon);
    }
}
