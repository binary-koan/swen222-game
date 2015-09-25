package game;

import client.renderer.Drawable;
import game.Player.Position;
import java.awt.*;

public class Item implements Drawable {
	private String name;

	public Item(String name){
		this.name = name;
	}

	@Override
	public BoundingCube getBoundingCube() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getSprite(Position position) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

}
