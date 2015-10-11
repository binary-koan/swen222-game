package game;

public enum Action {
    EXAMINE("Examine"), PICK_UP("Pick up"), SEARCH("Search"), SHOW_MENU("Other ..."), TAKE("Take"), GO_THROUGH("Enter");

    private final String text;

    /**
	 * Create a new action
	 *
	 * @param text string representation of the action
	 */
    Action(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
*/
    @Override
    public String toString() {
        return text;
    }
}
