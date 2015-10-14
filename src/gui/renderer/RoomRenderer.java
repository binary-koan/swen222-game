package gui.renderer;

import game.*;

import gui.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Given a player, this class renders the room that the player is in
 *
 * @author Jono Mingard
 */
public class RoomRenderer {
    /** The scale the room will be rendered at off-screen */
    public static final int RENDER_SCALE = 5;

    // A static map of comparators which can compare objects (based on their distance "back" in the scene) from any
    // direction
    private static final Map<Direction, Comparator<Drawable>> sceneItemComparators = new HashMap<>();
    static {
        sceneItemComparators.put(Direction.NORTH, new Comparator<Drawable>() {
            @Override
            public int compare(Drawable o1, Drawable o2) {
                return sortByType(Integer.compare(o1.getPosition().z, o2.getPosition().z), o1, o2);
            }
        });
        sceneItemComparators.put(Direction.WEST, new Comparator<Drawable>() {
            @Override
            public int compare(Drawable o1, Drawable o2) {
                return sortByType(-Integer.compare(o1.getPosition().x, o2.getPosition().x), o1, o2);
            }
        });
        sceneItemComparators.put(Direction.EAST, new Comparator<Drawable>() {
            @Override
            public int compare(Drawable o1, Drawable o2) {
                return sortByType(Integer.compare(o1.getPosition().x, o2.getPosition().x), o1, o2);
            }
        });
        sceneItemComparators.put(Direction.SOUTH, new Comparator<Drawable>() {
            @Override
            public int compare(Drawable o1, Drawable o2) {
                return sortByType(-Integer.compare(o1.getPosition().z, o2.getPosition().z), o1, o2);
            }
        });
    }

    /**
     * Check the given compare result. If it is zero, sort the given drawables by their type (players on top and doors
     * on bottom)
     */
    private static int sortByType(int compareResult, Drawable o1, Drawable o2) {
        if (compareResult == 0) {
            // If there are collisions, always show players on top and doors on the bottom
            if (o1 instanceof Player || o2 instanceof Door) {
                return 1;
            }
            else if (o2 instanceof Player || o1 instanceof Door) {
                return -1;
            }
        }
        return compareResult;
    }

    /**
     * An item in the list of all scene items
     */
    private class SceneItem {
        public final Drawable drawable;
        public final Image sprite;
        public final Rectangle screenBoundingBox;
        public final boolean interactable;

        public SceneItem(Drawable drawable, Image sprite, Rectangle screenBoundingBox, boolean interactable) {
            this.drawable = drawable;
            this.sprite = sprite;
            this.screenBoundingBox = screenBoundingBox;
            this.interactable = interactable;
        }
    }

    private ResourceManager loader;
    private Player player;

    // Using a LinkedHashMap makes sure elements are stored in insertion order so it can be iterated like a list
    private Map<Rectangle, Image> backgroundImages = new LinkedHashMap<>();
    private List<SceneItem> currentSceneItems = new ArrayList<>();

    /**
     * Construct a new RoomRenderer
     *
     * @param loader a resource loader which will be used to find sprites to draw
     * @param player the player that will be used to find the room this object will render
     */
    public RoomRenderer(ResourceManager loader, Player player) {
        this.loader = loader;
        this.player = player;

        updateRoom();
    }

    /**
     * Load the player's new room into the rendering list
     */
    public void updateRoom() {
        currentSceneItems.clear();
        backgroundImages.clear();

        Room room = player.getRoom();
        addInvisibleDoors(room);
        loadRoom(room, 1.0, true);
        addWalls(room, 1.0);
    }

