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
    private static final int RENDER_SCALE = 5;

    private static final Map<Direction, Comparator<Drawable>> sceneItemComparators = new HashMap<>();
    static {
        sceneItemComparators.put(Direction.NORTH, new Comparator<Drawable>() {
            @Override
            public int compare(Drawable o1, Drawable o2) {
                return Integer.compare(o1.getPosition().z, o2.getPosition().z);
            }
        });
        sceneItemComparators.put(Direction.WEST, new Comparator<Drawable>() {
            @Override
            public int compare(Drawable o1, Drawable o2) {
                return -Integer.compare(o1.getPosition().x, o2.getPosition().x);
            }
        });
        sceneItemComparators.put(Direction.EAST, new Comparator<Drawable>() {
            @Override
            public int compare(Drawable o1, Drawable o2) {
                return Integer.compare(o1.getPosition().x, o2.getPosition().x);
            }
        });
        sceneItemComparators.put(Direction.SOUTH, new Comparator<Drawable>() {
            @Override
            public int compare(Drawable o1, Drawable o2) {
                return -Integer.compare(o1.getPosition().z, o2.getPosition().z);
            }
        });
    }

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

    private @Nullable Image backgroundLeft;
    private @Nullable Image backgroundCenter;
    private @Nullable Image backgroundRight;
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
        addWalls(player.getRoom(), 1.0);
    }

    /**
     * Build the image corresponding to the current rendering list and return it
     *
     * @return the image that was created
     */
    public @NonNull BufferedImage getCurrentImage() {
        BufferedImage result = new BufferedImage(Room.ROOM_SIZE * RENDER_SCALE, Room.CEILING_HEIGHT * RENDER_SCALE, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = result.createGraphics();
        drawBackground(graphics);
        for (SceneItem item : currentSceneItems) {
            Rectangle bounds = item.screenBoundingBox;
            graphics.drawImage(
                    item.sprite,
                    bounds.x * RENDER_SCALE,
                    bounds.y * RENDER_SCALE,
                    bounds.width * RENDER_SCALE,
                    bounds.height * RENDER_SCALE,
                    null, null
            );
        }
        return result;
    }

    /**
     * Draw the left, central and right portions of the current room's background
     * @param graphics surface to draw on
     */
    private void drawBackground(Graphics2D graphics) {
        int x = 0;
        if (backgroundLeft != null) {
            graphics.drawImage(
                    backgroundLeft, x, 0,
                    Room.ROOM_SIZE * RENDER_SCALE / 4, Room.CEILING_HEIGHT * RENDER_SCALE,
                    null, null
            );
        }

        x += Room.ROOM_SIZE * RENDER_SCALE / 4;
        if (backgroundCenter != null) {
            graphics.drawImage(
                    backgroundCenter, x, 0,
                    Room.ROOM_SIZE * RENDER_SCALE / 4 * 2, Room.CEILING_HEIGHT * RENDER_SCALE,
                    null, null
            );
        }

        x += Room.ROOM_SIZE * RENDER_SCALE / 4 * 2;
        if (backgroundRight != null) {
            graphics.drawImage(
                    backgroundRight, x, 0,
                    Room.ROOM_SIZE * RENDER_SCALE / 4, Room.CEILING_HEIGHT * RENDER_SCALE,
                    null, null
            );
        }
    }

    /**
     * Return the topmost Drawable instance at the given point
     *
     * @param point point to check
     * @return the object at that point, or null if no object is found
     */
    public @Nullable Drawable getObjectAt(Point point) {
    	ListIterator<SceneItem> iterator = currentSceneItems.listIterator(currentSceneItems.size());
    	while (iterator.hasPrevious()) {
    		SceneItem item = iterator.previous();
    		if (item.screenBoundingBox.contains(point)) {
    			return item.drawable;
    		}
    	}
    	return null;
    }

    /**
     * Get the position and size on screen of a particular object
     */
    public @Nullable Rectangle getBounds(Drawable object) {
    	for (SceneItem item : currentSceneItems) {
    		if (item.drawable.equals(object)) {
    			return item.screenBoundingBox;
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

        List<Drawable> roomObjects = new ArrayList<>();
        roomObjects.addAll(room.getItems());
        if (room.getPlayers() != null) {
            for (Player p : room.getPlayers()) {
                if (p.getRoom().equals(room) && !p.equals(player)) {
                    roomObjects.add(p);
                }
            }
        }

        Collections.sort(roomObjects, sceneItemComparators.get(direction));

        for (Drawable drawable : roomObjects) {
            System.out.println("Drawing object: " + drawable.toString());

            BufferedImage sprite = loader.getSprite(drawable.getSpriteName(), drawable.getFacingDirection().viewFrom(direction));

            Drawable.Point3D position = drawable.getPosition();
            Rectangle screenBounds = calculateBoundingBox(position, sprite, direction);
            int z = calculateZIndex(position, direction);
            System.out.println(screenBounds.x + "," + screenBounds.y + "," + screenBounds.width + "," + screenBounds.height);

            scaleBoundingBox(screenBounds, z, scale, room);
            System.out.println(screenBounds.x + "," + screenBounds.y + "," + screenBounds.width + "," + screenBounds.height);

            currentSceneItems.add(new SceneItem(drawable, sprite, screenBounds));
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

        if (room.hasWall(position.previous())) {
            backgroundLeft = loader.getImage("backgrounds/room-wall-left.png");
        }
        else {
            backgroundLeft = loader.getImage("backgrounds/room-nowall-left.png");
        }

        if (room.hasWall(position.opposite())) {
            backgroundCenter = loader.getImage("backgrounds/room-wall-back.png");
        }
        else {
            backgroundCenter = loader.getImage("backgrounds/room-nowall-back.png");

            if (scale > 0.25) {
                Room next = room.getConnection(position.opposite());
                if (next != null) {
                    loadRoom(next, scale / 2);
                }
            }
        }

        if (room.hasWall(position.next())) {
            backgroundRight = loader.getImage("backgrounds/room-wall-right.png");
        }
        else {
            backgroundRight = loader.getImage("backgrounds/room-nowall-right.png");
        }
    }

    /**
     * Find the face of the cube facing in a given direction
     *
     * @param position the position of the object being viewed
     * @param sprite the image of the object which will be drawn
     * @param direction the direction from which the cube is being viewed
     * @return a rectangle representing the "front" face of the cube
     */
    private Rectangle calculateBoundingBox(Drawable.Point3D position, BufferedImage sprite, Direction direction) {
    	int width = sprite.getWidth();
    	int height = sprite.getHeight();

    	int y = Room.CEILING_HEIGHT - position.y - height;
    	int x;
        if (direction == Direction.NORTH) {
        	x = Room.ROOM_SIZE - position.x - width / 2;
        }
        else if (direction == Direction.SOUTH) {
        	x = position.x - width / 2;
        }
        else if (direction == Direction.WEST) {
        	x = Room.ROOM_SIZE - position.z - width / 2;
        }
        else {
        	x = position.z - width / 2;
        }

        return new Rectangle(x, y, width, height);
    }

    /**
     * Find and return the distance back an object is in the room, when viewed from a particular angle
     *
     * @param position the position of the object
     * @param direction the direction the room is being viewed from
     * @return a value from 0 to {@link Room#ROOM_SIZE} which represents how far back the object is from the viewer
     */
    private int calculateZIndex(Drawable.Point3D position, Direction direction) {
        switch (direction) {
            case NORTH:
                return position.z;
            case WEST:
                return Room.ROOM_SIZE - position.x;
            case EAST:
                return position.x;
            case SOUTH:
            default:
                return Room.ROOM_SIZE - position.z;
        }
    }

    /**
     * Scale the given rectangle based on its position within a room
     *
     * @param boundingBox the rectangle to scale (will be modified)
     * @param z the distance back the object is in the room (perpendicular direction to `boundingBox.x`)
     * @param scale the base scale multiplier
     * @param room the room that contains the object described by the rectangle
     */
    private void scaleBoundingBox(@NonNull Rectangle boundingBox, int z, double scale, @NonNull Room room) {
        double distanceBack = (float)z / Room.ROOM_SIZE;
        double scaleForObject = scale * (1 + distanceBack) / 2;

        int xFromCenter = (int)((boundingBox.x - Room.ROOM_SIZE / 2) * scaleForObject);
        int yFromCenter = (int)((boundingBox.y - Room.CEILING_HEIGHT / 2) * scaleForObject);

        int newWidth = (int)(boundingBox.width * scaleForObject);
        int newHeight = (int)(boundingBox.height * scaleForObject);

        boundingBox.setLocation((Room.ROOM_SIZE / 2) + xFromCenter, (Room.CEILING_HEIGHT / 2) + yFromCenter);
        boundingBox.setSize(newWidth, newHeight);
    }
}
