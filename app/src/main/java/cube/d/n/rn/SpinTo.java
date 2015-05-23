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

    public final Brick from;
    public final FaceIndex plane;

    public SpinTo(Brick form, FaceIndex plane, int clicks,Tess owner){
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

        this.from = form;
        this.plane = plane;

        startAt = form.getIndex();
        dim1 = getDim(plane, 0);
        dim2 = getDim(plane, 1);
        // I don't totally understand why I need flip
        flip = !plane.get(dim1).equals(plane.get(dim2));
        this.clicks =clicks;
    }

    private  int getDim(FaceIndex plane,int skip){
        int skipped = 0;
        for (int i =0;i<plane.size();i++){;
            if (plane.get(i)!= FaceIndex.FaceValue.NONE){
                if (skipped < skip){
                    skipped++;
                }else{
                    return i;
                }
            }
        }
        return -1;
    }

    private Vector getVector(FaceIndex plane,int skip) {
        int dim =getDim(plane,skip);
        FaceIndex.FaceValue fv = plane.get(dim);
        Vector v = ((Tess)owner).vectorForDimension(dim);
        if (fv == FaceIndex.FaceValue.NONE){
            return v.scale(0,true);
        }
        if (fv == FaceIndex.FaceValue.FORWARD){
            return v.scale(1,true);
        }
        if (fv == FaceIndex.FaceValue.BACK){
            return v.scale(-1,true);
        }
        if (fv == FaceIndex.FaceValue.EVEN){
            return v.scale(1,true);
        }

        Log.e("getVector","you must have changed the enum values in FaceValue");
        return new Vector();


    }


    public boolean in(Vector vector) {
        return distance(vector)<radius;
    }

    public void go(){
        //spin the cube
        Log.i("spin","this:"+this);
        if (clicks ==1) {
            ((Tess)owner).rotate(startAt, dim1, dim2, !flip);
        }else if (clicks ==2){
            ((Tess)owner).rotate(startAt, dim1, dim2, !flip);
            ((Tess)owner).rotate(startAt, dim1, dim2, !flip);
        }else if (clicks == -1){
            ((Tess)owner).rotate(startAt, dim1, dim2, flip);
        }
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
    protected Bitmap updateBitmap(Bitmap old) {

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


        // this looks crazy
        // but it's really not
        // the center is the center of the two lines
        // center of the fist line is (myV1 + 0)/2
        // center of the second line is ((myV2 + myV1) + myV1)/2
        // and when we average those we get the below
        float centerX = (myV1.x +myV1.x + myV1.x + myV2.x)/4f;
        float centerY = (myV1.y +myV1.y + myV1.y + myV2.y)/4f;


        startX = radius- centerX;
        startY = radius- centerY;

        canvas.drawLine( (startX),
                (startY),
                 (startX)+myV1.x,
                 (startY)+myV1.y,p2);
        p2.setColor(0xffff0000);
        canvas.drawLine( (startX)+myV1.x,
                (startY)+myV1.y,
                (startX)+myV1.x + myV2.x,
                (startY)+myV1.y + myV2.y,p2);

        picture.endRecording();

        return Util.pictureDrawable2Bitmap(picture,old);
    }
}
