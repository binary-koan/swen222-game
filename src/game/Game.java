package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import storage.GameLoader;

public class Game implements StateChangeListener {
	private List<StateChangeListener> stateChangeListeners = new ArrayList<>();

	private Map<String, Item> items;
	private Map<String, Room> rooms;
	private Map<String, Player> players = new HashMap<>();
    private GameLoader loader;

    public Game(String filenameBase, String filenameGame){
    	this.loader = new GameLoader(this, filenameBase);
    	this.loader.setXMLFilename(filenameGame);
    	//Now we are shifting to read and write from a copied file,
    	//so the base file is not overwritten.
    	this.loader.saveWholeGame();
    }

    /**
     * Add a state change listener, which will be notified whenever the game state changes
     *
     * @param listener listener to add
     */
	public void addStateChangeListener(StateChangeListener listener) {
		stateChangeListeners.add(listener);
	}

    @Override
	public void onStateChanged(Player player, Type type, String message) {
        // This is called when individual objects change state, so propagate the change up to our listeners
		for (StateChangeListener listener : stateChangeListeners) {
			listener.onStateChanged(player, type, message);
		}
	}

    public void setItems(HashMap<String, Item> items){
		this.items = items;
	}

	public Map<String, Item> getItems(){
		return items;
	}

	public Item getItem(String id){
		for(Map.Entry<String, Item> item : items.entrySet()){
			if(item.getKey().equals(id)){
				return item.getValue();
			}
		}
		return null;
	}

    public void setRooms(HashMap<String, Room> rooms){
    	this.rooms = rooms;
    }

    public Map<String , Room> getRooms(){
		return rooms;
	}

	public Room getRoom(String id){
		for(Map.Entry<String, Room> room : rooms.entrySet()){
			if(room.getKey().equals(id)){
				return room.getValue();
			}
		}
		return null;
	}

    public void addPlayer(Player player) {
        this.players.put(player.getName(), player);

        // Listen for changes in individual player state, and use them to trigger a global state change
        player.addStateChangeListener(this);
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Player getPlayer(String playerName) {
        for(Map.Entry<String, Player> player : players.entrySet()){
        	if(player.getKey().equals(playerName)){
        		return player.getValue();
        	}
        }
        return null;
    }

    public GameLoader getData(){
    	return loader;
    }

}

