package client;

import game.Furniture;
import game.Direction;
import game.Door;
import game.Drawable;
import game.Item;
import game.Container;
import game.Player;
import game.Room;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.plaf.synth.SynthLookAndFeel;

import org.eclipse.jdt.annotation.NonNull;

import client.renderer.ResourceLoader;


public class ApplicationWindow extends JFrame implements KeyListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 6273791834646480175L;

    private Player player;
	private @NonNull ResourceLoader loader;
	private GameCanvas canvas;

	public ApplicationWindow(Player player) {
		super("Game");
        this.player = player;
        this.loader = new ResourceLoader("resources");
        this.canvas = new GameCanvas(this, loader);
        canvas.setPlayer(player);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension scale = new Dimension();
		scale.setSize(screenSize.getWidth() * 0.6, screenSize.getHeight() * 0.7);
		this.canvas.setPreferredSize(scale);

		setLayout(new BorderLayout());

		//Add items to the frame
		add(setupMenuBar(), BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);
		add(setupLowerBar(), BorderLayout.SOUTH);

        addKeyListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public GameCanvas getGameCanvas() {
		return canvas;
	}

	private JMenuBar setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Game");

		JMenuItem newMenuItem = new JMenuItem("Penis");
		newMenuItem.setMnemonic(KeyEvent.VK_N);
		newMenuItem.setActionCommand("Penis");

	     JMenuItem openMenuItem = new JMenuItem("Open");
	     openMenuItem.setActionCommand("Open");

	     JMenuItem saveMenuItem = new JMenuItem("Save");
	     saveMenuItem.setActionCommand("Save");

	     JMenuItem exitMenuItem = new JMenuItem("Exit");
	     exitMenuItem.setActionCommand("Exit");
	     exitMenuItem.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	                System.exit(0);
	            }
	        });


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



		area.add(inventory, BorderLayout.EAST);
		return area;
	}

    public void handleAction(Item item, Item.Action action) {
        //TODO
    }

    private class ImagePanel extends JPanel {
		private BufferedImage image;

		public ImagePanel(String item) {
			//image = loader.getImage(item);
			//image = image.getSubimage(0, 0, image.getWidth(), image.getHeight());

			this.setPreferredSize(new Dimension(40, 40));
			this.setBackground(Color.DARK_GRAY);

		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			//g.drawImage(image, 5, 5, this.getWidth() - 5, this.getHeight() - 5, null);

		}
	}

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

	public static void main(String[] args) {
//		GameData gameData = new GameData("/u/students/holdawscot/saveFile1.xml");
//		Game game = new Game (gameData);
//		final Player player1 = game.getPlayers().get(0);
//    	player1.setFacingDirection(Direction.NORTH);
//    	player1.setRoom(game.getData().getRoom("testRoom1"));
//
//        final Player player = new Player("Player 1", "characters/alien1.png") {
//            @Override
//            public Room getRoom() {
//                return game.getData().getRoom("testRoom1");
//            }
//        };
//        player.setFacingDirection(Direction.NORTH);








		final Player player2 = new Player("Player 2", "characters/alien2.png");
    	player2.setFacingDirection(Direction.NORTH);

		final ResourceLoader loader = new ResourceLoader("resources");
        final Room room = new Room("Some name") {
            @Override
            public boolean hasWall(Direction position) {
                return true;
            }

            {
            	 Item item = new Furniture("Bucket", "Looks like this could be used to hold liquid of some sort ...", "objects/bucket.png");
                 getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.Point3D(160, 0, 160)));

                 item = new Container("Crate", "There might be something inside!", "objects/crate.png");
                 getItems().add(new Room.ItemInstance(item, Direction.EAST, new Drawable.Point3D(80, 0, 240)));

                 item = new Door("Door", "You can get to [insert room here] through here.", "objects/door.png");
                 getItems().add(new Room.ItemInstance(item, Direction.WEST, new Drawable.Point3D(320, -10, 160)));

                 getPlayers().add(player2);
            }
        };
        player2.setRoom(room);

        final Player player = new Player("Player 1", "characters/alien1.png") {
            @Override
            public Room getRoom() {
                return room;
            }
        };
        player.setFacingDirection(Direction.NORTH);

		SynthLookAndFeel lookAndFeel = new SynthLookAndFeel();
		try {
			lookAndFeel.load(ApplicationWindow.class.getResourceAsStream("style/synthStyle.xml"), ApplicationWindow.class);
			UIManager.setLookAndFeel(lookAndFeel);
		}
		catch (ParseException | UnsupportedLookAndFeelException e) {
			JOptionPane.showMessageDialog(null, "Could not load UI style: " + e.getMessage());
		}

		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ApplicationWindow aw = new ApplicationWindow(player);
                aw.pack();
                aw.setVisible(true);
            }
        });
	}

}