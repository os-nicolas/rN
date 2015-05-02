package cube.d.n.rn;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Colin_000 on 4/23/2015.
 */
public class Button extends BitmapBacked {
    //TODO we need to know when the button is not being touched so we can shrink it back down
    // this mean set a time out and on each change and after the timeout set the target to normal if nothing has happened


    float edgeBit = 0;
    private final float BaseBuffer = 12f * RN.rn().scale();
    float targetBuffer = BaseBuffer;
    float currentBuffer = BaseBuffer;

    public GS<String> txt =new GS<String>(""){
        @Override
        public void set(String newValue){
            super.set(newValue);
            myInvalidate();
        }
    };
    Vector topLeft;
    float width;
    float height;
    public GS<Action> action = new GS<>();


    public Button(float left, float top, float right, float bot, String txt, View owner) {
        super(owner);
        width = right - left;
        height = bot - top;
        topLeft = new Vector(top, left);
        this.txt.set(txt);
    }

    public Button(float left, float top, float right, float bot, String txt, View owner,Action action) {
        this(left, top, right, bot, txt,owner);
        this.action.set(action);
    }

    public void draw(Canvas canvas) {
        drawBitmap(canvas, topLeft.x, topLeft.y, new Paint());
    }

    public void hover() {
        //if (action.get() != null && action.get().canAct()) {
        //    setTargetBuffer(6f);

        Log.i("Button","hover - " + currentBuffer );
        //}
    }

    private void setCurrentBuffer(float cb) {
        currentBuffer=cb*RN.rn().scale();
        myInvalidate();
    }

//    private long lastChange = System.currentTimeMillis();
    private void setTargetBuffer(float tb) {
        targetBuffer=tb*RN.rn().scale();
        myInvalidate();
//        lastChange = System.currentTimeMillis();
//        final long  myChange = lastChange;
//        final Activity activity = (Activity) owner.getContext();
//        Thread th = new Thread(){
//            @Override
//            public void run(){
//                try{
//                    Thread.sleep(300);
//                    if (myChange == lastChange){
//                        targetBuffer=BaseBuffer;
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                myInvalidate();
//                            }
//                        });
//                    }
//                }catch(InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
//        };
//        th.start();

    }

    public void click(){
        //if (action.get() != null && action.get().canAct()) {
        setCurrentBuffer(2f);
//        setTargetBuffer(2f);
//            action.get().act();

        Log.i("Button","click - " + currentBuffer );
        //}
    }

    @Override
    protected Bitmap updateBitmap(){
        currentBuffer = ((RN.rn().rate()-1f)* currentBuffer + targetBuffer)/RN.rn().rate();
        if (Math.abs(currentBuffer - targetBuffer) < .02){
            currentBuffer = targetBuffer;
        }else{
            myInvalidate();
        }

        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording((int)width, (int)height);

        ArrayList<Vector> vectors = new ArrayList<>();
        // draw a hexagon
        // generating the lines takes a bit of work
        for (float i=0;i<3;i++){
            double angle= Math.PI*(i+.5)/(3);
            Vector v = new Vector( (float)Math.sin(angle), (float)Math.cos(angle));
            vectors.add(v);
        }
        float xSum = 0;
        float ySum =0;
        for (Vector v: vectors){
            xSum+=Math.abs(v.x);
            ySum+=Math.abs(v.y);
        }

        // first we do the side bros
        for (Vector v: vectors){
            if (Math.abs(v.y) >.1) {
                v.x*= (-2*currentBuffer+height)/ySum;
                edgeBit = v.x; // should be the same for all of these
                v.y*= (-2*currentBuffer+height)/ySum;
            }

        }

        for (Vector v: vectors){
            if (Math.abs(v.y) <.1) {
                v.x = (width-2 * currentBuffer  - 2*edgeBit );
            }
        }

        Paint p = new Paint();
        p.setColor(RN.rn().getDarkColor());
        p.setStrokeWidth(RN.rn().getStrokeWidth());

        Vector at = new Vector(currentBuffer,height/2f);
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
        tp.setTextSize(45);
        Rect out = new Rect();
        tp.getTextBounds(txt.get(), 0, txt.get().length(), out);
        while (out.width() + (2 * currentBuffer) > width || out.height() + (2 * currentBuffer) > height) {
            tp.setTextSize(tp.getTextSize() - 0.1f);
            tp.getTextBounds(txt.get(), 0, txt.get().length(), out);
        }

        canvas.drawText(txt.get(),(width/2f) - (out.width()/2f),(height/2f) + (out.height()/2f),tp);

        picture.endRecording();

        return Util.pictureDrawable2Bitmap(picture);
    }

    public boolean in(Vector at){
        // we model the body as a square
        if ((at.y > topLeft.y && at.y < topLeft.y + height ) &&
                (at.x > topLeft.x + edgeBit && at.x < topLeft.x + width - edgeBit)){
            return true;
        }
        // we model the ends as circles
        Vector leftEnd = new Vector(topLeft.x+edgeBit,edgeBit);
        if (leftEnd.distance(at)<edgeBit ){
            return true;
        }
        Vector rightEnd = new Vector(topLeft.x+width-edgeBit,edgeBit);
        if (rightEnd.distance(at)<edgeBit ){
            return true;
        }
        return false;
    }
}