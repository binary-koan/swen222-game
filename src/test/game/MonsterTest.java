package test.game;

import game.Monster;
import game.Player;
import game.Weapon;
import org.junit.Test;
import test.game.mocks.MockItem;
import test.game.mocks.MockPlayer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MonsterTest {
    @Test
    public void testFight_correctWeapon() {
        Weapon weapon = MockItem.createWeapon();
        Monster monster = MockItem.createMonsterWithWeakness(weapon);
        Player player = MockPlayer.createWithHeldItem(weapon);

        assertTrue(monster.fight(player));
        assertTrue(player.isInGame());
    }

    @Test
    public void testFight_incorrectWeapon() {
        Weapon weapon = MockItem.createWeapon();
        Monster monster = MockItem.createMonsterWithWeakness(null);
        Player player = MockPlayer.createWithHeldItem(weapon);

        assertFalse(monster.fight(player));
        assertFalse(player.isInGame());
    }

    @Test
    public void testFight_noWeapon() {
        Weapon weapon = MockItem.createWeapon();
        Monster monster = MockItem.createMonsterWithWeakness(weapon);
        Player player = MockPlayer.create();

        assertFalse(monster.fight(player));
        assertFalse(player.isInGame());
    }
}
