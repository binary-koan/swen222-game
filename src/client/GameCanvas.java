package client;

import client.renderer.ResourceLoader;
import client.renderer.RoomRenderer;
import client.renderer.TooltipRenderer;
import game.*;
import game.Room.ItemInstance;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class GameCanvas extends JPanel implements MouseListener, MouseMotionListener {
    private @NonNull ResourceLoader loader;

    private double roomImageScale;
    private @Nullable Point roomImagePosition;
    private @Nullable RoomRenderer roomImage;

    private @Nullable Player player;

    private BufferedImage currentTooltip;
    private Drawable activeObject;

    public GameCanvas(@NonNull ResourceLoader loader) {
        this.loader = loader;
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

        if (currentTooltip != null) {
        	Rectangle itemPosition = roomImage.getBounds(activeObject);

        	if (itemPosition != null) {
	        	int width = (int) (currentTooltip.getWidth() * roomImageScale * 5);
	        	int height = (int) (currentTooltip.getHeight() * roomImageScale * 5);

	        	int x = (int) ((itemPosition.x + itemPosition.width / 2) * roomImageScale * 5);
	        	int y = (int) ((itemPosition.y - 5) * roomImageScale * 5);

	        	Image scaled = currentTooltip.getScaledInstance(width, height, Image.SCALE_FAST);
	        	g.drawImage(scaled, x - width / 2, y - width / 2, width, height, null, null);
        	}
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
        Point scenePosition = calculateScenePosition(e);
        Drawable drawable = roomImage.getObjectAt(scenePosition);

        // Check if a tooltip needs to be drawn/updated
        if (drawable == null) {
        	activeObject = null;
        	currentTooltip = null;
        	return;
        }
        else if (drawable.equals(activeObject)) {
        	return;
        }

        // Create a new tooltip for the hovered object
        activeObject = drawable;
        if (drawable instanceof ItemInstance) {
        	Item item = ((ItemInstance)drawable).getItem();
        	currentTooltip = TooltipRenderer.createForItem(item);
        	repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    	Point scenePosition = calculateScenePosition(e);
        Drawable drawable = roomImage.getObjectAt(scenePosition);

//        if (drawable instanceof ItemInstance) {
//        	activeItem = ((ItemInstance)drawable).getItem();
//        	repaint();
//        }
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
