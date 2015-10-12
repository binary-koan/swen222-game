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

public class SinglePlayerClient implements ActionHandler {
    private Player player;

    public SinglePlayerClient(Player player) {
        this.player = player;
    }

    @Override
    public List<Action> getAllowedActions(Drawable drawable) {
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

    private void turnPlayer(GameActions.Turn action) {
        action.player.turn(action.direction);
    }

    private void pickUp(GameActions.PickUp action) {
        action.player.pickUp(action.target.getItem());
    }

    private void take(GameActions.Take action) {
        action.container.take(action.takenItem, action.player);
    }

    private void move(GameActions.GoThrough action) {
        action.player.move(action.door.getFacingDirection());
    }
}
