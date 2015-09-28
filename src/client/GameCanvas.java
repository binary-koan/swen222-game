package client;

import client.renderer.ResourceLoader;
import client.renderer.RoomRenderer;
import game.*;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameCanvas extends JPanel implements MouseListener, MouseMotionListener {
    private @NonNull ResourceLoader loader;
    private @Nullable RoomRenderer roomImage;

    public GameCanvas(@NonNull ResourceLoader loader) {
        this.loader = loader;
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
    }

    public void setPlayer(@Nullable Player player) {
        if (player == null) {
            roomImage = null;
        }
        else {
            roomImage = new RoomRenderer(loader, player);
            repaint();
        }
    }

    public void update() {
        if (roomImage != null) {
            roomImage.updateRoom();
            repaint();
        }
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
        final ResourceLoader loader = new ResourceLoader("resources");
        final Room room = new Room("Some name") {
            @Override
            public int getSize() {
                return 40;
            }

            @Override
            public String getWallImage() {
                return "backgrounds/room.png";
            }

            {
                Item item = new Item("Item name", "objects/bed.png");
                getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.BoundingCube(0, 0, 0, 10, 10, 10)));
            }
        };

        final Player player = new Player("Person", "characters/1.png") {
            @Override
            public BoundingCube getBoundingCube() {
                return new BoundingCube(0, 0, 0, 10, 10, 10);
            }

            @Override
            public Room getRoom() {
                return room;
            }
        };
        player.setFacingDirection(Direction.NORTH);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("GameCanvas");
                GameCanvas canvas = new GameCanvas(loader);
                frame.getContentPane().add(canvas, BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);

                canvas.setPlayer(player);
            }
        });
//        final NetworkNotifier notifier = new NetworkNotifier() {
//            @Override
//            public void addChangeListener(NetworkListener listener) {
//            }
//        };
//
//        SwingUtilities.invokeLater(new Runnable()
//        {
//            public void run()
//            {
//                JFrame frame = new JFrame("GameCanvas");
//                frame.getContentPane().add(new GameCanvas("player", notifier), BorderLayout.CENTER);
//                frame.pack();
//                frame.setVisible(true);
//            }
//        });
    }
}
