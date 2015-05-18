package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by Colin_000 on 4/17/2015.
 */
public class Brick extends BitmapBacked {
    final public Tess owner;
    final float radius = 100f;
    final int dbColor;

    // bricks have a color for every pair of dimensions
    public HashMap<FaceIndex, Face> faces = new HashMap<>();


    public Brick(Index i, Tess owner) {
        super(owner);
        this.owner = owner;
        owner.addBrick(i, this);
        initFaces(new FaceIndex());

        Random r = new Random();
        dbColor = r.nextInt(0xffffff) + 0x88000000;
    }

    private void initFaces(FaceIndex at) {
        // for every pair of sides we need a face
        if (at.size() == owner.dim.get()) {
            FaceIndex faceIndex = new FaceIndex(at);
            Index ownerIndex = getIndex();

            // we zero out the place of face index in ownerIndex
            for (int i = 0; i < faceIndex.size(); i++) {
                if (FaceIndex.FaceValue.NONE != faceIndex.get(i)) {
                    ownerIndex.set(i, 0);
                }
            }

            // we zero out owner indexes that are not zero or size-1
            for (int i = 0; i < ownerIndex.size(); i++) {
                if (0 != ownerIndex.get(i) && owner.size.get() - 1 != ownerIndex.get(i)) {
                    ownerIndex.set(i, 0);
                    faceIndex.set(i, FaceIndex.FaceValue.EVEN);
                }
            }
            if (faceIndex.noneZeroComps() == 2) {
                faces.put(faceIndex, new Face(this, faceIndex, ownerIndex));
            }

        } else {
            if (at.noneZeroComps() < 2) {
                FaceIndex temp1 = new FaceIndex(at);
                if (getIndex().get(at.size()) == 0) {
                    temp1.add(FaceIndex.FaceValue.FORWARD);
                    initFaces(temp1);
                } else if (getIndex().get(at.size()) == owner.size.get() - 1) {
                    temp1.add(FaceIndex.FaceValue.BACK);
                    initFaces(temp1);
                }

            }
            // if the number of noneZeroComps we need add is less than the number of spots we have left
//            if (2-at.noneZeroComps() < owner.dim.get()-at.size()) {
            FaceIndex temp2 = new FaceIndex(at);
            temp2.add(FaceIndex.FaceValue.NONE);
            initFaces(temp2);
//            }
        }
    }

    public boolean isActive() {
        return this.equals(owner.active.get());
    }

    public String toString() {
        Index myIndex = getIndex();
        String result = "";
        for (Integer i : myIndex) {
            result += i + ",";
        }
        return result;
    }

    @Override
    public Bitmap updateBitmap() {
        return getBitmap(owner);
    }

    public void draw(Canvas canvas, int alpha) {
        if (!hasAnimation()) {
            Vector startAt = getIndex().getVector(owner);
            Paint p = new Paint();
            p.setAlpha(alpha);
            drawBitmap(canvas, startAt.x - radius, startAt.y - radius, p);
        }
    }

    public AnimatedBrick getAnimation() {
        for (Animation a : owner.animations) {
            if (a instanceof AnimatedBrick) {
                if (((AnimatedBrick) a).brick.equals(this)) {
                    return (AnimatedBrick) a;
                }
            }
        }
        return null;
    }

    public boolean hasAnimation() {
        return getAnimation() != null;
    }


    public Index getIndex() {
        return new Index(owner.indexOf(this));
    }

    public FaceIndex indexOf(Face face) {
        for (FaceIndex i : faces.keySet()) {
            Face mine = faces.get(i);
            if (mine.equals(face)) {
                return i;
            }
        }
        return null;
    }

    public void rotateFaces(int dim1, int dim2, boolean direction) {
        HashMap<FaceIndex, Face> toChange = new HashMap<>();

        for (FaceIndex index : faces.keySet()) {
                toChange.put(index, faces.get(index));
        }

        // we clear the hashMap
        faces = new HashMap<>();

        // now we rotate and put them back
        for (FaceIndex myIndex : toChange.keySet()) {
            Face myFace = toChange.get(myIndex);
            FaceIndex newIndex = new FaceIndex(myIndex);
            newIndex=newIndex.rotate(dim1,dim2,direction);
            faces.put(newIndex, myFace);
        }
        myInvalidate();
    }

    public boolean in(Vector vector) {
        return distance(vector) < radius;
    }

