package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
    final int dbColor;
    final Index startIndex;
    Paint p;

    // bricks have a color for every pair of dimensions
    HashMap<Index, Face> faces = new HashMap<>();

    public Brick(Index currentIndex,Index startIndex, Tess owner) {
        super(owner);
        this.owner = owner;
        owner.addBrick(currentIndex, this);
        this.startIndex = new Index(startIndex);
        initFaces(new Index(owner.size.get()));

        p = new Paint();
        Random r = new Random();
        dbColor = r.nextInt(0xffffff) + 0x88000000;
    }

    public Brick(Index i, Tess owner) {
        this(i,i,owner);
    }

    private void initFaces(Index at) {
        // for every pair of sides we need a face
        if (at.size() == owner.dim.get()) {
            Index faceIndex = new Index(at);
            Index ownerIndex = getIndex();

            // we zero out the place of face index in ownerIndex
            for (int i = 0; i < faceIndex.size(); i++) {
                if (0 != faceIndex.get(i)) {
                    ownerIndex.set(i, 0);
                }
            }

            // we zero out owner indexes that are not zero or size-1
            for (int i = 0; i < ownerIndex.size(); i++) {
                if (0 != ownerIndex.get(i) && owner.size.get() - 1 != ownerIndex.get(i)) {
                    ownerIndex.set(i, 0);
                    faceIndex.set(i, 1);
                }
            }
            if (faceIndex.noneZeroComps() == 2) {
                faces.put(faceIndex, new Face(this, faceIndex, ownerIndex));
            }

        } else {
            if (at.noneZeroComps() < 2) {
                Index temp1 = new Index(at);
                if (getIndex().get(at.size()) == 0) {
                    temp1.add(1);
                    initFaces(temp1);
                } else if (getIndex().get(at.size()) == owner.size.get() - 1) {
                    temp1.add(-1);
                    initFaces(temp1);
                }

            }
            // if the number of noneZeroComps we need add is less than the number of spots we have left
//            if (2-at.noneZeroComps() < owner.dim.get()-at.size()) {
            Index temp2 = new Index(at);
            temp2.add(0);
            initFaces(temp2);
//            }
        }
    }

