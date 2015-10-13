package gui;

import game.Container;
import gui.actions.*;
import gui.popups.ActionMenu;
import gui.popups.ContentsMenu;
import gui.popups.InfoTooltip;
import game.*;
import game.Room.ItemInstance;

import gui.renderer.Door;
import gui.renderer.RoomRenderer;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;

import gui.actions.Action;

/**
 * A component which draws the room the current player is in, along with any tooltips or
 * popups caused by user interaction
 *
 * @author Jono Mingard
 */
@SuppressWarnings("serial")
public class GameCanvas extends JPanel implements MouseListener, MouseMotionListener, StateChangeListener {
    /**
     * An action handler which finds and performs any game action. It performs actions related to the GUI
     * ({@link GUIActions}) on its own, and passes actions it does not understand to a "parent" handler
     */
    private class CanvasActionHandler implements ActionHandler {
        private ActionHandler parent;

        /**
         * Create a new action handler
         *
         * @param parent handler to use for retrieving and performing additional actions (such as game state modifications)
         */
        public CanvasActionHandler(ActionHandler parent) {
            this.parent = parent;
        }

        /**
         * Return the allowed actions from the parent handler, plus any extras related to the GUI
         *
         * @param player the player performing the action
         * @param drawable the object to find actions for
         * @return a list of allowed actions
         */
        @Override
        public List<Action> getAllowedActions(Player player, Drawable drawable) {
            List<Action> result = parent.getAllowedActions(player, drawable);

            if (drawable instanceof ItemInstance && ((ItemInstance)drawable).getItem() instanceof Container) {
                ItemInstance instance = (ItemInstance)drawable;
                result.add(new GUIActions.Search(player, (Container)instance.getItem(), instance));
            }
            if (drawable instanceof ItemInstance || drawable instanceof Door) {
                result.add(new GUIActions.Examine(player, drawable));
            }
            if (result.size() > 2) {
                result.add(1, new GUIActions.ShowMenu(player, result.subList(1, result.size()), drawable));
            }

            return result;
        }

        /**
         * Perform actions related to the GUI, and pass anything else up to the parent handler
         *
         * @param action action to perform
         */
        @Override
        public void requestAction(Action action) {
            if (action instanceof GUIActions.Search) {
                showSearchMenu((GUIActions.Search)action);
            }
            else if (action instanceof GUIActions.Examine) {
                showDescription((GUIActions.Examine)action);
            }
            else if (action instanceof GUIActions.ShowMenu) {
                showActionMenu((GUIActions.ShowMenu)action);
            }
            else {
                parent.requestAction(action);
            }
        }
    }

    private ResourceManager loader;

    private CanvasActionHandler actionHandler;
    private double roomImageScale;

    private Point roomImagePosition;
    private RoomRenderer roomImage;
    private Player player;

    private InfoTooltip tooltip;
    private JPopupMenu actionMenu;
    private Drawable activeObject;

    /**
     * Construct a new game canvas
     *
     * @param loader a ResourceManager which will be used to locate and read images, sprites, etc.
     * @param gameActionHandler an object to use as the parent of the GUI action handler
     */
    public GameCanvas(ResourceManager loader, ActionHandler gameActionHandler) {
        this.loader = loader;
        this.actionHandler = new CanvasActionHandler(gameActionHandler);

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
     * @param game the game the player is in; the renderer will be updated when the game state is changed
     */
    public void setup(Player player, Game game) {
        game.addStateChangeListener(this);
        this.player = player;

        if (player == null) {
            roomImage = null;
        }
        else {
            roomImage = new RoomRenderer(loader, player);
            repaint();
        }
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
        if (player == null || roomImage == null || (actionMenu != null && actionMenu.isVisible())) {
            return;
        }

        Point scenePosition = calculateScenePosition(e);
        Drawable drawable = roomImage.getObjectAt(scenePosition);
        activeObject = drawable;

        if (drawable != null) {
            Action action = (e.getButton() == MouseEvent.BUTTON1) ?
                    tooltip.getPrimaryAction() : tooltip.getSecondaryAction();
            actionHandler.requestAction(action);
        }
    }

    /**
     * Called whenever the game state changes
     */
    @Override
    public void onStateChanged(Player player, Type type, String message) {
        // Hide the tooltip (stops item tooltips from hanging around after the item has been picked up etc.)
        if (this.player.equals(player)) {
            tooltip.setVisible(false);
        }

        // Play monster sounds if available
        if (type == Type.MOVE && player.getRoom().getMonster() != null) {
            Monster monster = (Monster)player.getRoom().getMonster().getItem();
            loader.playSoundEffect(monster.getSoundEffect());
        }

        // Redraw the scene
        update();
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
     * Repaint the canvas based on the current player
     */
    private void update() {
        if (roomImage != null) {
            roomImage.updateRoom();
            repaint();
        }
    }

    /**
     * Show a tooltip with a description of the object from the given action
     */
    private void showDescription(GUIActions.Examine action) {
        updateTooltip(action.target);

        if (action.target instanceof ItemInstance) {
            tooltip.showDescription(((ItemInstance) action.target).getItem().getDescription());
        }
        else if (action.target instanceof Door) {
            Room room = player.getRoom().getConnection(((Door)action.target).getLinkDirection());
            tooltip.showDescription("This leads to: " + room.getName());
        }

        repositionTooltipAbove(action.target);
        tooltip.setVisible(true);
    }

    /**
     * Show a context menu listing the actions from the given action
     */
    private void showActionMenu(GUIActions.ShowMenu action) {
        tooltip.setVisible(false);

        actionMenu = new ActionMenu(actionHandler, action.actions);
        Point position = positionAboveObject(action.target, actionMenu);
        actionMenu.show(this, position.x, position.y);
    }

    /**
     * Show a menu listing the items inside the given container
     */
    private void showSearchMenu(GUIActions.Search action) {
        actionMenu = new ContentsMenu(loader, actionHandler, action.containerInstance, player, "Click to pick up");
        Point position = positionAboveObject(action.containerInstance, actionMenu);
        actionMenu.show(this, position.x, position.y);
    }

    /**
     * Display a tooltip above the given drawable object with information about the object
     */
    private void updateTooltip(Drawable drawable) {
        activeObject = drawable;
        List<Action> actions = actionHandler.getAllowedActions(player, drawable);

        if (actions.size() > 1) {
            tooltip.showObject(drawable.getName(), actions.get(0), actions.get(1));
        }
        else if (actions.size() > 0) {
            tooltip.showObject(drawable.getName(), actions.get(0), null);
        }
        else {
            tooltip.showObject(drawable.getName(), null, null);
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
            Point result = new Point(x + roomImagePosition.x - size.width / 2, y + roomImagePosition.y - size.height);

            // Check the object is on screen
            if (result.x < 0) {
                result.x = 0;
            }
            else if (result.x + size.width > getWidth()) {
                result.x = getWidth() - size.width;
            }
            if (result.y < 0) {
                result.y = 0;
            }
            else if (result.y + size.height > getHeight()) {
                result.y = getHeight() - size.height;
            }
            return result;
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