    public float distance(Vector vector) {
        return getVector().distance(vector);
    }

    public ArrayList<SpinTo> getSpinTos() {
        ArrayList<SpinTo> result = new ArrayList<>();
        // we need to itterate over the ways we can spin
        // that is for every pair of dimensions
        for (FaceIndex side : faces.keySet()) {
            // we can spin 1,2 or -1
            int[] spins = new int[]{1, 2, -1};
            for (int spin : spins) {
                result.add(new SpinTo(this, new FaceIndex(side), spin, owner));
            }

        }
        return result;
    }

    public Vector getVector() {
        return getIndex().getVector(owner);
    }

    public Bitmap getBitmap(HasVectorDims hasVectorDims) {
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording((int) (2 * radius), (int) (2 * radius));

        Vector startAt = new Vector(radius, radius);

        Paint grey = new Paint();
        grey.setColor(dbColor);
        //canvas.drawCircle(startAt.x,startAt.y,(isActive()?radius:radius/2f),grey);


        //float scale =.15f;
        for (Face f : faces.values()) {
            if (owner.drawFace(this, f)) {
                f.targetAlpha.set((float)0xff);
            }else{
                f.targetAlpha.set((float)0x00);
            }
            if (Math.abs(f.alpha.get() - f.targetAlpha.get())>.1) {
                float fast = (RN.rn().rate()/2f);
                f.alpha.set((f.alpha.get() *fast  + f.targetAlpha.get()) / (fast + 1));
                myInvalidate();
            }else{
                f.alpha.set(f.targetAlpha.get());
            }

                //TODO maybe faces should know how to draw themselves

                // we need to get the compent vectors
                ArrayList<FaceIndex.FaceValue> values = f.getIndex().getCompnetValues();
                ArrayList<Vector> comps = f.getIndex().getCompnetVectors(hasVectorDims);
                if (comps.size() < 2) {
                    Log.e("brick", "comps should be size 2");
                }

                for (Vector v : comps) {
                    v.toUnit(false).scale(radius / 2f, false);
                }

                Paint p = new Paint();
                p.setStrokeWidth(5);
                p.setColor(f.getColor());
                p.setAlpha((int) (f.alpha.get()+0));


                float percent = .5f;

                Vector far;
                Vector home;
                Vector left;
                Vector right;

                if (values.get(0) != FaceIndex.FaceValue.EVEN && values.get(1) != FaceIndex.FaceValue.EVEN) {
                    far=startAt.add(comps.get(0), true).add(comps.get(1), false);
                    home=startAt;
                    left=startAt.add(comps.get(0), true).add(comps.get(1).scale(percent, true),false);
                    right=startAt.add(comps.get(1), true).add(comps.get(0).scale(percent, true), false);
                }else if (values.get(0) == FaceIndex.FaceValue.EVEN && values.get(1) != FaceIndex.FaceValue.EVEN){
                    far=startAt.add(comps.get(1), true);
                    home=startAt;
                    left=startAt.add(comps.get(1), true).add(comps.get(0).scale(-percent, true), false);
                    right=startAt.add(comps.get(1), true).add(comps.get(0).scale(percent, true),false);
                }else if (values.get(0) != FaceIndex.FaceValue.EVEN && values.get(1) == FaceIndex.FaceValue.EVEN){
                    far=startAt.add(comps.get(0), true);
                    home=startAt;
                    left=startAt.add(comps.get(0), true).add(comps.get(1).scale(-percent, true), false);
                    right=startAt.add(comps.get(0), true).add(comps.get(1).scale(percent, true),false);
                }else{
                    far=startAt.add(comps.get(0).scale(percent, true), true).add(comps.get(1).scale(percent, true), false);
                    home=startAt.add(comps.get(0).scale(-percent, true), true).add(comps.get(1).scale(-percent, true), false);
                    left=startAt.add(comps.get(0).scale(-percent, true), true).add(comps.get(1).scale(percent, true), false);
                    right=startAt.add(comps.get(0).scale(percent, true), true).add(comps.get(1).scale(-percent, true),false);
                }

                Util.drawLine(canvas,
                        left,
                        far, p);
                Util.drawLine(canvas,
                        right,
                        far, p);
                Util.drawLine(canvas,
                        left,
                        home, p);
                Util.drawLine(canvas,
                        right,
                        home, p);

                //                float per = .1f;
//
//                Vector myStartAt = new Vector(startAt);
//                Index myIndex = getIndex();
//                for (int i=0;i<myIndex.size();i++){
//                    if (f.getIndex().get(i).equals(new Integer(0))){
//                        if (myIndex.get(i).equals(new Integer(0))){
//                            myStartAt.add(owner.vectorForDimension(i).scale(-per,false),false);
//                        }else if (myIndex.get(i).equals(new Integer(owner.size.get()-1))){
//                            myStartAt.add(owner.vectorForDimension(i).scale(per,false),false);
//                        }
//                    }
//
//                }
//
//                Util.drawLine(canvas,
//                        myStartAt,
//                        myStartAt.add(comps.get(0), true), p);
//                Util.drawLine(canvas,
//                        myStartAt,
//                        myStartAt.add(comps.get(1), true), p);


//            Vector v = startAt.add(comps.get(0),true).add(comps.get(1),false);
//            canvas.drawCircle(v.x,v.y,10,p);


//            Util.drawLine(canvas,
//                    startAt.add(comps.get(0),true).add(comps.get(1),false),
//                    startAt, p);
//                float percent = 1f;
//                float back = -.5f;

//                    Util.drawLine(canvas,
//                            startAt.add(comps.get(0).scale(percent, true), true).add(comps.get(1).scale(percent,true),false),
//                            startAt.add(comps.get(0).scale(percent, true), true).add(comps.get(1).scale(back*percent,true),false), p);
//                    Util.drawLine(canvas,
//                            startAt.add(comps.get(0).scale(percent, true), true).add(comps.get(1).scale(back*percent,true),false),
//                            startAt.add(comps.get(0).scale(back*percent, true), true).add(comps.get(1).scale(back*percent,true),false), p);
//                    Util.drawLine(canvas,
//                            startAt.add(comps.get(0).scale(back*percent, true), true).add(comps.get(1).scale(back*percent,true),false),
//                            startAt.add(comps.get(0).scale(back*percent, true), true).add(comps.get(1).scale(percent,true),false), p);
//                    Util.drawLine(canvas,
//                            startAt.add(comps.get(0).scale(back*percent, true), true).add(comps.get(1).scale(percent,true),false),
//                            startAt.add(comps.get(0).scale(percent, true), true).add(comps.get(1).scale(percent,true),false), p);

//                    Util.drawLine(canvas,
//                            startAt.add(comps.get(1), true).add(comps.get(0).scale(percent, true), false),
//                            startAt.add(comps.get(0).scale(percent, true), true).add(comps.get(1).scale(percent, true), false), p);
//                    Util.drawLine(canvas,
//                            startAt.add(comps.get(0), true).add(comps.get(1).scale(percent, true), false),
//                            startAt.add(comps.get(0).scale(percent, true), true).add(comps.get(1).scale(percent, true), false), p);
                //Util.drawLine(canvas, startAt, startAt.addAsNew(comps.get(0)), p);
//            Util.drawLine(canvas,
//                    startAt.add(comps.get(0),true),
//                    startAt.add(comps.get(0),true).add(comps.get(1),false), p);
//            Util.drawLine(canvas,
//                    startAt.add(comps.get(0),true).add(comps.get(1),false),
//                    startAt.add(comps.get(1),true), p);
                //Util.drawLine(canvas,startAt.addAsNew(comps.get(1)),startAt,p);

                //scale +=.05f;

        }

        picture.endRecording();

        return Util.pictureDrawable2Bitmap(picture);
    }

    public boolean sharesFace(Brick brick) {
        // we share a face if two of the values in index are equal
        int numberEqual = 0;
        Index myIndex = getIndex();
        Index otherIndex = brick.getIndex();
        for (int i = 0; i < myIndex.size(); i++) {
            if (myIndex.get(i).equals(otherIndex.get(i))) {
                numberEqual++;
                if (numberEqual >= owner.dim.get() - 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean sharesSpecificFace(Brick brick,Face f) {
        // we share a face if two of the values in index are equal
        int numberEqual = 0;
        Index myIndex = getIndex();
        Index otherIndex = brick.getIndex();
        for (int i = 0; i < myIndex.size(); i++) {
            if (myIndex.get(i).equals(otherIndex.get(i)) && f.getIndex().get(i) == FaceIndex.FaceValue.NONE) {
                numberEqual++;
                if (numberEqual >= owner.dim.get() - 2) {
                    return true;
                }
            }
        }
        return false;
    }
}
