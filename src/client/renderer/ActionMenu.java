package client.renderer;

import game.Item;

import javax.swing.*;
import java.util.List;

public class ActionMenu extends JPanel {
    private List<Item.Action> actions;

    public ActionMenu(List<Item.Action> actions) {
        this.actions = actions;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        for (Item.Action action : actions) {
            add(new JLabel(action.toString()));
        }
    }
}
