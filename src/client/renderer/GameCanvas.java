package client.renderer;

import client.NetworkListener;
import client.NetworkNotifier;
import game.Game;
import game.Item;
import game.Player;
import game.Room;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class GameCanvas extends JPanel implements NetworkListener, MouseListener, MouseMotionListener {
    private @Nullable Game game;
    private @NonNull String playerName;
    private @Nullable Player player;

    private @Nullable RoomRenderer roomImage;

    public GameCanvas(@NonNull String playerName, @NonNull NetworkNotifier notifier) {
        this.playerName = playerName;
        notifier.addChangeListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (roomImage != null) {
            BufferedImage image = roomImage.getCurrentImage();
            double scale = Math.min(getWidth() / image.getWidth(), getHeight() / image.getHeight());
            int width = (int)(image.getWidth() * scale);
            int height = (int)(image.getHeight() * scale);

            g.drawImage(image, (getWidth() - width) / 2, (getHeight() - height) / 2, width, height, null, null);
        }

        g.drawRect(0, 0, 200, 200);
    }

    @Override
    public void onGameLoaded(Game game) {
        this.game = game;
        player = game.getPlayer(playerName);
        roomImage = new RoomRenderer(game, player);
    }

    @Override
    public void onPlayerMoved(Player player) {
    }

    @Override
    public void onItemRemoved(Room room, Item item) {
    }

    @Override
    public void onItemAdded(Room room, Item item) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //TODO show popup
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //TODO stuff
    }

    // Stub events

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    //TODO remove
    // placeholder main method

    public static void main(String[] args) {
        final NetworkNotifier notifier = new NetworkNotifier() {
            @Override
            public void addChangeListener(NetworkListener listener) {
            }
        };

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame frame = new JFrame("GameCanvas");
                frame.getContentPane().add(new GameCanvas("player", notifier), BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
