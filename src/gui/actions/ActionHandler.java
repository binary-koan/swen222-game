package gui.actions;

import game.*;

import java.util.List;

public interface ActionHandler {
    List<Action> getAllowedActions(Drawable drawable);
    void requestAction(Action action);
}
