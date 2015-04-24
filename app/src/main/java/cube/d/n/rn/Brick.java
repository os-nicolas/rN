package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;

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
    HashMap<Index, Face> faces = new HashMap<>();


    public Brick(Index i, Tess owner) {
        this.owner = owner;
        owner.addBrick(i, this);
        initFaces(new Index());

        Random r = new Random();
        dbColor = r.nextInt(0xffffff) + 0x88000000;
    }

    private void initFaces(Index at) {
        // for every pair of sides we need a face
        if (at.size() == owner.dim) {
            Index faceIndex = new Index(at);
            faces.put(faceIndex, new Face(this, faceIndex, getIndex()));
        } else {
            if (at.noneZeroComps() < 2) {
                Index temp1 = new Index(at);
                if (getIndex().get(at.size()) == 0) {
                    temp1.add(1);
                    initFaces(temp1);
                } else if (getIndex().get(at.size()) == owner.size - 1) {
                    temp1.add(-1);
                    initFaces(temp1);
                }
            }
            // if the number of slots left is larger the number of ones we still have to place
            if (owner.dim - at.size() > 2 - at.noneZeroComps()) {
                Index temp2 = new Index(at);
                temp2.add(0);
                initFaces(temp2);
            }
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
        invalidate();
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

    public Bitmap getBitmap(HasVectorDims hasVectorDims) {
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording((int) (2 * radius), (int) (2 * radius));

        Vector startAt = new Vector(radius, radius);

        Paint grey = new Paint();
        grey.setColor(dbColor);
        //canvas.drawCircle(startAt.x,startAt.y,(isActive()?radius:radius/2f),grey);


        //float scale =.15f;
        for (Face f : faces.values()) {
            if (owner.active.get() == null || f.sharesSide(owner.active.get())) {

                // we need to get the compent vectors
                ArrayList<Vector> comps = f.getIndex().getCompnetVectors(hasVectorDims);
                for (Vector v : comps) {
                    v.toUnit(false).scale(radius / 2f, false);
                }

                Paint p = new Paint();
                p.setStrokeWidth(5);
                p.setColor(f.getColor());

                float per = .1f;

                Vector myStartAt = new Vector(startAt);
                Index myIndex = getIndex();
                for (int i=0;i<myIndex.size();i++){
                    if (f.getIndex().get(i).equals(new Integer(0))){
                        if (myIndex.get(i).equals(new Integer(0))){
                            myStartAt.add(owner.vectorForDimension(i).scale(-per,false),false);
                        }else if (myIndex.get(i).equals(new Integer(owner.size-1))){
                            myStartAt.add(owner.vectorForDimension(i).scale(per,false),false);
                        }
                    }

                }

                Util.drawLine(canvas,
                        myStartAt,
                        myStartAt.add(comps.get(0), true), p);
                Util.drawLine(canvas,
                        myStartAt,
                        myStartAt.add(comps.get(1), true), p);


//            Vector v = startAt.add(comps.get(0),true).add(comps.get(1),false);
//            canvas.drawCircle(v.x,v.y,10,p);


//            Util.drawLine(canvas,
//                    startAt.add(comps.get(0),true).add(comps.get(1),false),
//                    startAt, p);

//                float percent = .05f;
//
//            Util.drawLine(canvas,
//                    startAt.add(comps.get(0),true).add(comps.get(1).scale(percent,true),false),
//                    startAt.add(comps.get(0),true).add(comps.get(1),false), p);
//            Util.drawLine(canvas,
//                    startAt.add(comps.get(0),true).add(comps.get(1),false),
//                    startAt.add(comps.get(1),true).add(comps.get(0).scale(percent,true),false), p);
//                Util.drawLine(canvas,
//                        startAt.add(comps.get(1), true).add(comps.get(0).scale(percent, true), false),
//                        startAt.add(comps.get(0).scale(percent, true), true).add(comps.get(1).scale(percent, true), false), p);
//                Util.drawLine(canvas,
//                        startAt.add(comps.get(0), true).add(comps.get(1).scale(percent, true), false),
//                        startAt.add(comps.get(0).scale(percent, true), true).add(comps.get(1).scale(percent, true), false), p);
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
                if (numberEqual >= owner.size - 2) {
                    return true;
                }
            }
        }
        return false;
    }
}
