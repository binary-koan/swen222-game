package client.renderer;

import game.Item;
import game.Room;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Tooltip extends JPanel {

	JLabel nameLabel = new JLabel("(no name)");
	JLabel primaryActionLabel = new JLabel();
	JLabel secondaryActionLabel = new JLabel();

	public Tooltip(ResourceLoader loader) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		Color background = new Color(0x222222);
		setBackground(background);
		setBorder(new LineBorder(background, 10));

		Color foreground = new Color(0xffffff);
		nameLabel.setForeground(foreground);
		primaryActionLabel.setForeground(foreground);
		primaryActionLabel.setIcon(new ImageIcon(loader.getImage("ui/mouse-leftclick.png")));
		secondaryActionLabel.setForeground(foreground);
		secondaryActionLabel.setIcon(new ImageIcon(loader.getImage("ui/mouse-rightclick.png")));

		add(nameLabel);
		add(primaryActionLabel);
		add(secondaryActionLabel);
	}

	public void update(String name, List<String> actions) {
		nameLabel.setText(name);

		if (actions != null && actions.size() > 0) {
			primaryActionLabel.setText(actions.get(0));

			if (actions.size() == 2) {
				secondaryActionLabel.setText(actions.get(1));
			}
			else if (actions.size() > 2) {
				secondaryActionLabel.setText("Other ...");
			}
			else {
				secondaryActionLabel.setVisible(false);
			}
		}
		else {
			primaryActionLabel.setVisible(false);
			secondaryActionLabel.setVisible(false);
		}
	}
}
