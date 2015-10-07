package client;

import game.Bed;
import game.Chest;
import game.Direction;
import game.Door;
import game.Drawable;
import game.Item;
import game.Key;
import game.Player;
import game.Room;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import client.renderer.ResourceLoader;


public class GameMenu {

	public static void main(String[] args) {
		new GameMenu();
	}

	public GameMenu() {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				JFrame frame = new JFrame("Haunted House");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLayout(new BorderLayout());
				frame.add(new GameOptions(frame), BorderLayout.WEST);
				frame.add(new ImagePanel(), BorderLayout.EAST);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);



			}

		});
	}

	public class ImagePanel extends JPanel {
		
		private BufferedImage image;
		
		public ImagePanel() {
			try {
				image = ImageIO.read(new File("resources/backgrounds/haunted_house_contruction.jpg"));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, null);
		}
	}
	
	public class GameOptions extends JPanel {

		private List<String> menuItems;
		private String selectMenuItem;
		private String focusedItem;

		private MenuItemPainter painter;
		private Map<String, Rectangle> menuBounds;


		public GameOptions(final JFrame frame) {
			setBackground(Color.BLACK);
			painter = new SimpleMenuItemPainter();
			menuItems = new ArrayList<>(25);
			menuItems.add("Start Game");
			menuItems.add("Options");
			menuItems.add("Exit");
			selectMenuItem = menuItems.get(0);

			MouseAdapter ma = new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					String newItem = null;
					for (String text : menuItems) {
						Rectangle bounds = menuBounds.get(text);
						if (bounds.contains(e.getPoint())) {
							newItem = text;
							break;
						}
					}
					if (newItem != null && !newItem.equals(selectMenuItem)) {
						selectMenuItem = newItem;
						repaint();
					}

					if (selectMenuItem == "Start Game") {
//						final Player player2 = new Player("Player 2", "characters/alien2.png");
//						player2.setFacingDirection(Direction.NORTH);
//
//						final ResourceLoader loader = new ResourceLoader("resources");
//						final Room room = new Room("Some name") {
//							{
//								Item item = new Bed("Bed", "", "objects/bucket.png");
//								//getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.Point3D(80, 0, 80)));
//
//								item = new Chest("Chest", "", "objects/crate.png");
//								getItems().add(new Room.ItemInstance(item, Direction.EAST, new Drawable.Point3D(40, 0, 120)));
//
//								item = new Key("Key", "", "objects/key-blue.png");
//								getItems().add(new Room.ItemInstance(item, Direction.NORTH, new Drawable.Point3D(140, 60, 20)));
//
//								item = new Door("Door", "", "objects/door.png");
//								getItems().add(new Room.ItemInstance(item, Direction.WEST, new Drawable.Point3D(140, 0, 80)));
//
//								getPlayers().add(player2);
//							}
//						};
//						player2.setRoom(room);
//
//						final Player player = new Player("Player 1", "characters/alien1.png") {
//							@Override
//							public Room getRoom() {
//								return room;
//							}
//						};
//						player.setFacingDirection(Direction.NORTH);
//
//						final GameCanvas canvas = new GameCanvas(loader);
//
//						final KeyListener keyListener = new KeyListener() {
//							@Override
//							public void keyPressed(KeyEvent e) {
//								switch (e.getKeyCode()) {
//								case KeyEvent.VK_LEFT:
//									player.setFacingDirection(player.getFacingDirection().next());
//									break;
//								case KeyEvent.VK_RIGHT:
//									player.setFacingDirection(player.getFacingDirection().previous());
//								}
//								canvas.update();
//							}
//
//							@Override
//							public void keyTyped(KeyEvent e) {
//							}
//
//							@Override
//							public void keyReleased(KeyEvent e) {
//							}
//
//						};
//
//						canvas.addKeyListener(keyListener);
//
//						SwingUtilities.invokeLater(new Runnable() {
//							public void run() {
//								final ResourceLoader loader = new ResourceLoader("resources");
//
//								ApplicationWindow aw = new ApplicationWindow("Game", canvas, loader);
//								aw.addKeyListener(keyListener);
//								aw.pack();
//								aw.setVisible(true);
//
//
//								aw.getGameCanvas().setPlayer(player);
//
//							}
//						});
//
//						frame.setVisible(false);
//
						
						
					}
					if(selectMenuItem == "Exit") {
						System.exit(0);
					}

				}

				@Override
				public void mouseMoved(MouseEvent e) {
					focusedItem = null;
					for (String text : menuItems) {
						Rectangle bounds = menuBounds.get(text);
						if (bounds.contains(e.getPoint())) {
							focusedItem = text;
							repaint();
							break;
						}
					}
				}

			};

			addMouseListener(ma);
			addMouseMotionListener(ma);

			InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
			ActionMap am = getActionMap();

			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "arrowDown");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "arrowUp");

			am.put("arrowDown", new MenuAction(1));
			am.put("arrowUp", new MenuAction(-1));

		}

		@Override
		public void invalidate() {
			menuBounds = null;
			super.invalidate();
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(200, 200);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			if (menuBounds == null) {
				menuBounds = new HashMap<>(menuItems.size());
				int width = 0;
				int height = 0;
				for (String text : menuItems) {
					Dimension dim = painter.getPreferredSize(g2d, text);
					width = Math.max(width, dim.width);
					height = Math.max(height, dim.height);
				}

				int x = (getWidth() - (width + 10)) / 2;

				int totalHeight = (height + 10) * menuItems.size();
				totalHeight += 5 * (menuItems.size() - 1);

				int y = (getHeight() - totalHeight) / 2;

				for (String text : menuItems) {
					menuBounds.put(text, new Rectangle(x, y, width + 10, height + 10));
					y += height + 10 + 5;
				}

			}
			for (String text : menuItems) {
				Rectangle bounds = menuBounds.get(text);
				boolean isSelected = text.equals(selectMenuItem);
				boolean isFocused = text.equals(focusedItem);
				painter.paint(g2d, text, bounds, isSelected, isFocused);
			}
			g2d.dispose();
		}

		public class MenuAction extends AbstractAction {

			private final int delta;

			public MenuAction(int delta) {
				this.delta = delta;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = menuItems.indexOf(selectMenuItem);
				if (index < 0) {
					selectMenuItem = menuItems.get(0);
				}
				index += delta;
				if (index < 0) {
					selectMenuItem = menuItems.get(menuItems.size() - 1);
				} else if (index >= menuItems.size()) {
					selectMenuItem = menuItems.get(0);
				} else {
					selectMenuItem = menuItems.get(index);
				}
				repaint();
			}

		}

	}

	public interface MenuItemPainter {

		public void paint(Graphics2D g2d, String text, Rectangle bounds, boolean isSelected, boolean isFocused);

		public Dimension getPreferredSize(Graphics2D g2d, String text);

	}

	public class SimpleMenuItemPainter implements MenuItemPainter {

		public Dimension getPreferredSize(Graphics2D g2d, String text) {
			return g2d.getFontMetrics().getStringBounds(text, g2d).getBounds().getSize();
		}

		@Override
		public void paint(Graphics2D g2d, String text, Rectangle bounds, boolean isSelected, boolean isFocused) {
			FontMetrics fm = g2d.getFontMetrics();
			if (isSelected) {
				paintBackground(g2d, bounds, Color.BLUE, Color.WHITE);
			} else if (isFocused) {
				paintBackground(g2d, bounds, Color.MAGENTA, Color.BLACK);
			} else {
				paintBackground(g2d, bounds, Color.DARK_GRAY, Color.LIGHT_GRAY);
			}
			int x = bounds.x + ((bounds.width - fm.stringWidth(text)) / 2);
			int y = bounds.y + ((bounds.height - fm.getHeight()) / 2) + fm.getAscent();
			g2d.setColor(isSelected ? Color.WHITE : Color.LIGHT_GRAY);
			g2d.drawString(text, x, y);
		}

		protected void paintBackground(Graphics2D g2d, Rectangle bounds, Color background, Color foreground) {
			g2d.setColor(background);
			g2d.fill(bounds);
			g2d.setColor(foreground);
			g2d.draw(bounds);
		}

	}



}

