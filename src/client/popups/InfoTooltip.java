package client.popups;

import client.ResourceLoader;
import game.Item;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A tooltip showing information about an item or player
 */
public class InfoTooltip extends JPanel {
    private Item.Action primaryAction;
    private Item.Action secondaryAction;

	private JLabel nameLabel = new JLabel("(no name)");
    private JLabel descriptionLabel = new JLabel();
	private JLabel primaryActionLabel = new JLabel();
	private JLabel secondaryActionLabel = new JLabel();

    /**
     * Create a blank info tooltip
     *
     * @param loader used to load mouse click icons
     */
	public InfoTooltip(@NonNull ResourceLoader loader) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(10, 10, 10, 10));

        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

        descriptionLabel.setVisible(false);
        descriptionLabel.setVerticalAlignment(JLabel.TOP);
        descriptionLabel.setPreferredSize(new Dimension(200, 75));
        descriptionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

		primaryActionLabel.setIcon(new ImageIcon(loader.getImage("ui/mouse-leftclick.png")));
        primaryActionLabel.setIconTextGap(8);

		secondaryActionLabel.setIcon(new ImageIcon(loader.getImage("ui/mouse-rightclick.png")));
        secondaryActionLabel.setIconTextGap(8);

		add(nameLabel);
        add(descriptionLabel);
        add(Box.createVerticalStrut(10));
        add(Box.createVerticalGlue());
		add(primaryActionLabel);
		add(secondaryActionLabel);
	}

    /**
     * Update the tooltip labels to show information about the
     *
     * @param name the name of the object
     * @param primary action to be taken when left-clicking the object
     * @param secondary action to be taken when right-clicking the object
     */
	public void showObject(@NonNull String name, Item.Action primary, Item.Action secondary) {
        primaryAction = primary;
        secondaryAction = secondary;

        descriptionLabel.setVisible(false);
		nameLabel.setText(name.toUpperCase());

        if (primary == null) {
            primaryActionLabel.setVisible(false);
        }
        else {
            primaryActionLabel.setText(primary.toString());
        }

		if (secondary == null) {
			secondaryActionLabel.setVisible(false);
		}
		else {
			secondaryActionLabel.setVisible(true);
			secondaryActionLabel.setText(secondary.toString());
		}
	}

    /**
     * Display a description of the current item below its name
     *
     * @param description text to display
     */
    public void showDescription(@NonNull String description) {
        descriptionLabel.setVisible(true);
        // Using HTML in the label makes it word wrap and allows for markup in descriptions
        descriptionLabel.setText("<html>" + description + "</html>");
    }

    /**
     * Returns the action currently being displayed as the primary (left-click) action
     *
     * @return the action being displayed
     */
    public Item.Action getPrimaryAction() {
        return primaryAction;
    }

    /**
     * Returns the action currently being displayed as the secondary (right-click) action
     *
     * @return the action being displayed
     */
    public Item.Action getSecondaryAction() {
        return secondaryAction;
    }
}