//    public boolean isActive() {
//        return this.equals(owner.active.get());
//    }

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
            p.setAlpha(alpha);
            drawBitmap(canvas, startAt.x - myRadius(), startAt.y - myRadius(), p);
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

    public Index indexOf(Face face) {
        for (Index i : faces.keySet()) {
            Face mine = faces.get(i);
            if (mine.equals(face)) {
                return i;
            }
        }
        return null;
    }

    public void rotateFaces(int dim1, int dim2, boolean direction) {
        HashMap<Index, Face> toChange = new HashMap<>();

        for (Index index : faces.keySet()) {
            if (index.get(dim1) != 0 || index.get(dim2) != 0) {
                toChange.put(index, faces.get(index));
            }
        }

        // now we remove
        for (Index myIndex : toChange.keySet()) {
            faces.remove(myIndex);
        }

        // now we rotate and put them back
        for (Index myIndex : toChange.keySet()) {
            Face myFace = toChange.get(myIndex);
            Index newIndex = new Index(myIndex);
            if (direction) {
                newIndex.set(dim1, -myIndex.get(dim2));
                newIndex.set(dim2, myIndex.get(dim1));
            } else {
                newIndex.set(dim1, myIndex.get(dim2));
                newIndex.set(dim2, -myIndex.get(dim1));
            }
            faces.put(newIndex, myFace);
        }
        myInvalidate();
    }

    public boolean in(Vector vector) {
        return distance(vector) < 1.6*myRadius();
    }

    public float distance(Vector vector) {
        return getVector().distance(vector);
    }

    public ArrayList<SpinTo> getSpinTos() {
        ArrayList<SpinTo> result = new ArrayList<>();
        // we need to itterate over the ways we can spin
        // that is for every pair of dimensions
        for (Index side : faces.keySet()) {
            // we can spin 1,2 or -1
            int[] spins = new int[]{1, 2, -1};
            for (int spin : spins) {
                result.add(new SpinTo(this, new Index(side), spin, owner));
            }

        }
        return result;
    }

    public Vector getVector() {
        return getIndex().getVector(owner);
    }


    public float myRadius(){
        return owner.radius*1.5f;
    }

    public Bitmap getBitmap(HasVectorDims hasVectorDims) {
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording((int) (2 *myRadius()), (int) (2 * myRadius()));

        Vector startAt = new Vector(myRadius(), myRadius());

        Paint grey = new Paint();
        grey.setColor(dbColor);
        //canvas.drawCircle(startAt.x,startAt.y,(isActive()?radius:radius/2f),grey);

        Paint p = new Paint();
        p.setStrokeWidth(20);
        p.setColor(getColor());

        Util.drawCircle(canvas, startAt, 20, p);

        //Util.drawCircle(canvas,startAt,owner.radius,p);

        for (int at=0;at<startIndex.size();at++){
            int valueAt = startIndex.get(at);
            int endWidth =  (Util.pointsAtAnyThing(owner, startIndex, at)?10:0);
                if (valueAt != 0) {
                    // we draw a line down from the center
                    Util.drawLine(canvas,
                            startAt,
                            startAt.add(owner.vectorForDimension(at).toUnit(true).scale(-owner.radius, false), true),
                            getColor(),
                            20,
                            endWidth);

                }
                if (valueAt != owner.size.get() - 1) {
                    Util.drawLine(canvas,
                            startAt,
                            startAt.add(owner.vectorForDimension(at).toUnit(true).scale(owner.radius, false), true),
                            getColor(),
                            20,
                            endWidth);
                }
        }

//        //float scale =.15f;
//        for (Face f : faces.values()) {
//            if (owner.drawFace(this, f)) {
//                //TODO maybe faces should know how to draw themselves
//
//                // we need to get the compent vectors
//                ArrayList<Vector> comps = f.getIndex().getCompnetVectors(hasVectorDims);
//                if (comps.size() < 2) {
//                    Log.e("brick", "comps should be size 2");
//                }
//
//
//                for (Vector v : comps) {
//                    v.toUnit(false).scale(owner.radius / 2f, false);
//                }
//
//                Paint p = new Paint();
//                p.setStrokeWidth(5);
//                p.setColor(f.getColor());
//
//
//                float percent = .5f;
////                Util.drawLine(canvas,
////                        startAt.add(comps.get(0), true).add(comps.get(1).scale(percent, true), false),
////                        startAt.add(comps.get(0), true).add(comps.get(1), false), p);
////                Util.drawLine(canvas,
////                        startAt.add(comps.get(0), true).add(comps.get(1), false),
////                        startAt.add(comps.get(1), true).add(comps.get(0).scale(percent, true), false), p);
////                Util.drawLine(canvas,
////                        startAt.add(comps.get(1), true).add(comps.get(0).scale(percent, true), false),
////                        startAt, p);
////                Util.drawLine(canvas,
////                        startAt.add(comps.get(0), true).add(comps.get(1).scale(percent, true), false),
////                        startAt, p);
//
//                Util.drawLine(canvas,
//                        startAt.add(comps.get(1), true),
//                        startAt, p);
//                Util.drawLine(canvas,
//                        startAt.add(comps.get(0), true),
//                        startAt, p);
//
//
//            }
//
//        }

        picture.endRecording();

        return Util.pictureDrawable2Bitmap(picture);
    }

    private int getColor() {
        Vector myLoc = startIndex.getVector(owner);

        return Util.getColor(owner,myLoc);
    }

    public boolean sharesFace(Brick brick) {
        // we share a face if two of the values in index are equal
        int numberEqual = 0;
        Index myIndex = getIndex();
        Index otherIndex = brick.getIndex();
        for (int i = 0; i < myIndex.size(); i++) {
            if (myIndex.get(i).equals(otherIndex.get(i))) {
                numberEqual++;
                if (numberEqual >= owner.size.get() - 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCornor() {
        Index index = getIndex();
        for (int i =0;i< index.size();i++){
            if (!(index.get(i).equals(new Integer(0)) || index.get(i).equals(owner.size.get()-1))){
                return false;
            }
        }
        return true;
    }
}
