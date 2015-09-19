package client.renderer;

import client.NetworkListener;
import client.NetworkNotifier;
import game.Game;
import game.Item;
import game.Player;
import game.Room;

import javax.swing.*;

public class GameCanvas extends JPanel implements NetworkListener {
    private Game game;
    private String playerName;
    private Player player;
    private RoomRenderer roomImage;

    public GameCanvas(String playerName, NetworkNotifier notifier) {
        this.playerName = playerName;
        notifier.addChangeListener(this);
    }

    @Override
    public void onGameLoaded(Game game) {
        player = game.getPlayer(playerName);
        roomImage = new RoomRenderer(game, player);
    }

    @Override
    public void onPlayerMoved(Player player) {
    }

    @Override
    public void onItemRemoved(Room room, Item item) {
    }

    @Override
    public void onItemAdded(Room room, Item item) {
    }
}
