package client.popups;

import client.GameCanvas;
import game.ActionReceiver;
import game.Item;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionMenu extends JPopupMenu implements ActionListener {
    private Item item;
    private ActionReceiver receiver;

    public ActionMenu(ActionReceiver receiver, Item item) {
        this.receiver = receiver;
        this.item = item;

        for (Item.Action action : item.getAllowedActions()) {
            addMenuItem(action.toString());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Item.Action action : item.getAllowedActions()) {
            if (e.getActionCommand().equals(action.toString())) {
                receiver.performAction(item, action);
                return;
            }
        }
    }

    private void addMenuItem(String text) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setName("actionMenuItem");
        menuItem.setActionCommand(text);
        menuItem.addActionListener(this);
        add(menuItem);
    }
}
