package cube.d.n.rn;

import android.view.MotionEvent;

/**
 * Created by Colin_000 on 4/17/2015.
 */
public class Vector {

    float x;
    float y;

    public Vector(Vector v){
        this.x =v.x;
        this.y = v.y;
    }

    public Vector(float x, float y){
        this.x =x;
        this.y = y;
    }

    public Vector(MotionEvent event) {
        this.x =event.getX();
        this.y = event.getY();
    }

    public Vector() {
        x=0;
        y=0;
    }

    public Vector scale(float s,boolean asNew) {
        Vector target = (asNew?new Vector(this):this);
        target.x*=s;
        target.y*=s;

        return target;
    }

    public Vector add(Vector v,boolean asNew) {
        Vector target = (asNew?new Vector(this):this);
        target.x += v.x;
        target.y += v.y;
        return target;
    }

    public  String toString(){
        return x +"," + y;

    }

    public boolean noneZero() {
        return x!= 0 || y != 0;
    }

    public float distance(Vector vector) {
        float dx = x- vector.x;
        float dy = y- vector.y;

        return (float) Math.sqrt(dx*dx + dy*dy);
    }

    public Vector toUnit(boolean asNew) {
        float scaleBy = 1f/(float) Math.sqrt(x*x + y*y);
        return scale(scaleBy,asNew);
    }

}
