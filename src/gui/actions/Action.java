package gui.actions;

import game.Game;
import game.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an action a player can take in the world - this could be as simple as viewing the description of an
 * object, or as complex as taking something out of a container
 *
 * @author Jono Mingard
 */
public abstract class Action {
    /** The player performing the action */
    public final Player player;
    /** A human-readable name for the action */
    public final String name;

    /**
     * Create a new action
     *
     * @param player player performing the action
     * @param name human-readable name for the action
     */
    public Action(Player player, String name) {
        this.player = player;
        this.name = name;
    }
}
