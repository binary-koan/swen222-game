package client.renderer;

import game.*;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Given a player, this class renders the room that the player is in
 */
public class RoomRenderer {
    private static final int DISPLAY_WIDTH = 160;
    private static final int DISPLAY_HEIGHT = 96;

    /**
     * An item in the list of all scene items
     */
    private class SceneItem {
        public final Drawable drawable;
        public final Image sprite;
        public final Rectangle screenBoundingBox;

        public SceneItem(Drawable drawable, Image sprite, Rectangle screenBoundingBox) {
            this.drawable = drawable;
            this.sprite = sprite;
            this.screenBoundingBox = screenBoundingBox;
        }
    }

    private @NonNull ResourceLoader loader;
    private @NonNull Player player;

    private @Nullable Image background;
    private @NonNull List<SceneItem> currentSceneItems = new ArrayList<>();

    /**
     * Construct a new RoomRenderer
     *
     * @param loader a resource loader which will be used to find sprites to draw
     * @param player the player that will be used to find the room this object will render
     */
    public RoomRenderer(@NonNull ResourceLoader loader, @NonNull Player player) {
        this.loader = loader;
        this.player = player;

        updateRoom();
    }

    /**
     * Load the player's new room into the rendering list
     */
    public void updateRoom() {
        currentSceneItems.clear();
        loadRoom(player.getRoom(), 1.0);
    }

    /**
     * Build the image corresponding to the current rendering list and return it
     *
     * @return the image that was created
     */
    public @NonNull BufferedImage getCurrentImage() {
        BufferedImage result = new BufferedImage(DISPLAY_WIDTH, DISPLAY_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = result.createGraphics();
        if (background != null) {
            graphics.drawImage(background, 0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT, null, null);
        }
        for (SceneItem item : currentSceneItems) {
            Rectangle bounds = item.screenBoundingBox;
            graphics.drawImage(item.sprite, bounds.x, bounds.y, bounds.width, bounds.height, null, null);
        }
        return result;
    }

    /**
     * Return the topmost Drawable instance at the given point
     *
     * @param point point to check
     * @return the object at that point, or null if no object is found
     */
    public @Nullable Drawable getObjectAt(Point point) {
        for (SceneItem item : currentSceneItems) {
            if (item.screenBoundingBox.contains(point)) {
                return item.drawable;
            }
        }
        return null;
    }

    /**
     * Load an entire room into the scene graph at the given scale
     *
     * @param room the room to add
     * @param scale the scale to use when calculating the size and position of items
     */
    private void loadRoom(@NonNull Room room, double scale) {
        Direction direction = player.getFacingDirection();
        addWalls(room, scale);

        List<Drawable> roomObjects = new ArrayList<>();
        roomObjects.addAll(room.getItems());
        if (room.getPlayers() != null) {
            for (Player p : room.getPlayers()) {
                if (p.getRoom().equals(room) && !p.equals(player)) {
                    roomObjects.add(p);
                }
            }
        }

        Collections.sort(roomObjects, comparatorForPosition(direction));

        for (Drawable drawable : roomObjects) {
            Image sprite = loader.getSprite(drawable.getSpriteName(), drawable.getFacingDirection().viewFrom(direction));
            Rectangle boundingBox = boundingBoxFromDirection(direction, drawable.getBoundingCube());
            boundingBox = scaleBoundingBox(boundingBox, scale, room);
            currentSceneItems.add(new SceneItem(drawable, sprite, boundingBox));
        }
    }

    /**
     * Sets the scene background to the wall texture of the room. If the room does not have a wall texture, draw the
     * room in front instead
     *
     * @param room room to check for walls or neighbour
     * @param scale scale currently being used to draw the room
     */
    private void addWalls(@NonNull Room room, double scale) {
        Direction position = player.getFacingDirection();

        if (room.getWallImage() != null) {
            background = loader.getImage(room.getWallImage());
        }
        else if (scale > 0.25) {
            Room next = room.getConnection(position.opposite());
            if (next != null) {
                loadRoom(next, scale / 2);
            }
        }
    }

    /**
     * Find the face of the cube facing in a given direction
     *
     * @param position the position from which the cube is being viewed
     * @param baseBounds the cube which is being viewed
     * @return a rectangle representing the "front" face of the cube
     */
    private @NonNull Rectangle boundingBoxFromDirection(Direction position, Drawable.BoundingCube baseBounds) {
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

    /**
     * Scale the given rectangle based on its position within a room
     *
     * @param boundingBox the rectangle to scale
     * @param scale the base scale multiplier
     * @param room the room that contains the object described by the rectangle
     * @return the scaled rectangle
     */
    private @NonNull Rectangle scaleBoundingBox(@NonNull Rectangle boundingBox, double scale, @NonNull Room room) {
        double distanceBack = boundingBox.x / room.getSize();
        double scaleForObject = scale * (1 + distanceBack) / 2;
        int newWidth = (int)(boundingBox.width * scaleForObject);
        int newHeight = (int)(boundingBox.height * scaleForObject);

        return new Rectangle(boundingBox.x + newWidth / 2, boundingBox.y + newHeight  / 2, newWidth, newHeight);
    }

    /**
     * Return a Comparator which compares Drawable objects, where "smaller" objects are those further back when viewed
     * from the given direction
     *
     * @param position position the objects are being viewed from
     * @return a Comparator which sorts the objects by how far "back" they are
     */
    private Comparator<Drawable> comparatorForPosition(Direction position) {
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
