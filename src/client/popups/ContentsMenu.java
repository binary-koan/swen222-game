package client.popups;

import client.GameCanvas;
import client.ResourceLoader;
import game.ActionReceiver;
import game.Direction;
import game.Item;
import game.Container;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ContentsMenu extends JPopupMenu implements ActionListener {
    private Container container;
    private ActionReceiver receiver;

    public ContentsMenu(ResourceLoader loader, ActionReceiver receiver, Container container, String description) {
        this.receiver = receiver;
        this.container = container;

        if (container.getItems().size() > 0) {
            addLabel(container.getItems().size() + " item(s)");
            addLabel(description);

            for (Item item : container.getItems()) {
                System.out.println("Adding menu item");
                addMenuItem(item.getID(), item.getName(), loader.getSprite(item.getSpriteName(), Direction.NORTH));
            }
        }
        else {
            addLabel("Empty");
        }
    }

    private void addLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBorder(new EmptyBorder(10, 10, 0, 10));
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        add(label);
    }

    private void addMenuItem(String id, String text, BufferedImage sprite) {
        ImageIcon icon = new ImageIcon(sprite.getScaledInstance(24, -1, Image.SCALE_DEFAULT));
        JMenuItem menuItem = new JMenuItem(text, icon);
        menuItem.setName("actionMenuItem");
        menuItem.setActionCommand(id);
        menuItem.addActionListener(this);
        add(menuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Item item : container.getItems()) {
            if (e.getActionCommand().equals(item.getID())) {
                receiver.performAction(container, item, Item.Action.TAKE);
                return;
            }
        }
    }
}
