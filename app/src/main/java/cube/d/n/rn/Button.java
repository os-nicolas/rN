package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Colin_000 on 4/23/2015.
 */
public abstract class Button extends BitmapBacked{

    String txt;
    Vector topLeft;
    float width;
    float height;

    public Button(float left, float top, float right, float bot, String txt){
        width = right- left;
        height = bot - top;
        topLeft = new Vector(top,left);
        this.txt = txt;
    }

    public void draw(Canvas canvas) {
        drawBitmap(canvas, topLeft.x, topLeft.y, new Paint());
    }

    public void hover(){
        invalidate();
    }

    public void click(){
        invalidate();
    }

    public abstract void act();

    protected Bitmap updateBitmap(){
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording((int)width, (int)height);

        ArrayList<Vector> vectors = new ArrayList<>();
        // draw a hexagon
        // generating the lines takes a bit of work
        for (float i=0;i<3;i++){
            double angle= Math.PI*(i+.5)/(3)  - Math.PI/2f;
            Vector v = new Vector( (float)Math.sin(angle), (float)Math.cos(angle));
            Log.i("adding vector:", v + "");
            vectors.add(v);
        }
        float xSum = 0;
        float ySum =0;
        for (Vector v: vectors){
            xSum+=Math.abs(v.x);
            ySum+=Math.abs(v.y);
        }

        float buffer =2f;
        for (Vector v: vectors){
            v.x*= (-2*buffer+width)/xSum;
            v.y*= (-2*buffer+height)/ySum;
        }

        Paint p = new Paint();
        p.setStrokeWidth(3);

        Vector at = new Vector(width/2f,buffer);
        for (Vector v: vectors){
            Vector old = new Vector(at);
            Util.drawLine(canvas,old,at.add(v,false),p);
        }
        for (Vector v: vectors){
            Vector old = new Vector(at);
            Util.drawLine(canvas,old,at.add(v.scale(-1,true),false),p);
        }

        //now we need to draw the text centered
        TextPaint tp = new TextPaint();
        Rect out = new Rect();
        tp.getTextBounds(txt, 0, txt.length(), out);
        while (out.width() + 2 * buffer > width || out.height() + 2 * buffer > height) {
            tp.setTextSize(tp.getTextSize() - 1);
            tp.getTextBounds(txt, 0, txt.length(), out);
        }

        canvas.drawText(txt,(width/2f) - (out.width()/2f),(height/2f) - (out.height()/2f),tp);

        picture.endRecording();

        return Util.pictureDrawable2Bitmap(picture);
    }

    public boolean in(Vector at){
        //TODO
        // we modle as a ???
        return false;

    }
}
