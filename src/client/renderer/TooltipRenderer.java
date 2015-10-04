package client.renderer;

import game.Item;
import game.Room;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TooltipRenderer {
	private static Font NAME_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);

	public static BufferedImage createForItem(Item item) {
		int width = Room.ROOM_SIZE / 4;
		int height = Room.CEILING_HEIGHT / 5;

		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = result.createGraphics();
		graphics.fillRoundRect(0, 0, width, height, 2, 2);

		return result;
	}
}