    /**
     * Build the image corresponding to the current rendering list and return it
     *
     * @return the image that was created
     */
    public BufferedImage getCurrentImage() {
        BufferedImage result = new BufferedImage(Room.ROOM_SIZE * RENDER_SCALE, Room.CEILING_HEIGHT * RENDER_SCALE, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = result.createGraphics();
        drawBackground(graphics);
        for (SceneItem item : currentSceneItems) {
            if (item.sprite == null) {
                continue;
            }

            Rectangle bounds = item.screenBoundingBox;
            graphics.drawImage(
                    item.sprite,
                    bounds.x * RENDER_SCALE, bounds.y * RENDER_SCALE,
                    bounds.width * RENDER_SCALE, bounds.height * RENDER_SCALE, null
            );
        }

        graphics.drawImage(loader.getImage("backgrounds/overlay.png"), 0, 0, Room.ROOM_SIZE * RENDER_SCALE, Room.CEILING_HEIGHT * RENDER_SCALE, null);

        return result;
    }

    /**
     * Draw the left, central and right portions of the current room's background
     * @param graphics surface to draw on
     */
    private void drawBackground(Graphics2D graphics) {
        for (Map.Entry<Rectangle, Image> entry : backgroundImages.entrySet()) {
            Rectangle bounds = entry.getKey();
            graphics.drawImage(
                    entry.getValue(),
                    bounds.x * RENDER_SCALE, bounds.y * RENDER_SCALE,
                    bounds.width * RENDER_SCALE, bounds.height * RENDER_SCALE, null
            );
        }
    }

    /**
     * Return the topmost Drawable instance at the given point
     *
     * @param point point to check
     * @return the object at that point, or null if no object is found
     */
    public Drawable getObjectAt(Point point) {
        // Check all scene items in reverse (front to back) order
        ListIterator<SceneItem> iterator = currentSceneItems.listIterator(currentSceneItems.size());
    	while (iterator.hasPrevious()) {
    		SceneItem item = iterator.previous();

    		if (item.interactable && item.screenBoundingBox != null && item.screenBoundingBox.contains(point)) {
    			return item.drawable;
    		}
    	}

    	return null;
    }

    /**
     * Get the position and size on screen of a particular object
     *
     * @param object object to calculate dimensions from
     * @return a rectangle representing the position and size of the object
     */
    public Rectangle getBounds(Drawable object) {
        if (object == null) {
            return null;
        }

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
    private void loadRoom(Room room, double scale, boolean isCurrent) {
        Direction direction = player.getFacingDirection();

        // Recursively load adjacent rooms into the back of the scene
        if (scale > 0.25) {
            Room next = room.getConnection(direction.opposite());
            if (next != null && !room.hasWall(direction.opposite())) {
                loadRoom(next, scale / 2, false);
            }
        }

        List<Drawable> roomObjects = new ArrayList<>();
        roomObjects.addAll(room.getItems());

        addRoomPlayers(room, roomObjects);
        addDoors(room, roomObjects);

        Collections.sort(roomObjects, sceneItemComparators.get(direction));

        for (Drawable drawable : roomObjects) {
            Drawable.Point3D position = drawable.getPosition();
            int z = calculateZIndex(position, direction);

            // Don't render items which are too close to the viewer
            if (isCurrent && z > Room.ROOM_SIZE - 40) {
                continue;
            }

            boolean interactable = isCurrent;
            if (room.containsMonster()) {
                interactable = drawable.equals(room.getMonster()) || drawable instanceof Door;
            }

            if (drawable.getSpriteName() == null) {
                currentSceneItems.add(new SceneItem(drawable, null, null, interactable));
            }
            else {
                BufferedImage sprite = loader.getSprite(drawable.getSpriteName(), drawable.getFacingDirection().viewFrom(direction));
                Rectangle screenBounds = calculateBoundingBox(position, sprite, direction);
                scaleBoundingBox(screenBounds, z, scale);

                currentSceneItems.add(new SceneItem(drawable, sprite, screenBounds, interactable));
            }
        }
    }

    /**
     * Add all players in the room to the given list of room objects
     */
    private void addRoomPlayers(Room room, List<Drawable> roomObjects) {
        if (room.getPlayers() != null) {
            for (Player p : room.getPlayers()) {
                if (p.getRoom().equals(room) && !p.equals(player)) {
                    roomObjects.add(p);
                }
            }
        }
    }

    /**
     * Add visible doors in the given room to the list of room objects
     */
    private void addDoors(Room room, List<Drawable> roomObjects) {
        for (Direction direction : Direction.values()) {
            Room connection = room.getConnection(direction);
            if (connection != null) {
                if (room.hasWall(direction)) {
                    roomObjects.add(new VisibleDoor(room, direction, connection.getColor()));
                }
            }
        }
    }

    /**
     * Adds "wall-doors", invisible objects which act as doors to adjacent rooms. For example, if the player is facing
     * down a corridor, an invisible door will be created which links to the next room along
     */
    private void addInvisibleDoors(Room room) {
        Direction direction = player.getFacingDirection();

        if (room.getConnection(direction.previous()) != null && !room.hasWall(direction.previous())) {
            currentSceneItems.add(new SceneItem(
                    new InvisibleDoor(room, direction.previous()), null,
                    new Rectangle(0, 0, Room.ROOM_SIZE / 4, Room.CEILING_HEIGHT), true
            ));
        }

        if (room.getConnection(direction.opposite()) != null && !room.hasWall(direction.opposite())) {
            currentSceneItems.add(new SceneItem(
                    new InvisibleDoor(room, direction.opposite()), null,
                    new Rectangle(Room.ROOM_SIZE / 4, 0, Room.ROOM_SIZE / 4 * 2, Room.CEILING_HEIGHT), true
            ));
        }

        if (room.getConnection(direction.next()) != null && !room.hasWall(direction.next())) {
            currentSceneItems.add(new SceneItem(
                    new InvisibleDoor(room, direction.next()), null,
                    new Rectangle(Room.ROOM_SIZE / 4 * 3, 0, Room.ROOM_SIZE / 4, Room.CEILING_HEIGHT), true
            ));
        }
    }

    /**
     * Sets the scene background to the wall texture of the room. If the room does not have a wall texture, draw the
     * room in front instead
     *
     * @param room room to check for walls or neighbour
     */
    private void addWalls(Room room, double scale) {
        Direction position = player.getFacingDirection();

        int baseWidth = Room.ROOM_SIZE / 4;
        int height = Room.CEILING_HEIGHT;

        Rectangle bounds = new Rectangle(0, 0, baseWidth, height);
        scaleBoundingBox(bounds, Room.ROOM_SIZE, scale);
        if (room.hasWall(position.previous())) {
            backgroundImages.put(bounds, loader.getImage("backgrounds/room-wall-left.png"));
        }
        else {
            backgroundImages.put(bounds, loader.getImage("backgrounds/room-nowall-left.png"));
        }

        bounds = new Rectangle(baseWidth, 0, baseWidth * 2, height);
        scaleBoundingBox(bounds, Room.ROOM_SIZE, scale);
        if (room.hasWall(position.opposite())) {
            backgroundImages.put(bounds, loader.getImage("backgrounds/room-wall-back.png"));
        }
        else {
            backgroundImages.put(bounds, loader.getImage("backgrounds/room-nowall-back.png"));
        }

        bounds = new Rectangle(baseWidth * 3, 0, baseWidth, height);
        scaleBoundingBox(bounds, Room.ROOM_SIZE, scale);
        if (room.hasWall(position.next())) {
            backgroundImages.put(bounds, loader.getImage("backgrounds/room-wall-right.png"));
        }
        else {
            backgroundImages.put(bounds, loader.getImage("backgrounds/room-nowall-right.png"));
        }

        if (!room.hasWall(position.opposite()) && room.getConnection(position.opposite()) != null) {
            addWalls(room.getConnection(position.opposite()), scale / 2);
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
     * Find and return the distance back an object is in the room, when viewed from a particular angle. Lower values
     * indicate the object is further away; higher values indicate it's closer
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
     */
    private void scaleBoundingBox(Rectangle boundingBox, int z, double scale) {
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
