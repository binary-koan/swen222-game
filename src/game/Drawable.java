package game;

public interface Drawable {
    public class Point3D {
        public final int x;
        public final int y;
        public final int z;

        public Point3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    Direction getFacingDirection();
    Point3D getPosition();
    String getSpriteName();
}
