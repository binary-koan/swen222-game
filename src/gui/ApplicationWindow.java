package gui;

import game.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.plaf.synth.SynthLookAndFeel;

import gui.actions.ActionHandler;


public class ApplicationWindow extends JFrame implements KeyListener {
	private static final long serialVersionUID = 6273791834646480175L;

    private Player player;
	private ActionHandler actionHandler;
	private ResourceLoader loader;
	private GameCanvas canvas;

	public ApplicationWindow(Game game, Player player, ActionHandler actionHandler) {
		super("Game");
        this.player = player;
        this.loader = new ResourceLoader("resources");
        this.canvas = new GameCanvas(loader, actionHandler);
        canvas.setup(player, game);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension scale = new Dimension();
		scale.setSize(screenSize.getWidth() * 0.5, screenSize.getHeight() * 0.6);
		setPreferredSize(scale);

		setLayout(new BorderLayout());

		//Add items to the frame
		add(setupMenuBar(), BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);
		add(setupLowerBar(), BorderLayout.SOUTH);

        addKeyListener(this);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
		area.setLayout(new GridLayout(1,2));

		JPanel leftBox = new JPanel();
		JPanel inventory = new JPanel();


		inventory.setLayout(new GridLayout(1,3, 4, 0));
		inventory.add(new ImagePanel("key"));
		inventory.add(new ImagePanel("fireplace"));
		inventory.add(new ImagePanel("Weapon"));


		area.add(leftBox);
		area.add(inventory);
		return area;
	}

    private class ImagePanel extends JPanel{
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
                player.turn(player.getFacingDirection().previous());
                break;
            case KeyEvent.VK_RIGHT:
                player.turn(player.getFacingDirection().next());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

	public static void main(String[] args) {
		//comment out below

		Game game = new Game("resources/mainGame.xml", "resources/continueGame.xml");
		final Player player2 = new Player("Player 2", "characters/alien2.png", game.getRoom("rx0y1"));
    	player2.turn(Direction.NORTH);

        final Player player = new Player("Player 1", "characters/alien1.png", game.getRoom("rx0y1"));

        player.turn(Direction.NORTH);

        //commentOutAbove



        //commentoutbelow

//		final ResourceLoader loader = new ResourceLoader("resources");
//        final Room room = new Room("sadasd", "Some name") {
//            @Override
//            public boolean hasWall(Direction position) {
//                return true;
//            }
//
//            {
//            	 Item item = new Furniture("sdas", "Bucket", "Looks like this could be used to hold liquid of some sort ...", "objects/bucket.png");
//                 getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.Point3D(160, 0, 160)));
//
//                 Container container = new Container("bggg", "Crate", "There might be something inside!", "objects/chest-blue.png");
//                 getItems().add(new Room.ItemInstance(container, Direction.EAST, new Drawable.Point3D(80, 0, 240)));
//
//				 container.getItems().add(new Furniture("some id", "Bucket 2", "Some other buckety thing", "objects/bucket.png"));
//
////                 item = new VisibleDoor("ssssss", "VisibleDoor", "You can get to [insert room here] through here.", "objects/door.png");
////                 getItems().add(new Room.ItemInstance(item, Direction.WEST, new Drawable.Point3D(320, -10, 160)));
//				roomConnections.put(Direction.NORTH, new Room("other", "room"));
//            }
//        };
//        final Player player2 = new Player("Player 2", "characters/alien2.png", room);
//        player2.turn(Direction.NORTH);
//
//        final Player player = new Player("Player 1", "characters/alien1.png", room);
//        player.turn(Direction.NORTH);

        //comment out above

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
                ApplicationWindow aw = new ApplicationWindow(game, player, new SinglePlayerClient(player));
                aw.pack();
                aw.setVisible(true);
            }
        });
	}

}