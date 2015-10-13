package gui;

import game.Direction;
import game.Game;
import game.Player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


public class GameMenu {

	private JFrame frame;
	private int nextAlien;
	private JTextField name;
	
	public static void main(String[] args) {
		new GameMenu(new ResourceManager("resources"));
	}

	public GameMenu(final ResourceManager loader) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
//				SynthLookAndFeel lookAndFeel = new SynthLookAndFeel();
//				try {
//					lookAndFeel.load(ApplicationWindow.class.getResourceAsStream("style/synthStyle.xml"), ApplicationWindow.class);
//					UIManager.setLookAndFeel(lookAndFeel);
//				}
//				catch (ParseException | UnsupportedLookAndFeelException e) {
//					JOptionPane.showMessageDialog(null, "Could not load UI style: " + e.getMessage());
//				}
<<<<<<< HEAD
				frame = new JFrame("Star Wars");
//				frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
//				frame.add(Box.createHorizontalGlue());
//				frame.add(new GameWindow());
//				frame.add(Box.createRigidArea(new Dimension(10, 0)));
//				frame.add(new CharacterView());
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				Dimension scale = new Dimension();
				scale.setSize(screenSize.getWidth() * 0.5, screenSize.getHeight() * 0.6);
				frame.setPreferredSize(scale);
				frame.setLayout(new GridLayout(0, 2));
				frame.add(new GameWindow());
				frame.add(new CharacterView());
=======
				final JFrame frame = new JFrame("Star Wars");
				frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
				frame.add(Box.createHorizontalGlue());
				frame.add(new GameWindow(loader, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						//TODO either connect to server or make properly single-player
						//TODO use player settings from this menu
						final Game game = new Game("resources/mainGame.xml", "resources/continueGame.xml");
						final Player player2 = new Player("Player 2", "characters/alien2.png", game.getRoom("rx1y2"));
						player2.turn(Direction.NORTH);
						game.addPlayer(player2);

						final Player player = new Player("Player 1", "characters/alien1.png", game.getRoom("rx1y2"));
						player.turn(Direction.NORTH);
						game.addPlayer(player);

						ApplicationWindow window = new ApplicationWindow(loader, game, player, new SinglePlayerClient());
						window.pack();
						window.setVisible(true);

						frame.setVisible(false);
					}
				}));
				frame.add(Box.createRigidArea(new Dimension(10, 0)));
				frame.add(new CharacterView(loader));
>>>>>>> origin/master
				frame.setResizable(true);
				frame.pack();
				frame.setVisible(true);
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);



			}

		});
	}
	
	private class GameWindow extends JPanel {
		
<<<<<<< HEAD
		public GameWindow() {
			setLayout(new GridLayout(3, 2));
			JTextField port = new JTextField();
			JTextField url = new JTextField();
			add(new JLabel("Port"));
			add(port);
			add(new JLabel("URL"));
			add(url);
			JButton client = new JButton("Client");
			client.addActionListener(new ActionListener() {
				 
	            public void actionPerformed(ActionEvent e)
	            {
	            	final Game game = new Game("resources/mainGame.xml", "resources/continueGame.xml");
	            	final Player player2;
	            	player2 = new Player(name.getText(), "characters/alien" +
	            			 nextAlien + ".png", game.getRoom("rx1y2"));
	            	player2.turn(Direction.NORTH);
	                game.addPlayer(player2);
	                
	                ApplicationWindow aw = new ApplicationWindow(game, player2, new SinglePlayerClient());
	                aw.pack();
	                frame.setVisible(false);
	                aw.setVisible(true);
	            }
	        }); 
=======
		public GameWindow(final ResourceManager loader, ActionListener actionListener) {
			setLayout(new BorderLayout());
			JTextField text = new JTextField();
			text.setBorder(BorderFactory.createLineBorder(Color.blue));
			add(text, BorderLayout.CENTER);
			
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(BorderFactory.createLineBorder(Color.blue));
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
			//buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

			JButton connect = new JButton("Connect");
			connect.addActionListener(actionListener);
			JButton newServer = new JButton("New Server");
			
			buttonPane.add(Box.createHorizontalGlue());
			buttonPane.add(connect);
			buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
			buttonPane.add(newServer);
			buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
>>>>>>> origin/master
			
			JButton server = new JButton("Server");
			add(client);
			add(server);
		}
	}
	
	private class CharacterView extends JPanel {
<<<<<<< HEAD
		
		 private final ResourceManager loader;
=======
		 private int nextAlien;
>>>>>>> origin/master
		
		public CharacterView(final ResourceManager loader) {
			nextAlien = 1;
			
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			final JPanel characterImage = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(loader.getSprite("characters/alien" + nextAlien + ".png", Direction.NORTH),
							100, 0, getWidth()/2, getHeight(), null);
				}
				
				
			};
			
			characterImage.setBorder(BorderFactory.createLineBorder(Color.blue));
			
			JPanel buttonPane = new JPanel();
			JButton buttonLeft = new JButton();
			buttonLeft.addActionListener(new ActionListener() {
				 
	            public void actionPerformed(ActionEvent e)
	            {
	            	if (nextAlien == 1) {
	            		nextAlien = 5;
	            	} else {
	                nextAlien--;
	            	}
	            	characterImage.repaint();
	            }
	        }); 
			JButton buttonRight = new JButton();
			buttonRight.addActionListener(new ActionListener() {
				 
	            public void actionPerformed(ActionEvent e)
	            {
	            	if (nextAlien == 5) {
	            		nextAlien = 1;
	            	} else {
	                nextAlien++;
	            	}
	            	characterImage.repaint();
	            }
	        }); 
			ImageIcon i = new ImageIcon(loader.getImage("ui/arrow.png"));
			buttonLeft.setIcon(i);
			buttonRight.setIcon(i);
			buttonPane.add(buttonLeft);
			buttonPane.add(buttonRight);
				
			
			JPanel characterName = new JPanel();
			name = new JTextField();
			characterName.add(name);
			characterName.setBorder(BorderFactory.createLineBorder(Color.blue));
			
			add(characterImage, BorderLayout.NORTH);
			add(Box.createRigidArea(new Dimension(5, 0)));
			add(buttonPane, BorderLayout.CENTER);
			add(Box.createRigidArea(new Dimension(5,0)));
			add(characterName, BorderLayout.SOUTH);
			
			
			
			
			
		}
	}

	
}