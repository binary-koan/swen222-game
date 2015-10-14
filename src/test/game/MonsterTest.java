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
        Player player = MockPlayer.createWithHeldItem(weapon);
        Monster monster = MockItem.createMonsterWithWeakness(player.getRoom(), weapon);

        assertTrue(monster.fight(player));
        assertTrue(player.isAlive());
    }

    @Test
    public void testFight_incorrectWeapon() {
        Weapon weapon = MockItem.createWeapon();
        Player player = MockPlayer.createWithHeldItem(weapon);
        Monster monster = MockItem.createMonsterWithWeakness(player.getRoom(), null);

        assertFalse(monster.fight(player));
        assertFalse(player.isAlive());
    }

    @Test
    public void testFight_noWeapon() {
        Weapon weapon = MockItem.createWeapon();
        Player player = MockPlayer.create();
        Monster monster = MockItem.createMonsterWithWeakness(player.getRoom(), weapon);

        assertFalse(monster.fight(player));
        assertFalse(player.isAlive());
    }
}
