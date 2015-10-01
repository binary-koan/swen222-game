package client;

import client.renderer.ResourceLoader;
import client.renderer.RoomRenderer;
import game.*;
import game.Room.ItemInstance;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameCanvas extends JPanel implements MouseListener, MouseMotionListener {
    private @NonNull ResourceLoader loader;

    private double roomImageScale;
    private @Nullable Point roomImagePosition;
    private @Nullable RoomRenderer roomImage;

    private @Nullable Player player;

    private Item clickedItem;

    public GameCanvas(@NonNull ResourceLoader loader) {
        this.loader = loader;
        addMouseListener(this);
        addMouseMotionListener(this);
    }


    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        if (roomImage != null) {
            BufferedImage image = roomImage.getCurrentImage();
            roomImageScale = Math.min((double)getWidth() / image.getWidth(), (double)getHeight() / image.getHeight());
            int width = (int) (image.getWidth() * roomImageScale);
            int height = (int) (image.getHeight() * roomImageScale);
            roomImagePosition = new Point((getWidth() - width) / 2, (getHeight() - height) / 2);

            Image scaled = image.getScaledInstance(width, height, Image.SCALE_FAST);
            g.drawImage(scaled, roomImagePosition.x, roomImagePosition.y, width, height, null, null);
        }

        if (player != null) {
            g.drawString("In " + player.getRoom().getName() + " facing " + player.getFacingDirection().opposite(), 10, 20);
        }

        if (clickedItem != null) {
        	g.drawString("Clicked on " + clickedItem.getName(), 10, 50);
        }
    }

    @Override
    public Dimension getPreferredSize() {
    	return new Dimension(Room.ROOM_SIZE, Room.CEILING_HEIGHT);
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
    	System.out.println(roomImageScale);
    	System.out.println(roomImagePosition);
    	Point relative = new Point(
    			(int)((e.getX() - roomImagePosition.x) / (roomImageScale * 5)),
    			(int)((e.getY() - roomImagePosition.y) / (roomImageScale * 5))
    	);
    	System.out.println(relative);

        Drawable drawable = roomImage.getObjectAt(relative);
        System.out.println(drawable);
        if (drawable instanceof ItemInstance) {
        	clickedItem = ((ItemInstance)drawable).getItem();
        	repaint();
        }
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
                Item item = new Item("Bed", "objects/bed.png");
                getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.BoundingCube(80, 0, 80, 48, 32, 48)));

                item = new Item("Chest", "objects/chest.png");
                getItems().add(new Room.ItemInstance(item, Direction.EAST, new Drawable.BoundingCube(40, 0, 120, 48, 32, 48)));

                item = new Item("Key", "objects/key.png");
                getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.BoundingCube(140, 60, 20, 32, 32, 32)));

                item = new Item("Door", "objects/door.png");
                getItems().add(new Room.ItemInstance(item, Direction.WEST, new Drawable.BoundingCube(140, 0, 80, 32, 48, 32)));
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
