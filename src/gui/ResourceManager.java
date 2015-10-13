package gui;

import game.Direction;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads and caches resources such as images and sprites, to be displayed by the renderer
 *
 * @author Jono Mingard
 */
public class ResourceManager implements LineListener {
    private String root;
    private Map<String, BufferedImage> imageCache = new HashMap<>();
    private BufferedImage notFound = new BufferedImage(32, 32, BufferedImage.TYPE_4BYTE_ABGR);

    private Clip musicPlayer;

    /**
     * Create a new ResourceManager which will load all files relative to the given root directory
     *
     * @param root directory to load resources from
     */
    public ResourceManager(String root) {
        this.root = root;

        Graphics2D graphics = notFound.createGraphics();
        graphics.setColor(new Color(0xff0000));
        graphics.fillRect(0, 0, 32, 32);
    }

    /**
     * Loads and returns the image at the given file path. Shows a message dialog and returns a red square if the image
     * cannot be loaded
     *
     * @param filename path to the image file to load (relative to the root directory)
     * @return the loaded image
     */
    public BufferedImage getImage(String filename) {
        if (imageCache.containsKey(filename)) {
            return imageCache.get(filename);
        }
        else {
            Path path = Paths.get(root + "/" + filename);
            try {
                File imageFile = new File(path.toAbsolutePath().toString());
                BufferedImage image = ImageIO.read(imageFile);
                imageCache.put(filename, image);
                return image;
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error: Couldn't find image " + path.toAbsolutePath().toString());
                return notFound;
            }
        }
    }

    /**
     * Loads and returns one image from the spritesheet at the given file path. Spritesheets are assumed to be 4x1
     * frames, representing the NORTH, EAST, WEST and SOUTH faces of the object respectively
     *
     * @param filename image file path {@link #getImage}
     * @param viewingDirection direction the sprite is being viewed from
     * @return one frame of the loaded image
     */
    public BufferedImage getSprite(String filename, Direction viewingDirection) {
        BufferedImage image = getImage(filename);
        if (image == notFound) {
            return image;
        }
        else {
            int tileSize = image.getWidth() / 4;
            switch (viewingDirection) {
                case NORTH:
                    return image.getSubimage(0, 0, tileSize, image.getHeight());
                case EAST:
                    return image.getSubimage(tileSize, 0, tileSize, image.getHeight());
                case WEST:
                    return image.getSubimage(tileSize * 2, 0, tileSize, image.getHeight());
                case SOUTH:
                default:
                    return image.getSubimage(tileSize * 3, 0, tileSize, image.getHeight());
            }
        }
    }

    /**
     * Stop any existing music and start looping the given track continuously
     *
     * @param source audio file path, relative to the resource root
     */
    public void setMusic(String source) {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.close();
        }

        try {
            musicPlayer = getAudioClip(source);
            musicPlayer.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to play music: " + e.getMessage());
        }
    }

    /**
     * Play a one-off sound effect over the current music
     *
     * @param source audio file path, relative to the resource root
     */
    public void playSoundEffect(String source) {
        try {
            Clip effect = getAudioClip(source);
            effect.addLineListener(this);
            effect.start();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to play sound effect: " + e.getMessage());
        }
    }

    /**
     * Create and return an audio clip corresponding to the given file
     */
    private Clip getAudioClip(String source) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File audioFile = new File(root + "/" + source);
        AudioInputStream input = AudioSystem.getAudioInputStream(audioFile);
        DataLine.Info info = new DataLine.Info(Clip.class, input.getFormat());

        Clip player = (Clip) AudioSystem.getLine(info);
        player.open(input);
        return player;
    }

    @Override
    public void update(LineEvent event) {
        // Ensure sound effect files are closed when the effect finishes
        if (event.getType() == LineEvent.Type.STOP) {
            event.getLine().close();
        }
    }
}
