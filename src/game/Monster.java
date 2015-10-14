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

	public void setSoundEffect(String soundEffect){
		this.soundEffect = soundEffect;
	}

	public String getSoundEffect() {
		return soundEffect;
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

	@Override
	public Element toXML() {
		Element monster = super.toXML();
		monster.addContent(new Element("deadlyWeapon").setText(this.deadlyWeapon.getID()));
		monster.addContent(new Element("soundEffect").setText(this.soundEffect));
		monster.addContent(new Element("killMessage").setText(this.killMessage));
		return monster;
	}

	@Override
	public void loadXML(Game game, Element objectElement) {
		super.loadXML(game, objectElement);
		this.deadlyWeapon = (Weapon) game.getItem(objectElement.getChildText("deadlyWeapon"));
		this.soundEffect = objectElement.getChildText("soundEffect");
		this.killMessage = objectElement.getChildText("killMessage");
	}
}
