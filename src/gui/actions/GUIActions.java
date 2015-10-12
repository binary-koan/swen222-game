package gui.actions;

import game.Container;
import game.Drawable;
import game.Player;
import game.Room;

import java.util.List;

public class GUIActions {
    public static class Examine extends Action {
        public final Drawable target;

        public Examine(Player player, Drawable target) {
            super(player, "Examine");
            this.target = target;
        }
    }

    public static class Search extends Action {
        public final Container container;
        public final Room.ItemInstance containerInstance;

        public Search(Player player, Container container, Room.ItemInstance containerInstance) {
            super(player, "Search");

            this.container = container;
            this.containerInstance = containerInstance;
        }
    }

    public static class ShowMenu extends Action {
        public final List<Action> actions;
        public final Drawable target;

        public ShowMenu(Player player, List<Action> actions, Drawable target) {
            super(player, "Other ...");

            this.actions = actions;
            this.target = target;
        }
    }
}
