package gui.actions;

public class GUIAction extends Action {
    public enum Type {
        EXAMINE, SEARCH, SHOW_MENU;

        @Override
        public String toString() {
            // Nicely capitalize the name of the enum constant
            String result = super.toString().replace('_', ' ').toLowerCase();
            return result.substring(0, 1).toUpperCase() + result.substring(1);
        }
    }

    private Type type;

    public GUIAction(Type type) {
        super(type.toString());
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
