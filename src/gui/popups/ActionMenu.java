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
 */
public class ActionMenu extends JPopupMenu implements ActionListener {
    private Player player;
    private Drawable target;
    private ActionHandler receiver;
    private List<Action> actions;

    /**
     * Create a new action menu
     *
     * @param receiver object which will be notified when an action is selected
     */
    public ActionMenu(ActionHandler receiver, Player player, Drawable target) {
        this.receiver = receiver;
        this.player = player;
        this.target = target;
        this.actions = receiver.getAllowedActions(target);

        for (Action action : actions) {
            addMenuItem(action.toString());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Figure out which action was triggered and send it off to the receiver
        for (Action action : actions) {
            if (e.getActionCommand().equals(action.toString())) {
                try {
                    receiver.requestAction(action, player, target);
                }
                catch (ActionHandler.InvalidActionException err) {
                    JOptionPane.showMessageDialog(null, err.getMessage());
                }
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
