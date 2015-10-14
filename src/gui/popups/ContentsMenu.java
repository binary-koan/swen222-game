package gui.popups;

import game.*;
import game.Container;
import gui.*;
import gui.actions.ActionHandler;
import gui.actions.GameActions;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Popup menu displaying the contents of a particular container
 *
 * @author Jono Mingard
 */
public class ContentsMenu extends JPopupMenu implements ActionListener {
    private Player player;
    private Room.ItemInstance containerInstance;
    private Container container;
    private ActionHandler receiver;

    /**
     * Create a new contents menu
     *
     * @param loader used to retrieve the sprites of the items in the container
     * @param receiver object which will be notified when an action is selected
     * @param containerInstance box to display items from
     * @param description text description of the action that can be taken with these items (eg. "Click to pick up")
     */
    public ContentsMenu(ResourceManager loader, ActionHandler receiver, Room.ItemInstance containerInstance,
                        Player player, String description) {
        this.receiver = receiver;
        this.containerInstance = containerInstance;
        this.player = player;

        if (containerInstance.getItem() instanceof Container) {
            container = (Container)containerInstance.getItem();

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
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Figure out which item was activated and send a TAKE action to the receiver
        for (Item item : ((Container) containerInstance.getItem()).getItems()) {
            if (e.getActionCommand().equals(item.getID())) {
                receiver.requestAction(new GameActions.PickUp(player, item, container));
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
