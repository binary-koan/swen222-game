package gui;

import game.Direction;
import game.Game;
import game.Player;
import game.Room;
import gui.actions.SinglePlayerClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.synth.SynthLookAndFeel;


public class GameMenu {

	private JFrame frame;
	private int width;
	private int height;
	

	public GameMenu(final ResourceManager loader) {
		EventQueue.invokeLater(new Runnable() {

			
		public void run() {
//				SynthLookAndFeel lookAndFeel = new SynthLookAndFeel();
//				try {
//					lookAndFeel.load(ApplicationWindow.class.getResourceAsStream("style/synthStyle.xml"), ApplicationWindow.class);
//					UIManager.setLookAndFeel(lookAndFeel);
//				}
//				catch (ParseException e) {
//					JOptionPane.showMessageDialog(null, "Could not load UI style: " + e.getMessage());
//				}
//				catch (UnsupportedLookAndFeelException e)
				frame = new JFrame("Star Wars");
				width = 700;
				height = 500;
				frame.setLayout(new BorderLayout());
				frame.setPreferredSize(new Dimension(width, height));
				CharacterView c = new CharacterView(loader);
				frame.add(new GameWindow(loader, c), BorderLayout.WEST);
				frame.add(c, BorderLayout.EAST);
				frame.setResizable(false);
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
				



			}

		});
	}
	
	private class GameWindow extends JPanel {
		
		public GameWindow(ResourceManager loader, CharacterView info) {
			setLayout(new GridLayout(4, 1));
			setPreferredSize(new Dimension(width/2, height));
			JTextField port = new JTextField();
			JTextField url = new JTextField();
			JTextField gameName = new JTextField();
			add(new JLabel("Port"));
			add(port);
			add(new JLabel("URL"));
			add(url);
			add(new JLabel("Game Name"));
			add(gameName);
			
			JButton client = new JButton("Client");
			client.addActionListener(new ActionListener() {
				 
	            public void actionPerformed(ActionEvent e)
	            {
	            	
	            	if (!gameName.getText().isEmpty() && !info.playerName().isEmpty() ) {
	            	final Game game; 
	            	Player runPlayer;
	            	
	            	if (new File("resources/"+gameName.getText()+".xml").exists()){
	            		game= new Game("resources/"+gameName.getText()+".xml", "resources/"+gameName.getText()+".xml");
	            		if(game.getPlayer(info.playerName()) != null){
	            			runPlayer = game.getPlayer(info.playerName());
	            			for (Entry<String, Room> r : game.getRooms().entrySet()) {
	            				r.getValue().getPlayers().clear();
	            			}
	            			game.addPlayer(runPlayer);
	            	}
	            		
	            		else{
	            			runPlayer = new Player(info.playerName(), "characters/alien" +
			            			 info.getCharacterImage() + ".png", game.getRoom("rx1y2"));
	            			for (Entry<String, Room> r : game.getRooms().entrySet()) {
	            				r.getValue().getPlayers().clear();
	            			}
	            			game.addPlayer(runPlayer);
	            		}
	            	} else {
	            		
	            		game= new Game("resources/mainGame.xml", "resources/"+gameName.getText()+".xml");
	            		
		            	final Player player;
		            	player = new Player(info.playerName(), "characters/alien" +
		            			 info.getCharacterImage() + ".png", game.getRoom("rx1y2"));
		            
		            	player.turn(Direction.NORTH);
		                game.addPlayer(player);
		                game.saveGame();
		                runPlayer = player;
	            		
	            	}
	            	
	            	 SwingUtilities.invokeLater(new Runnable() {
		                    public void run() {
		                        ApplicationWindow aw = new ApplicationWindow(
		                                loader, frame, game, runPlayer, new SinglePlayerClient()
		                        );
		                        aw.pack();
		                        aw.setVisible(true);
		                    }
		                });
	            	} else {
	            		JOptionPane.showMessageDialog(null,"Please Enter Gamename and Player Name",
	            				"WARNING",JOptionPane.PLAIN_MESSAGE);
	            	}
	            	
	            
	                
	               
	            }
	        }); 

	
			
			JButton server = new JButton("Server");
			add(client);
			add(server);
		}
	}
	
	private class CharacterView extends JPanel {

		private int nextAlien;
		private JTextField name;
		
		public CharacterView(ResourceManager loader) {
			nextAlien = 1;
			
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			setPreferredSize(new Dimension(width/2, height));
			final JPanel characterImage = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(loader.getSprite("characters/alien" + nextAlien + ".png", Direction.NORTH),
							80, 0, getWidth()/2, getHeight(), null);
				}
				
				@Override
				public Dimension getMaximumSize() {
				    return new Dimension(width/2, 200);
				}
				
			};
			
		
			
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new BorderLayout());
			buttonPane.setMaximumSize(new Dimension(width/2, 80));
			JButton buttonLeft = new JButton();
			buttonLeft.setPreferredSize(new Dimension(150, 80));
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
			buttonRight.setPreferredSize(new Dimension(150, 80));
			buttonRight.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (nextAlien == 5) {
						nextAlien = 1;
					}
					else {
						nextAlien++;
					}
					characterImage.repaint();
				}
			});
			BufferedImage image =loader.getImage("ui/arrow-left.png");
			image.getScaledInstance(buttonLeft.getWidth() - 20,	buttonLeft.getHeight() - 10, image.SCALE_DEFAULT);
			buttonLeft.setIcon(new ImageIcon(image));
			image = loader.getImage("ui/arrow.png");
			image.getScaledInstance(buttonLeft.getWidth() - 20, buttonLeft.getHeight() - 10, image.SCALE_DEFAULT);
			buttonRight.setIcon(new ImageIcon(image));
			
			buttonPane.add(buttonLeft, BorderLayout.WEST);
			buttonPane.add(buttonRight, BorderLayout.EAST);
				
			
			JPanel characterName = new JPanel();
			characterName.setLayout(new BorderLayout());
			name = new JTextField();
			characterName.setMaximumSize(new Dimension(width / 2, 40));
			name.setPreferredSize(new Dimension(150, 20));
			characterName.add(new JLabel("Enter Character name:"), BorderLayout.CENTER);
			characterName.add(name, BorderLayout.EAST);
			
			add(characterImage, BorderLayout.NORTH);
			//add(Box.createRigidArea(new Dimension(5, 0)));
			add(buttonPane, BorderLayout.CENTER);
		//	add(Box.createRigidArea(new Dimension(5,0)));
			add(characterName, BorderLayout.SOUTH);
		}
		
		public int getCharacterImage() {
			return nextAlien;
		}
		
		public String playerName() {
			return name.getText();
		}
	
	}

	
}