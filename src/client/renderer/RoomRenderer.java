package client.renderer;

import game.Game;
import game.Player;
import game.Room;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class RoomRenderer {
    private static final int DISPLAY_WIDTH = 1920;
    private static final int DISPLAY_HEIGHT = 1080;

    private class SceneItem {
        public Drawable drawable;
        public Rectangle screenBoundingBox;

        public SceneItem(Drawable drawable, Rectangle screenBoundingBox) {
            this.drawable = drawable;
            this.screenBoundingBox = screenBoundingBox;
        }
    }

    private @NonNull Game game;
    private @NonNull BufferedImage image = new BufferedImage(DISPLAY_WIDTH, DISPLAY_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);

    private @Nullable Image background;
    private @NonNull List<SceneItem> currentSceneItems = new ArrayList<>();

    public RoomRenderer(@NonNull Game game, @NonNull Player player) {
        this.game = game;
        loadPlayerRoom(player);
    }

    public void loadPlayerRoom(@NonNull Player player) {
        currentSceneItems.clear();
        loadRoom(player.getRoom(), player, 1.0);
    }

    public @NonNull BufferedImage getCurrentImage() {
        return image;
    }

    public @Nullable Drawable getObjectAt(Point point) {
        for (SceneItem item : currentSceneItems) {
            if (item.screenBoundingBox.contains(point)) {
                return item.drawable;
            }
        }
        return null;
    }

    private void loadRoom(@NonNull Room room, @NonNull Player player, double scale) {
        Player.Position position = player.getPosition();
        setWalls(room, player, scale);

        List<Drawable> roomObjects = new ArrayList<Drawable>(room.getItems());
        for (Player p : game.getPlayers()) {
            if (p.getRoom().equals(room) && !p.equals(player)) {
                roomObjects.add(p);
            }
        }

        Collections.sort(roomObjects, comparatorForPosition(position));

        for (Drawable drawable : roomObjects) {
            Rectangle boundingBox = boundingBoxFromDirection(position, drawable.getBoundingCube());
            boundingBox = scaleBoundingBox(boundingBox, scale, room);
            currentSceneItems.add(new SceneItem(drawable, boundingBox));
        }
    }

    private void setWalls(Room room, Player player, double scale) {
        Player.Position position = player.getPosition();

        if (room.getWallTexture() != null) {
            background = room.getWallTexture();
        }
        else if (scale > 0.25) {
            Room next = room.getConnection(position.opposite());
            if (next != null) {
                loadRoom(next, player, scale / 2);
            }
        }
    }

    private @NonNull Rectangle boundingBoxFromDirection(Player.Position position, Drawable.BoundingCube baseBounds) {
        switch (position) {
            case NORTH:
                return new Rectangle(DISPLAY_WIDTH - baseBounds.x, baseBounds.y, baseBounds.width, baseBounds.height);
            case SOUTH:
                return new Rectangle(baseBounds.x, baseBounds.y, baseBounds.width, baseBounds.height);
            case EAST:
                return new Rectangle(DISPLAY_WIDTH - baseBounds.z, baseBounds.y, baseBounds.depth, baseBounds.height);
            case WEST:
            default:
                return new Rectangle(baseBounds.z, baseBounds.y, baseBounds.depth, baseBounds.height);
        }
    }

    private @NonNull Rectangle scaleBoundingBox(@NonNull Rectangle boundingBox, double scale, Room room) {
        double distanceBack = boundingBox.x / room.getSize();
        double scaleForObject = scale * (1 + distanceBack) / 2;
        int newWidth = (int)(boundingBox.width * scaleForObject);
        int newHeight = (int)(boundingBox.height * scaleForObject);

        return new Rectangle(boundingBox.x + newWidth / 2, boundingBox.y + newHeight  / 2, newWidth, newHeight);
    }

    private Comparator<Drawable> comparatorForPosition(Player.Position position) {
        switch (position) {
            case NORTH:
                return new Comparator<Drawable>() {
                    @Override
                    public int compare(Drawable o1, Drawable o2) {
                        return Integer.compare(o1.getBoundingCube().z, o2.getBoundingCube().z);
                    }
                };
            case SOUTH:
                return new Comparator<Drawable>() {
                    @Override
                    public int compare(Drawable o1, Drawable o2) {
                        return -Integer.compare(o1.getBoundingCube().z, o2.getBoundingCube().z);
                    }
                };
            case EAST:
                return new Comparator<Drawable>() {
                    @Override
                    public int compare(Drawable o1, Drawable o2) {
                        return -Integer.compare(o1.getBoundingCube().x, o2.getBoundingCube().x);
                    }
                };
            case WEST:
            default:
                return new Comparator<Drawable>() {
                    @Override
                    public int compare(Drawable o1, Drawable o2) {
                        return Integer.compare(o1.getBoundingCube().x, o2.getBoundingCube().x);
                    }
                };
        }
    }
}
