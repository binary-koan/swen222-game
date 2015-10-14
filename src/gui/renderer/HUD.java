package gui.renderer;

import game.Player;
import gui.ResourceManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class HUD extends JPanel {
    private ResourceManager resources;
    private JLabel compassLabel = new JLabel();
    private JLabel infoLabel = new JLabel();

    public HUD(ResourceManager resources) {
        this.resources = resources;

        compassLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(compassLabel);
        add(infoLabel);
    }

    public void update(Player player) {
        compassLabel.setIcon(new ImageIcon(resources.getSprite("ui/compass.png", player.getFacingDirection())));
        infoLabel.setText("<html>" +
                "<i>" + player.getName() + "</i><br />" +
                "In " + player.getRoom().getName() +
        "</html>");
    }
}
