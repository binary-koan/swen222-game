package game;

import storage.Serializable;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

/**
 * Represents a player's avatar in the game
 */
public class Player implements Drawable, Serializable {
    private List<StateChangeListener> stateChangeListeners = new ArrayList<>();
    private String name;
	private String spriteName;
    private Room room;
    private Direction facingDirection = Direction.NORTH;
    private Holdable heldItem = null;
    private boolean isAlive = true;

    /**
     * Create a new player
     *
     * @param name player name
     * @param spriteName sprite used to render the player
     * @param room room the player is initially in
     */
    public Player(String name, String spriteName, Room room) {
    	this.name = name;
    	this.spriteName = spriteName;
        this.room = room;

        room.addPlayer(this);
    }

    public void addStateChangeListener(StateChangeListener listener) {
        stateChangeListeners.add(listener);
    }

    // Getters

    /**
     * @return the name of the player
     */
    @Override
    public String getName() {
    	return name;
    }

    /**
     * @return the room that the player is in
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @return the direction the player is facing
     */
    public Direction getFacingDirection() {
        return facingDirection;
    }

    /**
     * @return the item the player is holding
     */
    public Holdable getHeldItem() {
        return heldItem;
    }

    public void winGame(){
    	triggerStateChange(StateChangeListener.Type.WIN, "Not only did you save your planet, you stopped a rogueAI "
    			+ "busting up your ship. Dave Bowman ain't got nothing on you");
    }

    /**
     * @return a point near the center of the wall the player is viewing their room from
     */
    @Override
    public Point3D getPosition() {
        switch (this.facingDirection) {
            case NORTH:
                return new Point3D(160, 0, 40);
            case SOUTH:
                return new Point3D(160, 0, 280);
            case EAST:
                return new Point3D(280, 0, 160);
            case WEST:
            default:
                return new Point3D(40, 0, 160);
        }
    }

    @Override
    public String getSpriteName() {
        return spriteName;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void kill(String message) {
        isAlive = false;
        triggerStateChange(StateChangeListener.Type.DIE, message);
    }

    /**
     * Set the player's facing direction
     *
     * @param facingDirection new direction the player should face
     */
    public boolean turn(Direction facingDirection) {
        if (isAlive) {
            this.facingDirection = facingDirection;
            triggerStateChange(StateChangeListener.Type.TURN, null);
            return true;
        }
        return false;
    }

    /**
     * Move to the adjacent room in the given direction
     *
     * @param movementDirection direction to move in
     * @return true if the move succeeded, false otherwise
     */
	public boolean move(Direction movementDirection) {
        if (isAlive) {
            Room newRoom = room.getConnection(movementDirection);

            if (newRoom == null) {
                return false;
            }
            else if (newRoom.allowsEntry(this, movementDirection)) {
                room.removePlayer(this);
                newRoom.addPlayer(this);
                room = newRoom;
                triggerStateChange(StateChangeListener.Type.MOVE, null);
                return true;
            }
        }
        return false;
	}

    /**
     * Set the item the player is holding
     *
     * @return true if the item was picked up, false otherwise (eg. if the player was already holding an item)
     */
    public boolean pickUp(Holdable item, Container container) {
        if (isAlive) {
            if (item == null || heldItem != null) {
                return false;
            }
            else {
                heldItem = item;
                if (container == null) {
                    room.removeItem(item);
                }
                else {
                    container.getItems().remove(item);
                }

                triggerStateChange(StateChangeListener.Type.PICK_UP, null);
                return true;
            }
        }
        return false;
    }

    /**
     * Unsets the player's held item and adds it to the current room
     *
     * @return the dropped item
     * @param target
     */
    public Holdable dropItem(Container target) {
        if (isAlive) {
            if (heldItem == null) {
                return null;
            }
            else {
                Holdable item = heldItem;

                if (target == null) {
                    getRoom().addItem(item);
                }
                else {
                    target.getItems().add(item);
                }

                heldItem = null;
                triggerStateChange(StateChangeListener.Type.DROP, null);
                return item;
            }
        }
        return null;
    }

    /**
     * The main state change trigger for actions performed by the player.
     * @param type StateChangeListener action, eg MOVE, DROP.
     * @param message The message displayed to the screen.
     */
    public void triggerStateChange(StateChangeListener.Type type, String message) {
        for (StateChangeListener listener : stateChangeListeners) {
            listener.onStateChanged(this, type, message);
        }
    }

    // Serialization

    /**
	 * {@inheritDoc}
	 * @author holdawscot
	 */
	@Override
	public Element toXML() {
		Element player = new Element("player");
		player.addContent(new Element("name").setText(this.name));
		player.addContent(new Element("spriteName").setText(this.spriteName));
		player.addContent(new Element("room").setText(this.room.getID()));
		player.addContent(new Element("facingDirection").setText(this.facingDirection.toString()));
		player.addContent(new Element("heldItem"));
		if(this.heldItem != null){
			player.getChild("heldItem").setText(heldItem.getID());
		}
		return player;
	}

	/**
	 * {@inheritDoc}
	 * @author holdawscot
	 */
	@Override
	public void loadXML(Game game, Element objectElement) {
		this.name = objectElement.getChildText("name");
		this.spriteName = objectElement.getChildText("spriteName");
		this.room = game.getRoom(objectElement.getChildText("room"));
		this.facingDirection = Direction.fromString(objectElement.getChildText("facingDirection"));
		if(game.getItem(objectElement.getChildText("heldItem")) != null){
			this.heldItem = (Holdable)game.getItem(objectElement.getChildText("heldItem"));
		}
	}
}
