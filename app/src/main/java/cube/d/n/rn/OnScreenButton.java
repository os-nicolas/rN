package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.util.Log;

/**
 * Created by Colin_000 on 5/22/2015.
 */
public abstract  class OnScreenButton extends BitmapBacked {

    final float  radius = 25f;
    final Action action;
    final Vector myVector;

    public OnScreenButton(Tess owner,Action action,Vector myVector){
        super(owner);
        this.action = action;
        this.myVector = myVector;
    }

    public boolean in(Vector vector) {
        return distance(vector)<radius;
    }

    public void go(){
        if (action.canAct()) {
            action.act();
        }
    }

    public float distance(Vector vector) {
        return myVector.distance(vector);
    }

    private long startedAt = System.currentTimeMillis();
    public void draw(Canvas canvas) {
        Paint p = new Paint();
        long now =System.currentTimeMillis();
        long timePassed=Math.min(RN.rn().fadeTime(),now - startedAt);
        p.setAlpha((int)((timePassed/(float)RN.rn().fadeTime())*0xff));
        drawBitmap(canvas,myVector.x-radius,myVector.y-radius,p);
    }


    protected Canvas drawBkg(Canvas canvas) {
        Paint p1 = new Paint();
        p1.setColor(0xffffffff);
        canvas.drawCircle(radius,radius,radius,p1);

        Paint p2 = new Paint();
        p2.setColor(0xff444444);
        p2.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(radius,radius,radius,p2);

        return canvas;
    }
}
