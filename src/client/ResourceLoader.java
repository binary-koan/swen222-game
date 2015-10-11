package client;

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

public class ResourceLoader {
    private @NonNull String root;
    private @NonNull Map<String, BufferedImage> imageCache = new HashMap<>();
    private @NonNull BufferedImage notFound = new BufferedImage(32, 32, BufferedImage.TYPE_4BYTE_ABGR);

    public ResourceLoader(@NonNull String root) {
        this.root = root;

        Graphics2D graphics = notFound.createGraphics();
        graphics.setColor(new Color(0xff0000));
        graphics.fillRect(0, 0, 32, 32);
    }

    public @NonNull BufferedImage getImage(@NonNull String basename) {
        if (imageCache.containsKey(basename)) {
            return imageCache.get(basename);
        }
        else {
            Path path = Paths.get(root + "/" + basename);
            try {
                File imageFile = new File(path.toAbsolutePath().toString());
                BufferedImage image = ImageIO.read(imageFile);
                imageCache.put(basename, image);
                return image;
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error: Couldn't find image " + path.toAbsolutePath().toString());
                return notFound;
            }
        }
    }

    public @NonNull BufferedImage getSprite(@NonNull String basename, Direction viewingDirection) {
        BufferedImage image = getImage(basename);
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
