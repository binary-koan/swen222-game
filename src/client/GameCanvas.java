package client;

import client.renderer.ResourceLoader;
import client.renderer.RoomRenderer;
import game.*;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameCanvas extends JPanel implements MouseListener, MouseMotionListener {
    private @NonNull ResourceLoader loader;
    private @Nullable RoomRenderer roomImage;
    private @Nullable Player player;

    public GameCanvas(@NonNull ResourceLoader loader) {
        this.loader = loader;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        if (roomImage != null) {
            BufferedImage image = roomImage.getCurrentImage();
            double scale = Math.min(getWidth() / image.getWidth(), getHeight() / image.getHeight());
            int width = (int) (image.getWidth() * scale);
            int height = (int) (image.getHeight() * scale);

            g.drawImage(image, (getWidth() - width) / 2, (getHeight() - height) / 2, width, height, null, null);
        }

        if (player != null) {
            g.drawString("In " + player.getRoom().getName() + " facing " + player.getFacingDirection().opposite(), 10, 20);
        }
    }

    public void setPlayer(@Nullable Player player) {
        this.player = player;

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
            public String getWallImage() {
                return "backgrounds/room.png";
            }

            {
                Item item = new Item("Item name", "objects/bed.png");
                getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.BoundingCube(80, 0, 80, 20, 20, 20)));

                item = new Item("Item name", "objects/bed.png");
                getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.BoundingCube(40, 0, 120, 40, 20, 40)));

                item = new Item("Item name", "objects/bed.png");
                getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.BoundingCube(140, 40, 20, 20, 40, 20)));
            }
        };

        final Player player = new Player("Person", "characters/1.png") {
            @Override
            public BoundingCube getBoundingCube() {
                return new BoundingCube(80, 0, 80, 20, 20, 20);
            }

            @Override
            public Room getRoom() {
                return room;
            }
        };
        player.setFacingDirection(Direction.NORTH);

        final GameCanvas canvas = new GameCanvas(loader);

        final KeyListener keyListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        player.setFacingDirection(player.getFacingDirection().previous());
                        break;
                    case KeyEvent.VK_RIGHT:
                        player.setFacingDirection(player.getFacingDirection().next());
                }
                canvas.update();
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("GameCanvas");
                frame.addKeyListener(keyListener);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                frame.getContentPane().add(canvas, BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);

                canvas.setPlayer(player);
            }
        });
    }
}
