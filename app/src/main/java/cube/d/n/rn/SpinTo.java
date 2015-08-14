package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.util.Log;

/**
 * Created by Colin_000 on 4/20/2015.
 */
public class SpinTo extends BitmapBacked {
    final float  radius = 25f;
    private Vector myVector;

    Vector v1;
    Vector v2;

    public final Index startAt;
    public final int dim1;
    public final int dim2;
    public final int clicks;
    public final boolean flip;

    public SpinTo(Brick form, Index plane, int clicks,Tess owner){
        super(owner);
        //calculate vector
        myVector = new Vector(form.getVector());
        v1 = getVector(plane, 0);
        v2 = getVector(plane, 1);
        if (clicks ==1){
            myVector.add(v1.scale(1f,true),false);
            myVector.add(v2.scale(.15f,true),false);
        }else if (clicks ==2){
            myVector.add(v1.scale(1f,true),false);
            myVector.add(v2.scale(1f,true),false);
        }else if (clicks ==-1){
            myVector.add(v1.scale(.15f,true),false);
            myVector.add(v2.scale(1f,true),false);
        }

        startAt = form.getIndex();
        dim1 = getDim(plane, 0);
        dim2 = getDim(plane, 1);
        // I don't totally understand why I need flip
        flip = !plane.get(dim1).equals(plane.get(dim2));
        this.clicks =clicks;
    }

    public static int getDim(Index plane,int skip){
        int skipped = 0;
        for (int i =0;i<plane.size();i++){
            Integer integer = plane.get(i);
            if (integer!=0){
                if (skipped < skip){
                    skipped++;
                }else{
                    return i;
                }
            }
        }
        return -1;
    }

    private Vector getVector(Index plane,int skip) {
        int dim =getDim(plane,skip);
        Integer integer = plane.get(dim);
        return ((Tess)owner).vectorForDimension(dim).scale(integer,false);
    }


    public boolean in(Vector vector) {
        return distance(vector)<radius;
    }

    public void go(){
        //spin the cube
        Log.i("spin","this:"+this);
        ((Tess)owner).rotate(startAt,dim1,dim2,flip,clicks);


    }

    public float distance(Vector vector) {
        return myVector.distance(vector);
    }

    public String toString(){
        return myVector+"";
    }

    private long startedAt = System.currentTimeMillis();
    public void draw(Canvas canvas) {
        Paint p = new Paint();
        long now =System.currentTimeMillis();
        long timePassed=Math.min(RN.rn().fadeTime(),now - startedAt);
        p.setAlpha((int)((timePassed/(float)RN.rn().fadeTime())*0xff));
        drawBitmap(canvas,myVector.x-radius,myVector.y-radius,p);
    }

    @Override
    protected Bitmap updateBitmap() {

        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording((int)(2*radius), (int)(2*radius));

        Paint p1 = new Paint();
        p1.setColor(0xffffffff);
        canvas.drawCircle(radius,radius,radius,p1);

        Paint p2 = new Paint();
        p2.setColor(0xff444444);
        p2.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(radius,radius,radius,p2);

        Vector myV1 = v1.toUnit(true).scale(radius,false);
        Vector myV2 = v2.toUnit(true).scale(radius,false);

        float startX;
        float startY;

        if (clicks == -1){
            myV1.scale(-1,false);
            myV2.scale(-1,false);
        }else if (clicks ==2){
            myV2.scale(-1,false);
        }

        float totalX = myV1.x + myV2.x;

        if (Math.abs(totalX)< Math.abs(myV1.x)){
            totalX = myV1.x;
        }
        if (Math.abs(totalX)< Math.abs(myV2.x)){
            totalX = myV2.x;
        }
        float totalY = myV1.y + myV2.y;

        if (Math.abs(totalY)< Math.abs(myV1.y)){
            totalY = myV1.y;
        }
        if (Math.abs(totalY)< Math.abs(myV2.y)){
            totalY = myV2.y;
        }
        startX = totalX/2f  - (Math.signum(myV1.x)  !=Math.signum(totalX) ?myV1.x:0);
        startY = totalY/2f  - (Math.signum(myV1.y)  !=Math.signum(totalY) ?myV1.y:0);

        canvas.drawLine(radius - (startX),
                radius- (startY),
                radius - (startX)+myV1.x,
                radius - (startY)+myV1.y,p2);
        p2.setColor(0xffff0000);
        canvas.drawLine(radius - (startX)+myV1.x,
                radius- (startY)+myV1.y,
                radius - (startX)+myV1.x + myV2.x,
                radius - (startY)+myV1.y + myV2.y,p2);

        picture.endRecording();

        return Util.pictureDrawable2Bitmap(picture);
    }
}
