package game;

public interface StateChangeListener {
    enum Type {
        TURN, MOVE, PICK_UP, TAKE, DROP, DIE, ATTACK
    }

    void onStateChanged(Player player, Type type, String message);
}
