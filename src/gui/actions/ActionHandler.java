package gui.actions;

import game.*;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.util.List;

public interface ActionHandler {
    class InvalidActionException extends Exception {
        public InvalidActionException(String message) {
            super(message);
        }
    }

//    public List<Action> getAllowedActions(@NonNull Drawable drawable) {
//        List<Action> result = new ArrayList<>();
//
//        if (drawable instanceof Room.ItemInstance) {
//            Item item = ((Room.ItemInstance)drawable).getItem();
//
//            if (item instanceof Pickable) {
//                result.add(Action.PICK_UP);
//            }
//            if (item instanceof Container) {
//                result.add(Action.SEARCH);
//            }
//        }
//        else if (drawable instanceof Door) {
//            result.add(Action.GO_THROUGH);
//        }
//
//        return result;
//    }

    List<Action> getAllowedActions(@NonNull Drawable drawable);
    void requestAction(@NonNull Action action, @NonNull Player player,
                       @Nullable Drawable target, @Nullable Object... parameters) throws InvalidActionException;
}
