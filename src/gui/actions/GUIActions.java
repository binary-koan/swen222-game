package gui.actions;

import game.Container;
import game.Drawable;
import game.Player;
import game.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Actions related to showing information in the GUI
 *
 * @author Jono Mingard
 */
public class GUIActions {
    /**
     * Action representing the player viewing the description or state of an object
     */
    public static class Examine extends Action {
        public final Drawable target;

        public Examine(Player player, Drawable target) {
            super(player, "Examine");
            this.target = target;
        }
    }

    /**
     * Action representing the player viewing the contents of a container
     */
    public static class Search extends Action {
        public final Container container;
        public final Room.ItemInstance containerInstance;

        public Search(Player player, Container container, Room.ItemInstance containerInstance) {
            super(player, "Search");

            this.container = container;
            this.containerInstance = containerInstance;
        }
    }

    /**
     * Meta-action representing the player viewing a list of possible actions which can be taken on the target (eg. by
     * showing a context menu listing the actions)
     */
    public static class ShowMenu extends Action {
        public final List<Action> actions;
        public final Drawable target;

        public ShowMenu(Player player, List<Action> actions, Drawable target) {
            super(player, "Other ...");

            this.actions = new ArrayList<>(actions);
            this.target = target;
        }
    }
}
