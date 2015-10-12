package gui;

import game.*;
import gui.actions.Action;
import gui.actions.ActionHandler;
import gui.actions.GameActions;
import gui.renderer.Door;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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

            if (item instanceof Pickable) {
                result.add(new GameActions.PickUp(player, (Room.ItemInstance)drawable));
            }
        }
        else if (drawable instanceof Door) {
            result.add(new GameActions.GoThrough(player, (Door)drawable));
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
        if (action instanceof GameActions.Turn) {
            turnPlayer((GameActions.Turn) action);
        }
        else if (action instanceof GameActions.PickUp) {
            pickUp((GameActions.PickUp) action);
        }
        else if (action instanceof GameActions.Take) {
            take((GameActions.Take) action);
        }
        else if (action instanceof GameActions.GoThrough) {
            move((GameActions.GoThrough) action);
        }
    }

    /**
     * Perform a "Turn" action
     */
    private void turnPlayer(GameActions.Turn action) {
        action.player.turn(action.direction);
    }

    /**
     * Perform a "Pick up" action
     */
    private void pickUp(GameActions.PickUp action) {
        action.player.pickUp(action.target.getItem());
    }

    /**
     * Perform a "Take" action
     */
    private void take(GameActions.Take action) {
        action.container.take(action.takenItem, action.player);
    }

    /**
     * Perform a "Move" action
     */
    private void move(GameActions.GoThrough action) {
        action.player.move(action.door.getFacingDirection());
    }
}
