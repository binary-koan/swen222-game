package client.renderer;

import game.Item;
import game.Room;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Tooltip extends JPanel {
    private Item.Action primaryAction;
    private Item.Action secondaryAction;

	private JLabel nameLabel = new JLabel("(no name)");
    private JLabel descriptionLabel = new JLabel();
	private JLabel primaryActionLabel = new JLabel();
	private JLabel secondaryActionLabel = new JLabel();

	public Tooltip(ResourceLoader loader) {
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

	public void showObject(String name, Item.Action primary, Item.Action secondary) {
        primaryAction = primary;
        secondaryAction = secondary;

        descriptionLabel.setVisible(false);
		nameLabel.setText(name.toUpperCase());

        if (primary != null) {
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

    public Item.Action getPrimaryAction() {
        return primaryAction;
    }

    public Item.Action getSecondaryAction() {
        return secondaryAction;
    }

    public void showDescription(String description) {
        descriptionLabel.setVisible(true);
        // Using HTML in the label makes it word wrap and allows for markup in descriptions
        descriptionLabel.setText("<html>" + description + "</html>");
    }
}
