package game;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    public Direction opposite() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case WEST:
                return EAST;
            case SOUTH:
            default:
                return NORTH;
        }
    }

    public Direction viewFrom(Direction direction) {
        switch (direction) {
            case NORTH:
                return swapLinear(EAST, WEST);
            case EAST:
                return swapDiagonal(EAST, NORTH);
            case WEST:
                return swapDiagonal(WEST, NORTH);
            case SOUTH:
            default:
                return swapLinear(NORTH, SOUTH);
        }
    }

    private Direction swapLinear(Direction first, Direction second) {
        if (this == first) {
            return second;
        }
        else if (this == second) {
            return first;
        }
        return this;
    }

    private Direction swapDiagonal(Direction first, Direction second) {
        if (this == first) {
            return second;
        }
        else if (this == second) {
            return first;
        }
        else if (this == first.opposite()) {
            return second.opposite();
        }
        else {
            return first.opposite();
        }
    }

    public Direction previous() {
        Direction[] values = Direction.values();

        if (this == values[0]) {
            return values[values.length - 1];
        }
        else {
            return values[ordinal() - 1];
        }
    }

    public Direction next() {
        Direction[] values = Direction.values();

        if (this == values[values.length - 1]) {
            return values[0];
        }
        else {
            return values[ordinal() + 1];
        }
    }
}
