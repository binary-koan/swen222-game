package client;

import client.renderer.ActionMenu;
import client.renderer.ResourceLoader;
import client.renderer.RoomRenderer;
import client.renderer.Tooltip;
import game.*;
import game.Room.ItemInstance;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;

@SuppressWarnings("serial")
public class GameCanvas extends JPanel implements MouseListener, MouseMotionListener {
    private @NonNull ApplicationWindow parent;
    private @NonNull ResourceLoader loader;

    private double roomImageScale;
    private @Nullable Point roomImagePosition;
    private @Nullable RoomRenderer roomImage;

    private @Nullable Player player;

    private Tooltip tooltip;
    private ActionMenu openMenu;
    private Drawable activeObject;

    public GameCanvas(@NonNull ApplicationWindow parent, @NonNull ResourceLoader loader) {
        this.loader = loader;
        this.parent = parent;

        tooltip = new Tooltip(loader);
        tooltip.setVisible(false);
        add(tooltip);

        setLayout(null);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        if (roomImage != null) {
            BufferedImage image = roomImage.getCurrentImage();
            roomImageScale = Math.min((double)getWidth() / image.getWidth(), (double)getHeight() / image.getHeight());
            int width = (int) (image.getWidth() * roomImageScale);
            int height = (int) (image.getHeight() * roomImageScale);
            roomImagePosition = new Point((getWidth() - width) / 2, (getHeight() - height) / 2);

            Image scaled = image.getScaledInstance(width, height, Image.SCALE_FAST);
            g.drawImage(scaled, roomImagePosition.x, roomImagePosition.y, width, height, null, null);
        }

        if (player != null) {
            g.drawString("In " + player.getRoom().getName() + " facing " + player.getFacingDirection().opposite(), 10, 20);
        }
    }

    @Override
    public Dimension getPreferredSize() {
    	return new Dimension(Room.ROOM_SIZE * 3, Room.CEILING_HEIGHT * 3);
    }

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

    public void update() {
        if (roomImage != null) {
            roomImage.updateRoom();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (openMenu != null) {
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
        }
    }

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

        positionAboveObject(drawable, tooltip);

        tooltip.setVisible(true);
        repaint();
    }

    private void positionAboveObject(Drawable drawable, JComponent component) {
        Rectangle renderBounds = roomImage.getBounds(drawable);
        if (renderBounds != null) {
            Dimension size = component.getPreferredSize();

            int x = (int) ((renderBounds.x + renderBounds.width / 2) * roomImageScale * 5);
            int y = (int) ((renderBounds.y - 5) * roomImageScale * 5);
            component.setBounds(
                    x + roomImagePosition.x - size.width / 2,
                    y + roomImagePosition.y - size.height,
                    size.width, size.height
            );
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    	if (openMenu != null) {
            remove(openMenu);
            openMenu = null;
        }

        Point scenePosition = calculateScenePosition(e);
        Drawable drawable = roomImage.getObjectAt(scenePosition);
        activeObject = drawable;

        if (drawable != null) {
            Item.Action action = (e.getButton() == MouseEvent.BUTTON1) ?
                    tooltip.getPrimaryAction() : tooltip.getSecondaryAction();

            if (drawable instanceof ItemInstance) {
                handleItemAction((ItemInstance)drawable, action);
            }
        }
    }

    private void handleItemAction(ItemInstance drawable, Item.Action action) {
        Item item = drawable.getItem();

        if (action == null) {
            return;
        }
        else if (action == Item.Action.EXAMINE) {
            System.out.println("Examining");
            tooltip.showDescription(item.getDescription());
            positionAboveObject(drawable, tooltip);
        }
        else if (action == Item.Action.SHOW_MENU) {
            tooltip.setVisible(false);
            openMenu = new ActionMenu(item.getAllowedActions());
            add(openMenu);
            positionAboveObject(drawable, openMenu);
        }
        else {
            parent.handleAction(item, action);
        }
    }

    private Point calculateScenePosition(MouseEvent e) {
		return new Point(
    			(int)((e.getX() - roomImagePosition.x) / (roomImageScale * 5)),
    			(int)((e.getY() - roomImagePosition.y) / (roomImageScale * 5))
    	);
	}

    // Stub events

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}
