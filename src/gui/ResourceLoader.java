package gui;

import game.Direction;
import org.eclipse.jdt.annotation.NonNull;

import javax.imageio.ImageIO;
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
public class ResourceLoader {
    private String root;
    private Map<String, BufferedImage> imageCache = new HashMap<>();
    private BufferedImage notFound = new BufferedImage(32, 32, BufferedImage.TYPE_4BYTE_ABGR);

    /**
     * Create a new ResourceLoader which will load all files relative to the given root directory
     *
     * @param root directory to load resources from
     */
    public ResourceLoader(String root) {
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
}
