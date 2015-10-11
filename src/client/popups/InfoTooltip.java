package client.popups;

import client.ResourceLoader;
import game.Item;
import org.eclipse.jdt.annotation.NonNull;

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
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));

        setupNameLabel();
        setupDescriptionLabel();
        setupPrimaryActionLabel(loader);
        setupSecondaryActionLabel(loader);
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
        descriptionLabel.setText("<html><div style='width:150px'>" + description + "</div></html>");
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

    /**
     * Setup and add the name label to the control
     */
    private void setupNameLabel() {
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        nameLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        addLabel(nameLabel);
    }

    /**
     * Setup and add the description label to the control
     */
    private void setupDescriptionLabel() {
        descriptionLabel.setVisible(false);
        descriptionLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        descriptionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        addLabel(descriptionLabel);
    }

    /**
     * Setup and add the primary action label to the control
     */
    private void setupPrimaryActionLabel(@NonNull ResourceLoader loader) {
        primaryActionLabel.setIcon(new ImageIcon(loader.getImage("ui/mouse-leftclick.png")));
        primaryActionLabel.setIconTextGap(8);
        addLabel(primaryActionLabel);
    }

    /**
     * Setup and add the secondary action label to the control
     */
    private void setupSecondaryActionLabel(@NonNull ResourceLoader loader) {
        secondaryActionLabel.setIcon(new ImageIcon(loader.getImage("ui/mouse-rightclick.png")));
        secondaryActionLabel.setIconTextGap(8);
        addLabel(secondaryActionLabel);
    }

    /**
     * Add a label to the control, ensuring that it stretches to fit
     */
    private void addLabel(JLabel label) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = getComponentCount();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(label, constraints);
    }
}
