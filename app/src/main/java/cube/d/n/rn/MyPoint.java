package cube.d.n.rn;

/**
 * Created by Colin_000 on 4/4/2015.
 */
public class MyPoint {
    public float x = 0;
    public float y = 0;

    public MyPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public MyPoint() {
    }

    public MyPoint(MyPoint other) {
        this.x = other.x;
        this.y = other.y;
    }

    public static MyPoint ave(MyPoint a, MyPoint b, float p) {
        float nextX = a.x * p + b.x * (1 - p);
        float nextY = a.y * p + b.y * (1 - p);
        return new MyPoint(nextX, nextY);
    }
}
