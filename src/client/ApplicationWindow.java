package client;

import game.Bed;
import game.Direction;
import game.Door;
import game.Drawable;
import game.Item;
import game.Chest;
import game.Key;
import game.Player;
import game.Room;
import game.Drawable.BoundingCube;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import client.renderer.ResourceLoader;


public class ApplicationWindow extends JFrame {

	

	public ApplicationWindow(String title, GameCanvas canvas) {
		super(title);

		final JMenuBar menuBar = setupMenuBar();
		JPanel lowerBar = setupLowerBar();
		setLayout(new BorderLayout());

		//Add items to the frame
		add(menuBar, BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);
		add(lowerBar, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pack();

	}


	private JMenuBar setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Game");

		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.setMnemonic(KeyEvent.VK_N);
		newMenuItem.setActionCommand("New");

	     JMenuItem openMenuItem = new JMenuItem("Open");
	     openMenuItem.setActionCommand("Open");

	     JMenuItem saveMenuItem = new JMenuItem("Save");
	     saveMenuItem.setActionCommand("Save");

	     JMenuItem exitMenuItem = new JMenuItem("Exit");
	     exitMenuItem.setActionCommand("Exit");

	     fileMenu.add(newMenuItem);
	     fileMenu.add(openMenuItem);
	     fileMenu.add(saveMenuItem);
	     fileMenu.addSeparator();
	     fileMenu.add(exitMenuItem);

	     menuBar.add(fileMenu);
	     menuBar.add(editMenu);

	     return menuBar;

	}

	private JPanel setupLowerBar() {
		JPanel area = new JPanel();
		area.setLayout(new FlowLayout());
		return area;
	}

	public static void main(String[] args) {
		final ResourceLoader loader = new ResourceLoader("resources");
        final Room room = new Room("Some name") {
            @Override
            public String getWallImage() {
                return "backgrounds/room.png";
            }

            {
                Item item = new Bed("Item name", "objects/bed.png");
                getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.BoundingCube(80, 0, 80, 48, 32, 48)));

                item = new Chest("Item name", "objects/chest.png");
                getItems().add(new Room.ItemInstance(item, Direction.EAST, new Drawable.BoundingCube(40, 0, 120, 48, 32, 48)));

                item = new Key("Item name", "objects/key.png");
                getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.BoundingCube(140, 60, 20, 32, 32, 32)));

                item = new Door("Item name", "objects/door.png");
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
            	
                new ApplicationWindow("Game", canvas).setVisible(true);
            }
        });
	}

}