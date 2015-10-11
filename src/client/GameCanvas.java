package client;

import client.popups.ActionMenu;
import client.popups.ContentsMenu;
import client.popups.InfoTooltip;
import game.*;
import game.Container;
import game.Room.ItemInstance;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * A component which draws the room the current player is in, along with any tooltips or
 * popups caused by user interaction
 */
@SuppressWarnings("serial")
public class GameCanvas extends JPanel implements MouseListener, MouseMotionListener, ActionReceiver {
    private @NonNull ApplicationWindow parent;
    private @NonNull ResourceLoader loader;

    private double roomImageScale;
    private @Nullable Point roomImagePosition;
    private @Nullable RoomRenderer roomImage;

    private @Nullable Player player;

    private @NonNull InfoTooltip tooltip;
    private @Nullable JPopupMenu actionMenu;
    private @Nullable Drawable activeObject;

    /**
     * Construct a new game canvas
     *
     * @param parent the application window this canvas is connected to
     * @param loader a ResourceLoader which will be used to locate and read images, sprites, etc.
     */
    public GameCanvas(@NonNull ApplicationWindow parent, @NonNull ResourceLoader loader) {
        this.loader = loader;
        this.parent = parent;

        tooltip = new InfoTooltip(loader);
        tooltip.setVisible(false);
        add(tooltip);

        setLayout(null);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Set the player attached to this canvas, and update the canvas to display that player's viewpoint
     *
     * @param player player whose viewpoint will be rendered from now on
     */
    public void setPlayer(@Nullable Player player) {
        this.player = player;

        if (player == null) {
            roomImage = null;
        }
        else {
            roomImage = new RoomRenderer(loader, player);
            repaint();
        }
    }

    /**
     * Repaint the canvas based on the current player
     */
    public void update() {
        if (roomImage != null) {
            roomImage.updateRoom();
            repaint();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void performAction(Item item, Item.Action action) {
        if (player == null) {
            throw new RuntimeException("Attempting to perform an action with no player present");
        }

        // Attempt to find the item in the room's list of item instances
        //TODO move to room class
        ItemInstance drawable = null;
        for (ItemInstance instance: player.getRoom().getItems()) {
            if (instance.getItem().equals(item)) {
                drawable = instance;
                break;
            }
        }

        // Handle actions related to rendering, such as examining an object or showing a popup
        if (action == Item.Action.EXAMINE) {
            updateTooltip(drawable);
            tooltip.showDescription(item.getDescription());
            repositionTooltipAbove(drawable);
            tooltip.setVisible(true);
        }
        else if (action == Item.Action.SHOW_MENU) {
            tooltip.setVisible(false);

            actionMenu = new ActionMenu(this, item);
            Point position = positionAboveObject(drawable, actionMenu);
            actionMenu.show(this, position.x, position.y);
        }
        else if (action == Item.Action.SEARCH) {
            if (item instanceof Container) {
                actionMenu = new ContentsMenu(loader, this, (Container)item, "Click to pick up");
                Point position = positionAboveObject(drawable, actionMenu);
                actionMenu.show(this, position.x, position.y);
            }
        }
        else if (action != null) {
            // Pass all other actions up to the application window
            parent.performAction(item, action);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void performAction(Container container, Item item, Item.Action action) {
        // Not enough information to know if the action can be done or not - let the application window decide
        parent.performAction(container, item, action);
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getPreferredSize() {
    	return new Dimension(Room.ROOM_SIZE * 3, Room.CEILING_HEIGHT * 3);
    }

    /**
     * Draw a tooltip over the hovered object, if one is hovered
     *
     * @param e mouse movement event
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        // Make sure that the view is in a state where the tooltip should be updated
        if (roomImage == null || (actionMenu != null && actionMenu.isVisible()) ||
                tooltip.contains(e.getX() - tooltip.getX(), e.getY() - tooltip.getY())) {
            return;
        }

        Point scenePosition = calculateScenePosition(e);
        Drawable drawable = roomImage.getObjectAt(scenePosition);

        if (drawable == null) {
        	activeObject = null;
        	tooltip.setVisible(false);
        }
        else if (!drawable.equals(activeObject)) {
            updateTooltip(drawable);
            repositionTooltipAbove(drawable);
            tooltip.setVisible(true);
        }
    }

    /**
     * Perform the primary (left-click) or secondary (right-click) action of the active object
     *
     * @param e mouse click event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (roomImage == null || (actionMenu != null && actionMenu.isVisible())) {
            return;
        }

        Point scenePosition = calculateScenePosition(e);
        Drawable drawable = roomImage.getObjectAt(scenePosition);
        activeObject = drawable;

        if (drawable != null) {
            Item.Action action = (e.getButton() == MouseEvent.BUTTON1) ?
                    tooltip.getPrimaryAction() : tooltip.getSecondaryAction();

            if (drawable instanceof ItemInstance) {
                performAction(((ItemInstance)drawable).getItem(), action);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        if (roomImage != null) {
            // Paint the current room image so that it takes up the whole control
            BufferedImage image = roomImage.getCurrentImage();
            roomImageScale = Math.min((double)getWidth() / image.getWidth(), (double)getHeight() / image.getHeight());
            int width = (int) (image.getWidth() * roomImageScale);
            int height = (int) (image.getHeight() * roomImageScale);
            roomImagePosition = new Point((getWidth() - width) / 2, (getHeight() - height) / 2);

            Image scaled = image.getScaledInstance(width, height, Image.SCALE_FAST);
            g.drawImage(scaled, roomImagePosition.x, roomImagePosition.y, width, height, null, null);
        }

        if (player != null) {
            // Draw a HUD showing the player's location and direction
            g.setColor(Color.BLACK);
            g.drawString("In " + player.getRoom().getName() + " facing " + player.getFacingDirection().opposite(), 10, 20);
        }
    }

    /**
     * Display a tooltip above the given drawable object with information about the object
     */
    private void updateTooltip(Drawable drawable) {
        activeObject = drawable;

        if (drawable instanceof ItemInstance) {
            Item item = ((ItemInstance) drawable).getItem();
            List<Item.Action> actions = item.getAllowedActions();
            if (actions.size() > 2) {
                tooltip.showObject(item.getName(), item.getAllowedActions().get(0), Item.Action.SHOW_MENU);
            }
            else if (actions.size() > 1) {
                tooltip.showObject(item.getName(), item.getAllowedActions().get(0), item.getAllowedActions().get(1));
            }
            else if (actions.size() > 0) {
                tooltip.showObject(item.getName(), item.getAllowedActions().get(0), null);
            }
            else {
                tooltip.showObject(item.getName(), null, null);
            }
        }
        else if (drawable instanceof Player) {
            tooltip.showObject(((Player)drawable).getName(), null, null);
        }
    }

    /**
     * Set the tooltip's position to be above the given object
     */
    private void repositionTooltipAbove(Drawable drawable) {
        Point position = positionAboveObject(drawable, tooltip);
        Dimension size = tooltip.getPreferredSize();
        tooltip.setBounds(position.x, position.y, size.width, size.height);
    }

    /**
     * Return a Point representing where the component should be positioned in order to line up with the top-center
     * point of the object. Use the center of the screen if the position of the component cannot be determined
     */
    private Point positionAboveObject(Drawable drawable, JComponent component) {
        if (roomImage == null || roomImagePosition == null) {
            // This got called at completely the wrong time
            return new Point(0, 0);
        }

        Rectangle renderBounds = roomImage.getBounds(drawable);
        Dimension size = component.getPreferredSize();

        if (renderBounds != null) {
            int x = (int) ((renderBounds.x + renderBounds.width / 2) * roomImageScale * 5);
            int y = (int) ((renderBounds.y - 5) * roomImageScale * 5);

            return new Point(x + roomImagePosition.x - size.width / 2, y + roomImagePosition.y - size.height);
        }
        else {
            return new Point(getWidth() / 2 - size.width / 2, getHeight() / 2 - size.height / 2);
        }
    }

    /**
     * Return a Point representing where on the scene (relative to ROOM_SIZE and CEILING_HEIGHT) the mouse event is
     * positioned
     */
    private Point calculateScenePosition(MouseEvent e) {
        if (roomImagePosition == null) {
            return new Point(0, 0);
        }

		return new Point(
    			(int)((e.getX() - roomImagePosition.x) / (roomImageScale * RoomRenderer.RENDER_SCALE)),
    			(int)((e.getY() - roomImagePosition.y) / (roomImageScale * RoomRenderer.RENDER_SCALE))
    	);
	}

    // Stub events

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) { }
}
