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
    private class SceneItem {
        public Drawable drawable;
        public Point screenPosition;
        public Rectangle screenBoundingBox;

        public SceneItem(Drawable drawable, Point screenPosition, Rectangle screenBoundingBox) {
            this.drawable = drawable;
            this.screenPosition = screenPosition;
            this.screenBoundingBox = screenBoundingBox;
        }
    }

    private @NonNull Game game;
    private @NonNull BufferedImage image = new BufferedImage(1920, 1080, BufferedImage.TYPE_4BYTE_ABGR);

    private @Nullable Image background;
    private @NonNull List<SceneItem> currentSceneItems = new ArrayList<>();

    public RoomRenderer(@NonNull Game game, @NonNull Player player) {
        this.game = game;
        loadRoomFor(player);
    }

    public void loadRoomFor(Player player) {
        currentSceneItems.clear();
        loadRoom(player.getRoom(), player, 1.0);
    }

    public BufferedImage getCurrentImage() {
        return image;
    }

    public Drawable getObjectAt(Point point) {
        for (SceneItem item : currentSceneItems) {
            if (item.screenBoundingBox.contains(point)) {
                return item.drawable;
            }
        }
        return null;
    }

    private void loadRoom(Room room, Player player, double scale) {
        Player.Position position = player.getPosition();
        setWalls(room, player, scale);

        List<Drawable> roomObjects = new ArrayList<Drawable>(room.getItems());
        for (Player p : game.getPlayers()) {
            if (p.getRoom().equals(room) && !p.equals(player)) {
                roomObjects.add(p);
            }
        }

//        Collections.sort(roomObjects, DrawableComparator.from(position));

        for (Drawable drawable : roomObjects) {
            addSceneItem(drawable, scale);
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

    private void addSceneItem(Drawable drawable, double scale) {
//        Point position = getPositionOnScreen(drawable, scale);
//        Rectangle boundingBox = getBoundingBox(drawable, scale);
//        currentSceneItems.add(new SceneItem(drawable, position, boundingBox));
    }
}
