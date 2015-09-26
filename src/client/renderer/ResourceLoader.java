package client.renderer;

import game.Player;
import org.eclipse.jdt.annotation.NonNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResourceLoader {
    private @NonNull String root;
    private @NonNull Map<String, BufferedImage> imageCache = new HashMap<>();
    private @NonNull BufferedImage notFound = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);

    public ResourceLoader(@NonNull String root) {
        this.root = root;
        notFound.setRGB(0, 0, 0xff0000);
    }

    public @NonNull BufferedImage getImage(@NonNull String basename) {
        if (imageCache.containsKey(basename)) {
            return imageCache.get(basename);
        }
        else {
            try {
                File imageFile = new File(root + "/" + basename + ".png");
                BufferedImage image = ImageIO.read(imageFile);
                imageCache.put(basename, image);
                return image;
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error: Couldn't find image " + root + "/" + basename);
                return notFound;
            }
        }
    }

    public @NonNull BufferedImage getSprite(@NonNull String basename, Player.Position viewingDirection) {
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
