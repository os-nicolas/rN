package cube.d.n.rn;

import android.app.Application;

/**
 * Created by Colin_000 on 4/22/2015.
 */
public class RN extends Application {
    private static RN instance = new RN();
    private RN(){}
    public static RN rn(){
        return instance;
    }
//    public int getMaxSize(){
//        return 5;
//    }

    /**
     * @return milliseconds
     */
    public int fadeTime() {
        return 500;
    }

    public float rate() {
        return 5;
    }

    public float scale() {
        return 1;
    }

    public int getDarkColor() {
        return 0xff888888;
    }

    public float getStrokeWidth() {
        return 3f;
    }
}
