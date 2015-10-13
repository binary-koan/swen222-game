package game;

import org.jdom2.Element;

public class Monster extends Item {
	private String soundEffect;
	private String killMessage;
	private Weapon deadlyWeapon;

	public Monster(String id, String name, String description, String spriteName, Weapon deadlyWeapon) {
		super(id, name, description, spriteName);
		this.deadlyWeapon = deadlyWeapon;
	}

	public String getSoundEffect() {
		return soundEffect;
	}

	@Override
	public Element toXML() {
		return super.toXML();
	}

	@Override
	public void loadXML(Game game, Element objectElement) {
		super.loadXML(game, objectElement);

		soundEffect = objectElement.getChildText("soundEffect");
		killMessage = objectElement.getChildText("killMessage");
	}

	public boolean fight(Player player) {
		// Sanity check to make sure the player and monster are in the same room
		Room.ItemInstance instance = player.getRoom().getMonster();
		if (!equals(instance.getItem())) {
			return false;
		}

		if (player.getHeldItem().equals(deadlyWeapon)) {
			player.getRoom().removeItem(this);
			return true;
		}
		else {
			player.kill(killMessage);
			return false;
		}
	}
}
