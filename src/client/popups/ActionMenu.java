package client.popups;

import game.ActionReceiver;
import game.Item;
import org.eclipse.jdt.annotation.NonNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A popup menu showing all the actions which can be taken on a particular item
 */
public class ActionMenu extends JPopupMenu implements ActionListener {
    private @NonNull Item item;
    private @NonNull ActionReceiver receiver;

    /**
     * Create a new action menu
     *
     * @param receiver object which will be notified when an action is selected
     * @param item item to display actions of
     */
    public ActionMenu(@NonNull ActionReceiver receiver, @NonNull Item item) {
        this.receiver = receiver;
        this.item = item;

        for (Item.Action action : item.getAllowedActions()) {
            addMenuItem(action.toString());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Figure out which action was triggered and send it off to the receiver
        for (Item.Action action : item.getAllowedActions()) {
            if (e.getActionCommand().equals(action.toString())) {
                receiver.performAction(item, action);
                return;
            }
        }
    }

    /**
     * Add an item to this menu
     */
    private void addMenuItem(String text) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setName("actionMenuItem");
        menuItem.setActionCommand(text);
        menuItem.addActionListener(this);
        add(menuItem);
    }
}
