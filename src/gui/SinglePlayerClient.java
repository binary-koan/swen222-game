package gui;

import game.*;
import gui.actions.Action;
import gui.actions.ActionHandler;
import gui.actions.GameActions;
import gui.renderer.Door;

import java.util.ArrayList;
import java.util.List;

/**
 * An action handler which finds and handles actions related to the game state. This performs the actions synchronously
 * on a local copy of the game data, useful for single player functionality
 *
 * @author Jono Mingard
 */
public class SinglePlayerClient implements ActionHandler {
    /**
     * Return actions which the player can take to modify the current state of the game (from {@link GameActions})
     *
     * @param player the player performing the action
     * @param drawable the object to find actions for
     * @return a list of possible actions
     */
    @Override
    public List<Action> getAllowedActions(Player player, Drawable drawable) {
        List<Action> result = new ArrayList<>();

        if (drawable instanceof Room.ItemInstance) {
            Item item = ((Room.ItemInstance)drawable).getItem();

            if (item instanceof Holdable) {
                result.add(new GameActions.PickUp(player, (Holdable)item, null));
            }
            else if (item instanceof Monster) {
                result.add(new GameActions.Attack(player, (Monster)item));
            }
        }
        else if (drawable instanceof Door) {
            result.add(new GameActions.GoThrough(player, (Door) drawable));
        }

        return result;
    }

    /**
     * Perform an action immediately on the local game data the action refers to
     *
     * @param action action to perform
     */
    @Override
    public void requestAction(Action action) {
        // For multiplayer, perform some network request here ...
        if (action instanceof GameActions.GameAction) {
            ((GameActions.GameAction)action).apply();
        }
    }
}
