package test.game.mocks;

import game.Item;
import game.Key;
import game.Pickable;

public class MockItem {
    private static int itemCount = 0;

    public static Item createKey() {
        itemCount++;
        return new Key("testItem" + itemCount, "Key (Item " + itemCount + ")", "Test key", "");
    }
}
