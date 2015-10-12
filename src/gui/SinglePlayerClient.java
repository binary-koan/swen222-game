package gui;

import game.*;
import gui.actions.Action;
import gui.actions.ActionHandler;
import gui.actions.GameAction;
import gui.renderer.Door;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SinglePlayerClient implements ActionHandler {
    @Override
    public List<Action> getAllowedActions(@NonNull Drawable drawable) {
        List<Action> result = new ArrayList<>();

        if (drawable instanceof Room.ItemInstance) {
            Item item = ((Room.ItemInstance)drawable).getItem();

            if (item instanceof Pickable) {
                result.add(new GameAction(GameAction.Type.PICK_UP));
            }
            if (item instanceof Container) {
                result.add(new GameAction(GameAction.Type.TAKE));
            }
        }
        else if (drawable instanceof Door) {
            result.add(new GameAction(GameAction.Type.GO_THROUGH));
        }

        return result;
    }

    @Override
    public void requestAction(@NonNull Action action, @NonNull Player player,
                              @Nullable Drawable target, @Nullable Object... parameters) throws InvalidActionException {
        // For multiplayer, perform some network request here ...
        if (action instanceof GameAction) {
            GameAction gameAction = (GameAction)action;

            switch(gameAction.getType()) {
                case PICK_UP:
                    pickUp(player, target);
                    break;
                case TAKE:
                    take(player, target, parameters);
                    break;
                case GO_THROUGH:
                    move(player, target);
                    break;
            }
        }
    }

    private void pickUp(@NonNull Player player, @Nullable Drawable target) throws InvalidActionException {
        if (target instanceof Room.ItemInstance) {
            player.pickUp(((Room.ItemInstance)target).getItem());
        }
        else {
            throw new InvalidActionException("You're trying to pick up something which isn't an item.");
        }
    }

    private void take(@NonNull Player player, @Nullable Drawable target, Object... parameters) throws InvalidActionException {
        if (parameters.length == 0 || !(parameters[0] instanceof Item)) {
            throw new InvalidActionException("I don't know which item you're trying to take.");
        }
        Item takenItem = (Item)(parameters[0]);

        if (target instanceof Room.ItemInstance) {
            Item item = ((Room.ItemInstance)target).getItem();
            if (item instanceof Container) {
                ((Container)item).take(takenItem, player);
            }
            else {
                throw new InvalidActionException("You're trying to take from something which isn't a container");
            }
        }
        else {
            throw new InvalidActionException("You're trying to take from something which isn't an item");
        }
    }

    private void move(@NonNull Player player, @Nullable Drawable target) throws InvalidActionException {
        if (target instanceof Door) {
            player.move(target.getFacingDirection());
        }
        else {
            throw new InvalidActionException("You're trying to go through something which isn't a door.");
        }
    }
}
