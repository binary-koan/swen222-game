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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import client.renderer.ResourceLoader;


public class ApplicationWindow extends JFrame {

	private GameCanvas canvas;
	
	public ApplicationWindow(String title, GameCanvas canvas) {
		super(title);
		this.canvas = canvas;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension scale = new Dimension();
		scale.setSize(screenSize.getWidth() * 0.6, screenSize.getHeight() * 0.7);
		setPreferredSize(scale);
		
		
		
		final JMenuBar menuBar = setupMenuBar();
		JPanel lowerBar = setupLowerBar();
		setLayout(new BorderLayout());

		
		//Add items to the frame
		add(menuBar, BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);
		add(lowerBar, BorderLayout.SOUTH);
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	
	

	public GameCanvas getGameCanvas() {
		return canvas;
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
		area.setLayout(new BorderLayout());
		
		JPanel inventory = new JPanel();
		inventory.setLayout(new FlowLayout());
		inventory.add(new ImagePanel("key"));
		inventory.add(new ImagePanel("fireplace"));
		
		//inventory.setBackground(Color.BLACK);
		
		area.add(inventory, BorderLayout.EAST);
		return area;
	}
	
	private class ImagePanel extends JPanel {
		private BufferedImage image;
		
		public ImagePanel(String item) {
			try {
				image = ImageIO.read(new File("resources/objects/"+item+".png"));
				image = image.getSubimage(0, 0, image.getWidth(), image.getHeight());
			} catch (IOException ex) {
				
			}
			
			this.setPreferredSize(new Dimension(40, 40));
			this.setBackground(Color.DARK_GRAY);
		
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 5, 5, this.getWidth() - 5, this.getHeight() - 5, null);
		
		}
	}

	public static void main(String[] args) {
		final Player player2 = new Player("Player 2", "characters/o1.png");
    	player2.setFacingDirection(Direction.NORTH);

		final ResourceLoader loader = new ResourceLoader("resources");
        final Room room = new Room("Some name") {
            @Override
            public String getWallImage() {
                return "backgrounds/room.png";
            }

            {
            	 Item item = new Bed("Bed", "objects/bed.png");
                 getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.BoundingCube(80, 0, 80, 48, 32, 48)));

                 item = new Chest("Chest", "objects/chest.png");
                 getItems().add(new Room.ItemInstance(item, Direction.EAST, new Drawable.BoundingCube(40, 0, 120, 48, 32, 48)));

                 item = new Key("Key", "objects/key.png");
                 getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.BoundingCube(140, 60, 20, 32, 32, 32)));

                 item = new Door("Door", "objects/door.png");
                 getItems().add(new Room.ItemInstance(item, Direction.WEST, new Drawable.BoundingCube(140, 0, 80, 32, 48, 32)));

                 player2.setBoundingBox(new Drawable.BoundingCube(80, 80, 80, 32, 32, 32));
                 getPlayers().add(player2);
            }
        };
        player2.setRoom(room);

        final Player player = new Player("Player 1", "characters/o1.png") {
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
        
        canvas.addKeyListener(keyListener);

		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	
                ApplicationWindow aw = new ApplicationWindow("Game", canvas);
                aw.addKeyListener(keyListener);
                aw.pack();
                aw.setVisible(true);
               
                aw.getGameCanvas().setPlayer(player);
                
            }
        });
	}

}