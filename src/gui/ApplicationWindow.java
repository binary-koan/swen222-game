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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.plaf.synth.SynthLookAndFeel;

import gui.actions.ActionHandler;
import gui.actions.GameActions;


public class ApplicationWindow extends JFrame implements KeyListener{
	private static final long serialVersionUID = 6273791834646480175L;

	private Game game;
    private Player player;
    private ActionHandler actionHandler;
	private GameCanvas canvas;

	public ApplicationWindow(ResourceManager loader, Game game, Player player, ActionHandler actionHandler) {
		super("Game");
		this.game = game;
        this.player = player;
        this.actionHandler = actionHandler;
        this.canvas = new GameCanvas(loader, actionHandler);
        canvas.setup(player, game);

		loader.setMusic("audio/music-main.wav");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension scale = new Dimension();
		scale.setSize(screenSize.getWidth() * 0.5, screenSize.getHeight() * 0.6);
		setPreferredSize(scale);

		setLayout(new BorderLayout());

		JPanel inventory = setupLowerBar(loader);
		//Add items to the frame
		add(setupMenuBar(), BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);
		add(inventory, BorderLayout.SOUTH);

        addKeyListener(this);
     
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private JMenuBar setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Game");

	     JMenuItem openMenuItem = new JMenuItem("Open");
	     openMenuItem.setActionCommand("Open");

	     JMenuItem saveMenuItem = new JMenuItem("Save");
	     saveMenuItem.setActionCommand("Save");
	     saveMenuItem.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	            	game.getData().saveWholeGame();
	              
	            }
	        });
	     
	     JMenuItem exitMenuItem = new JMenuItem("Exit");
	     exitMenuItem.setActionCommand("Exit");
	     exitMenuItem.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	            	game.getData().saveWholeGame();
	                System.exit(0);
	            }
	        });


	     fileMenu.add(openMenuItem);
	     fileMenu.add(saveMenuItem);
	     fileMenu.addSeparator();
	     fileMenu.add(exitMenuItem);

	     menuBar.add(fileMenu);
	     menuBar.add(editMenu);

	     return menuBar;

	}

	private JPanel setupLowerBar(ResourceManager loader)  {
		JPanel area = new JPanel();
		area.setLayout(new GridLayout(1,2));

		JPanel leftBox = new JPanel();
		JPanel inventory = new JPanel();
		

		inventory.setLayout(new GridLayout(1,1, 0, 0));
		//inventory.setPreferredSize(new Dimension((int)(area.getWidth() * 0.2), (int)(canvas.getHeight() * 0.3)));
		inventory.add(new ImagePanel("key", loader));
		


		area.add(leftBox);
		area.add(inventory);
		return area;
	}

    private class ImagePanel extends JPanel implements StateChangeListener, MouseListener{
		private BufferedImage image;
		private ResourceManager loader;
		public ImagePanel(String item, ResourceManager loader) {
			
			this.loader = loader;
			game.addStateChangeListener(this);
			
			addMouseListener(this);
			this.setPreferredSize(new Dimension(40, 40));
			this.setBackground(Color.DARK_GRAY);

		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (image != null) {
				g.drawImage(image, 5, 5, this.getWidth() - 5, this.getHeight() - 5, null);
			}
			g.setColor(Color.DARK_GRAY);
			g.drawRect(0, 0, this.getWidth(), this.getHeight());
			
			

		}

		@Override
		public void onStateChanged(Player player, Type type, String message) {
			if (type == Type.PICK_UP) {
				this.image = loader.getSprite(player.getHeldItem().getSpriteName(), Direction.NORTH);
			}

			
			repaint();
			
		}
		

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			actionHandler.requestAction(new GameActions.Drop(player, player.getHeldItem()));	
			image = null;
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

	
	}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                actionHandler.requestAction(new GameActions.Turn(player, player.getFacingDirection().next()));
                break;
            case KeyEvent.VK_RIGHT:
                actionHandler.requestAction(new GameActions.Turn(player, player.getFacingDirection().previous()));
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

	public static void main(String[] args) {

		final Game game = new Game("resources/mainGame.xml", "resources/continueGame.xml");
		final Player player2 = new Player("Player 2", "characters/alien2.png", game.getRoom("rx1y2"));
    	player2.turn(Direction.NORTH);
        game.addPlayer(player2);

        final Player player = new Player("Player 1", "characters/alien1.png", game.getRoom("rx1y2"));
        player.turn(Direction.NORTH);
        game.addPlayer(player);
//        final Player player = game.getPlayer("Player 1");
//    	final Player player3 = new Player("Player 3", "characters/alien2.png", game.getRoom("rx2y3"));
//    	player3.turn(Direction.NORTH);
//        game.addPlayer(player3);

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
                ApplicationWindow aw = new ApplicationWindow(
                        new ResourceManager("resources"), game, player, new SinglePlayerClient()
                );
                aw.pack();
                aw.setVisible(true);
            }
        });
	}

	

}