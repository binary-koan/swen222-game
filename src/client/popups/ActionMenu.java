package client.popups;

import client.GameCanvas;
import game.Item;
import game.Room;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionMenu extends JPopupMenu implements ActionListener {
    private Room.ItemInstance itemInstance;
    private GameCanvas canvas;

    public ActionMenu(GameCanvas canvas, Room.ItemInstance itemInstance) {
        this.canvas = canvas;
        this.itemInstance = itemInstance;

        for (Item.Action action : itemInstance.getItem().getAllowedActions()) {
            addMenuItem(action.toString());
        }
    }

    private void addMenuItem(String text) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setName("actionMenuItem");
        menuItem.setActionCommand(text);
        add(menuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Item.Action action : itemInstance.getItem().getAllowedActions()) {
            if (e.getActionCommand().equals(action.toString())) {
                canvas.performAction(itemInstance, action);
                return;
            }
        }
    }
}
