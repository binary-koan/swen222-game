package gui.popups;

import game.*;
import gui.actions.ActionHandler;
import org.eclipse.jdt.annotation.NonNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import gui.actions.Action;

/**
 * A popup menu showing all the actions which can be taken on a particular item
 *
 * @author Jono Mingard
 */
public class ActionMenu extends JPopupMenu implements ActionListener {
    private ActionHandler receiver;
    private List<Action> actions;

    /**
     * Create a new action menu
     *
     * @param receiver object which will be notified when an action is selected
     */
    public ActionMenu(ActionHandler receiver, List<Action> actions) {
        this.receiver = receiver;
        this.actions = actions;

        for (Action action : actions) {
            addMenuItem(action.name, action.toString());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Figure out which action was triggered and send it off to the receiver
        for (Action action : actions) {
            if (e.getActionCommand().equals(action.toString())) {
                receiver.requestAction(action);
                return;
            }
        }
    }

    /**
     * Add an item to this menu
     */
    private void addMenuItem(String text, String actionCommand) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setName("actionMenuItem");
        menuItem.setActionCommand(actionCommand);
        menuItem.addActionListener(this);
        add(menuItem);
    }
}
