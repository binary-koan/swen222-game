package gui.actions;

import game.*;

import java.util.List;

/**
 * An object which can list and handle actions for things in the game world
 *
 * @author Jono Mingard
 */
public interface ActionHandler {
    /**
     * Returns a list of all actions which can be performed on the item. Any actions returned by this method can be
     * handled by the {@link #requestAction(Action)} of this object
     *
     * @param player the player performing the action
     * @param drawable the object to find actions for
     * @return a list of actions the player can take on the object
     */
    List<Action> getAllowedActions(Player player, Drawable drawable);

    /**
     * Attempts to perform the given action, updating the state of the game it is attached to. The action may be
     * asynchronous, so the result cannot be determined without listening to changes in the game state
     *
     * @param action action to perform
     */
    void requestAction(Action action);
}
