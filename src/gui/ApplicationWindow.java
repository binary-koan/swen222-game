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
import gui.renderer.HUD;


public class ApplicationWindow extends JFrame implements KeyListener, StateChangeListener {
	private static final long serialVersionUID = 6273791834646480175L;

	private JFrame gameMenu;
	private ResourceManager loader;

	private Game game;
    private Player player;
    private ActionHandler actionHandler;
	private GameCanvas canvas;
    private HUD hud;
	private ImagePanel inventory;

	public ApplicationWindow(ResourceManager loader, JFrame gameMenu, Game game, Player player, ActionHandler actionHandler) {
		super("Game");
		this.loader = loader;
		this.gameMenu = gameMenu;

		this.game = game;
        this.player = player;
        this.actionHandler = actionHandler;
        this.canvas = new GameCanvas(loader, actionHandler);

		game.addStateChangeListener(this);
		canvas.setup(player, game);
		loader.setMusic("audio/music-main.wav");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension scale = new Dimension();
		scale.setSize(screenSize.getWidth() * 0.5, screenSize.getHeight() * 0.6);
		canvas.setPreferredSize(scale);

		setLayout(new BorderLayout());

		JPanel lowerBar = setupLowerBar(loader);
		//Add items to the frame
		add(setupMenuBar(), BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);
		add(lowerBar, BorderLayout.SOUTH);

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
		area.setLayout(new BoxLayout(area, BoxLayout.LINE_AXIS));

		hud = new HUD(loader);
        hud.update(player);

        inventory = new ImagePanel("key", loader);
		JPanel inventoryPanel = new JPanel();
		inventoryPanel.setLayout(new BorderLayout());
		inventoryPanel.add(inventory, BorderLayout.EAST);

		area.add(hud);
        area.add(Box.createHorizontalGlue());
		area.add(inventoryPanel);
		return area;
	}

	@Override
	public void onStateChanged(Player player, StateChangeListener.Type type, String message) {
		if (message != null) {
			JOptionPane.showMessageDialog(getRootPane(), message);
		}

        if (type == StateChangeListener.Type.TURN || type == StateChangeListener.Type.MOVE) {
            hud.update(player);
        }
		else if (type == StateChangeListener.Type.PICK_UP) {
			inventory.setImage(loader.getSprite(player.getHeldItem().getSpriteName(), Direction.NORTH));
			inventory.repaint();
		}
        else if (type == StateChangeListener.Type.DROP) {
            inventory.setImage(null);
            inventory.repaint();
        }
		else if (type == StateChangeListener.Type.DIE) {
			setVisible(false);
			gameMenu.setVisible(true);
		}
	}

	private class ImagePanel extends JPanel implements MouseListener {
		private BufferedImage image;
		private ResourceManager loader;
		public ImagePanel(String item, ResourceManager loader) {
			
			this.loader = loader;
			
			addMouseListener(this);
			setPreferredSize(new Dimension(200, 100));
			this.setBackground(Color.DARK_GRAY);

		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (image != null) {
				g.drawImage(image, 10, 5, this.getWidth() /2, this.getHeight() - 5, null);
			}
			g.setColor(Color.DARK_GRAY);
			g.drawRect(0, 0, this.getWidth(), this.getHeight());
			
			

		}
		

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
            canvas.startDrop();
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


		public void setImage(BufferedImage image) {
			this.image = image;
		}
	}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                actionHandler.requestAction(new GameActions.Turn(player, player.getFacingDirection().previous()));
                break;
            case KeyEvent.VK_RIGHT:
                actionHandler.requestAction(new GameActions.Turn(player, player.getFacingDirection().next()));
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
