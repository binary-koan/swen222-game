package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class SplashScreen extends JFrame implements ActionListener {
    private class ImagePanel extends JButton {
        private BufferedImage image;

        public ImagePanel(BufferedImage image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            double scale = Math.min((double)getWidth() / image.getWidth(), (double)getHeight() / image.getHeight());
            int width = (int) (image.getWidth() * scale);
            int height = (int) (image.getHeight() * scale);
            Point offset = new Point((getWidth() - width) / 2, (getHeight() - height) / 2);

            Image scaled = image.getScaledInstance(width, height, Image.SCALE_FAST);
            g.drawImage(scaled, offset.x, offset.y, width, height, null, null);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(image.getWidth() * 2, image.getHeight() * 2);
        }
    }

    private ResourceManager loader = new ResourceManager("resources");

    public SplashScreen() {
        loader.setMusic("audio/music-title.wav");

        ImagePanel content = new ImagePanel(loader.getImage("title-screen.png"));
        content.setCursor(new Cursor(Cursor.HAND_CURSOR));
        content.addActionListener(this);
        setContentPane(content);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        new GameMenu(loader);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SplashScreen aw = new SplashScreen();
                aw.pack();
                aw.setVisible(true);
            }
        });
    }
}
