package gui;

import game.Direction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.synth.SynthLookAndFeel;


public class GameMenu {

	public static void main(String[] args) {
		new GameMenu();
	}

	public GameMenu() {
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
				JFrame frame = new JFrame("Star Wars");
				frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
				frame.add(Box.createHorizontalGlue());
				frame.add(new GameWindow());
				frame.add(Box.createRigidArea(new Dimension(10, 0)));
				frame.add(new CharacterView());
				frame.setResizable(true);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);



			}

		});
	}
	
	private class GameWindow extends JPanel {
		
		public GameWindow() {
			setLayout(new BorderLayout());
			JTextField text = new JTextField();
			text.setBorder(BorderFactory.createLineBorder(Color.blue));
			add(text, BorderLayout.CENTER);
			
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(BorderFactory.createLineBorder(Color.blue));
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
			//buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
			
			
			JButton connect = new JButton("Connect");
			JButton newServer = new JButton("New Server");
			
			buttonPane.add(Box.createHorizontalGlue());
			buttonPane.add(connect);
			buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
			buttonPane.add(newServer);
			buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
			
			add(buttonPane, BorderLayout.PAGE_END);
		}
	}
	
	private class CharacterView extends JPanel {
		
		 private final ResourceLoader loader;
		 private int nextAlien;
		
		public CharacterView() {
			this.loader = new ResourceLoader("resources");
			nextAlien = 1;
			
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			JPanel characterImage = new JPanel() {
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
			characterName.add(new JTextField());
			characterName.setBorder(BorderFactory.createLineBorder(Color.blue));
			
			add(characterImage, BorderLayout.NORTH);
			add(Box.createRigidArea(new Dimension(5,0)));
			add(buttonPane, BorderLayout.CENTER);
			add(Box.createRigidArea(new Dimension(5,0)));
			add(characterName, BorderLayout.SOUTH);
			
			
			
			
			
		}
	}

	
}