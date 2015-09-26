package client;

import game.Game;
import game.Item;
import game.Player;
import game.Room;

public interface NetworkListener {
    void onPlayerLoaded(Player player);
    void onPlayerMoved(Player player);
    void onItemRemoved(Room room, Item item);
    void onItemAdded(Room room, Item item);
}
