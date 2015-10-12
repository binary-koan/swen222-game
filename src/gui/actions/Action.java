package gui.actions;

import game.Player;

public abstract class Action {
    public final Player player;
    public final String name;

    public Action(Player player, String name) {
        this.player = player;
        this.name = name;
    }
}
