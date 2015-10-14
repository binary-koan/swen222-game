package game;

public interface StateChangeListener {
    enum Type {
        TURN, MOVE, PICK_UP, TAKE, DROP, DIE, KILL_MONSTER, ATTACK, WIN
    }

    void onStateChanged(Player player, Type type, String message);
}
