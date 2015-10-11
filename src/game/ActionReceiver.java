package game;

public interface ActionReceiver {
    /**
     * Handle a specific action being sent to an item
     *
     * @param item the item being activated
     * @param action the action taken
     */
    void performAction(Item item, Item.Action action);

    /**
     * Handle an action being sent to an item in a container
     *
     * @param container the container being accessed
     * @param item the item being activated
     * @param action the action taken
     */
    void performAction(Container container, Item item, Item.Action action);
}
