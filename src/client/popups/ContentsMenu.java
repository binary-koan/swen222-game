package client.popups;

import client.ResourceLoader;
import game.ActionReceiver;
import game.Direction;
import game.Item;
import game.Container;
import org.eclipse.jdt.annotation.NonNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Popup menu displaying the contents of a particular container
 */
public class ContentsMenu extends JPopupMenu implements ActionListener {
    private @NonNull Container container;
    private @NonNull ActionReceiver receiver;

    /**
     * Create a new contents menu
     *
     * @param loader used to retrieve the sprites of the items in the container
     * @param receiver object which will be notified when an action is selected
     * @param container box to display items from
     * @param description text description of the action that can be taken with these items (eg. "Click to pick up")
     */
    public ContentsMenu(@NonNull ResourceLoader loader, @NonNull ActionReceiver receiver, @NonNull Container container,
                        @NonNull String description) {
        this.receiver = receiver;
        this.container = container;

        if (container.getItems().size() > 0) {
            addLabel(container.getItems().size() + " item(s)");
            addLabel(description);

            for (Item item : container.getItems()) {
                addMenuItem(item.getID(), item.getName(), loader.getSprite(item.getSpriteName(), Direction.NORTH));
            }
        }
        else {
            addLabel("Empty");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Figure out which item was activated and send a TAKE action to the receiver
        for (Item item : container.getItems()) {
            if (e.getActionCommand().equals(item.getID())) {
                receiver.performAction(container, item, Item.Action.TAKE);
                return;
            }
        }
    }

    /**
     * Add a label to the menu
     */
    private void addLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBorder(new EmptyBorder(10, 10, 0, 10));
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        add(label);
    }

    /**
     * Add a menu item to the menu. id becomes its actionCommand; text and sprite are displayed
     */
    private void addMenuItem(String id, String text, BufferedImage sprite) {
        ImageIcon icon = new ImageIcon(sprite.getScaledInstance(24, -1, Image.SCALE_DEFAULT));
        JMenuItem menuItem = new JMenuItem(text, icon);
        menuItem.setName("actionMenuItem");
        menuItem.setActionCommand(id);
        menuItem.addActionListener(this);
        add(menuItem);
    }
}
