package cube.d.n.rn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Colin_000 on 4/17/2015.
 */
public class Tess extends View implements View.OnTouchListener, HasVectorDims, NoScroll {

    public HashMap<Index, Brick> bricks = new HashMap<>();
    public GS<Integer> size = new GS<>();
    public GS<Integer> dim = new GS<>();
    private Vector startAt;
    //ArrayList<SpinTo> spinTos = new ArrayList<SpinTo>();
    ArrayList<Animation> animations = new ArrayList<>();
    private OutLine outline;
    public float radius = 50;
    private Problem problem;

    public Tess(Context context, int dim, int size) {
        super(context);

        init(dim, size);
        Log.d("cubeString", getCubeString());
    }

    public void init(final int dim, final int size) {
        final Tess that = this;


        pInit(dim, size);

        initCube(new Index(that.size.get()));

    }

    public void init(final int dim, final int size, final String cubeRep) {


        pInit(dim, size);

        initCube(cubeRep);

        //Log.d("init", "dim: " + dim + " size: " + size + " cubeRep: " + cubeRep);

    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    private void pInit(int dim, int size) {
        animations = new ArrayList<>();
        this.dim.set(dim);
        this.size.set(size);

        final Tess that = this;
        this.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        intiDimVectors(getWidth(), getHeight());
                        that.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
        );

        this.setOnTouchListener(this);
    }

    public Tess(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init(3, 3);
    }

    public void rotate(Index startAt, int dim1, int dim2, boolean direction) {

        // find the set of points we are going to rotate
        // uses the og indexs
        HashMap<Index, Brick> toRotate = new HashMap<>();
        for (int i = 0; i < size.get(); i++) {
            for (int j = 0; j < size.get(); j++) {
                Index myIndex = new Index(startAt);
                myIndex.set(dim1, i);
                myIndex.set(dim2, j);
                Brick myBrick = bricks.get(myIndex);
                toRotate.put(myIndex, myBrick);
            }
        }

        // now we need to rotate them
        for (Index myIndex : toRotate.keySet()) {
            if (Util.hasAtleastOneEdge(this, myIndex)) {
                Brick myBrick = toRotate.get(myIndex);
                myBrick.rotateFaces(dim1, dim2, direction);
                Index newIndex = myIndex.rotate(dim1, dim2, direction, this);
                bricks.put(newIndex, myBrick);

                // we also need to create animations
                if (myBrick.hasAnimation()) {
                    myBrick.getAnimation().next = new AnimatedBrick(myIndex, dim1, dim2, direction, myBrick);
                } else {
                    animations.add(new AnimatedBrick(myIndex, dim1, dim2, direction, myBrick));
                }
            }
        }
        if (problem != null && !problem.getSolved()) {//
            if (isCurrentlySolved()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(AnimatedBrick.runTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        problem.setSolved(true);
                    }
                }).start();
            }
        }
    }

    public String getCubeString() {
        String res = "";
        for (int at = 0; at < Math.pow(size.get(), dim.get()); at++) {
            Index tempIndex = new Index(size.get(), at, dim.get());
            if (Util.hasAtleastOneEdge(this, tempIndex)) {
                res += bricks.get(tempIndex).startIndex.pos() + ",";
            } else {
                res += "-,";
            }
        }
        return res;
    }


    public void initCube(Index at) {
        if (at.size() == dim.get()) {
            if (Util.hasAtleastOneEdge(this, at)) {
                new Brick(new Index(at), this);
            }
        } else {
            for (int i = 0; i < size.get(); i++) {
                Index temp = new Index(at);
                temp.add(i);
                initCube(temp);
            }
        }
    }

    public synchronized void resetTo(String cubeRep) {
        bricks = new HashMap<>();
        animations = new ArrayList<>();
        initCube(cubeRep);
    }

    private void initCube(String cubeRep) {
//        new Index(5,3);
        String[] split = cubeRep.split(",");
        int at = 0;
        for (String s : split) {
            if (s.equals("-")) {
            } else {
                new Brick(new Index(size.get(), at, dim.get()), new Index(size.get(), Integer.parseInt(s), dim.get()), this);
            }
            at++;
        }
    }

    public boolean isCurrentlySolved() {
        for (Brick b : bricks.values()) {
            if (!b.startIndex.equals(b.getIndex())) {
                return false;
            }
        }
        return true;
    }

    long startTime = System.currentTimeMillis();
    int frames = 1;

    @Override
    public synchronized void onDraw(Canvas canvas) {
        // draw outline

        Paint red = new Paint();
        red.setColor(0xFFFFFF00);
        red.setStrokeWidth(45);

        // draw the path we are outlining
        for (int i = 0; i < path.size() - 1; i++) {
            Util.drawLine(canvas, path.get(i).getVector(), path.get(i + 1).getVector(), red);
        }
        if (path.size() != 0 && current != null && path.size() < 3) {
            Util.drawLine(canvas, path.get(path.size() - 1).getVector(), current, red);
        }

        outline.drawBitmap(canvas, 0, 0, new Paint());

        // draw animations
        for (int i = animations.size() - 1; i >= 0; i--) {
            Animation a = animations.get(i);
            if (a.done()) {
                if (a instanceof AnimatedBrick && ((AnimatedBrick) a).next != null) {
                    animations.set(i, ((AnimatedBrick) a).next);
                    ((AnimatedBrick) a).next.draw(canvas);
                } else {
                    animations.remove(a);
                }
            } else {
                a.draw(canvas);
            }
        }

        // draw bricks
        for (Brick b : bricks.values()) {
            //if (active.get() == null || b.sharesFace(active.get())) {
            b.draw(canvas, 0xff);
            //}
        }

        long now = System.currentTimeMillis();
        float elapsedTime = (now - startTime) / 1000f;
        frames++;
        if (frames % 100 == 0) {
            Log.i("fps", "" + frames / elapsedTime);
        }

        invalidate();
    }

    public Index indexOf(Brick brick) {
        for (Index i : bricks.keySet()) {
            Brick mine = bricks.get(i);
            if (mine.equals(brick)) {
                return i;
            }
        }
        return null;
    }

    private HashMap<Integer, Vector> dimensionVectors = new HashMap<>();

    public Vector vectorForDimension(int i) {
        return new Vector(dimensionVectors.get(i));
    }


    public float scale;

    private void intiDimVectors(float width, float height) {
        float d = 1;
        for (float i = 0; i < dim.get(); i++) {
            double angle = Math.PI * (i + .5) / (dim.get()) - Math.PI / 2f;
            Vector v = new Vector((float) Math.sin(angle) * d, (float) Math.cos(angle) * d);
            d *= (float) size.get();
            Log.i("adding vector:", v + "");
            dimensionVectors.put((int) i, v);
        }

        // scale the vectors to fit in the screen
        float xSum = 1;
        float leftSum = .5f;
        float ySum = 1;
        for (Vector v : dimensionVectors.values()) {
            xSum += Math.abs(v.x) * (size.get() - 1);
            if (v.x < 0) {
                leftSum += Math.abs(v.x) * (size.get() - 1);
            }
            ySum += Math.abs(v.y) * (size.get() - 1);
        }
        //float buffer = 30;//TODO scale by dpi
        scale = Math.min(
                (width) / xSum,//- 2 * buffer
                (height) / ySum);// - 2 * buffer

        for (Vector v : dimensionVectors.values()) {
            v.scale(scale, false);
        }

        // find out start point
        startAt = new Vector((leftSum * scale), (height - ySum * scale) / 2f);


        outline = new OutLine(this, Math.max(width / 2, height / 2));
    }

    public void addBrick(Index i, Brick brick) {
        bricks.put(i, brick);
    }

    boolean dead = false;
    ArrayList<Brick> path = new ArrayList<>();
    Vector current;

    //    boolean justSelected =false;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getPointerCount() == 1 && (!dead || event.getAction() == MotionEvent.ACTION_DOWN)) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dead = false;
                path = new ArrayList<Brick>();
            }
            Brick closeBrick = nearAny(event);
            if (closeBrick == null) {
            } else if (path.contains(closeBrick)) {
                // remove everything after closet
                for (int i = path.size() - 1; i > path.indexOf(closeBrick); i--) {
                    path.remove(i);
                }
            } else if (path.size() > 1 && legalNext(path.get(path.size() - 2), closeBrick)) {
                path.set(path.size() - 1, closeBrick);
            } else if (path.size() < 3 && legalNext(closeBrick)) {
                path.add(closeBrick);
            }
            Vector myPoint = new Vector(event);
            current = myPoint;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                //we have to actully do the spin
                if (path.size() == 3) {
                    SpinTo sp = getSpinTo(path);
                    sp.go();
                }

                path = new ArrayList<Brick>();
                current = null;
            }
        } else {
            dead = true;
        }


//        if (event.getPointerCount()==1 && (!dead || event.getAction() == MotionEvent.ACTION_DOWN)){
//            if (event.getAction() == MotionEvent.ACTION_DOWN && active.get() == null){
//                Brick closest=null;
//                float minDis = Float.MAX_VALUE;
//                Vector eventAt = new Vector(event);
//                dead = false;
//                for (Brick b: bricks.values()){
//                    if (b.in(eventAt)){
//                        float myDistance = b.distance(eventAt);
//                        if (myDistance < minDis){
//                            closest=b;
//                            minDis= myDistance;
//                        }
//                    }
//                }
//                if (closest != null){
//                    Log.i("new Active", closest + "");
//                    active.set(closest);
//                    justSelected = true;
//                }
//            }else if (event.getAction() == MotionEvent.ACTION_UP){
//                Vector eventAt = new Vector(event);
//                SpinTo closest=null;
//                float minDis = Float.MAX_VALUE;
//                for (SpinTo st: spinTos){
//                    if (st.in(eventAt)){
//                        float myDistance = st.distance(eventAt);
//                        if (myDistance < minDis){
//                            closest=st;
//                            minDis= myDistance;
//                        }
//                    }
//                }
//                if (closest != null){
//                    Log.i("spintTo", closest + "");
//                    closest.go();
//                    active.set(null);
//                }
//                if (!justSelected){
//                    active.set(null);
//                }
//                justSelected = false;
//            }
//        }else {
//            dead = true;
//        }
        return true;
    }

    private SpinTo getSpinTo(ArrayList<Brick> path) {
        Index plane = new Index(path.get(0).getIndex());
        for (int j = 0; j < plane.size(); j++) {
            plane.set(j, 1);
        }
        // we find what plane all the points have in common
        for (int i = 1; i < path.size(); i++) {
            for (int j = 0; j < plane.size(); j++) {
                if (!path.get(i - 1).getIndex().get(j).equals(path.get(i).getIndex().get(j))) {
                    plane.set(j, 0);
                }
            }
        }

        for (int j = 0; j < plane.size(); j++) {
            plane.set(j, plane.get(j) == 0 ? 1 : 0);
        }

        int clicks = 0;
        int dim1 = SpinTo.getDim(plane, 0);
        int dim2 = SpinTo.getDim(plane, 1);

        if (
                new Index(path.get(0).getIndex()).rotate(dim1, dim2, true, this)
                        .equals(
                                new Index(path.get(1).getIndex()))
                ) {
            clicks = 1;
        } else {
            clicks = -1;
        }

        return new SpinTo(path.get(0), plane, clicks, this);
    }

    private boolean legalNext(Brick closeBrick) {
        if (path.size() == 0) {
            return true;
        } else {
            Brick pathEnd = path.get(path.size() - 1);
            return legalNext(pathEnd, closeBrick);
        }
    }

    private boolean legalNext(Brick pathEnd, Brick closeBrick) {
        if (path.size() == 0) {
            return true;
        } else {
            int same = 0;
            Index cbi = closeBrick.getIndex();
            Index ebi = pathEnd.getIndex();
            for (int at = 0; at < closeBrick.getIndex().size(); at++) {
                if (closeBrick.getIndex().get(at).equals(pathEnd.getIndex().get(at))) {
                    same++;
                } else {
                    // if they are not the same it better be and edge
                    if (cbi.get(at) != 0 && cbi.get(at) != size.get() - 1) {
                        return false;
                    }
                    // and the way we are comming from better also be an edge
                    if (ebi.get(at) != 0 && ebi.get(at) != size.get() - 1) {
                        return false;
                    }
                }
            }

            return same == closeBrick.getIndex().size() - 1;
        }
    }

    private Brick nearAny(MotionEvent event) {
        Brick closest = null;
        float minDis = Float.MAX_VALUE;
        Vector eventAt = new Vector(event);
        dead = false;
        for (Brick b : bricks.values()) {
            if (Util.isEdge(this, b)) {
                if (b.in(eventAt)) {
                    float myDistance = b.distance(eventAt);
                    if (myDistance < minDis) {
                        closest = b;
                        minDis = myDistance;
                    }
                }
            }
        }

        return closest;
    }

//    SuperPrvate<Brick> active = new SuperPrvate<Brick>() {
//        @Override
//        public void set(Brick newActive) {
//            if (value != null) {
//                value.myInvalidate();
//            }
//            if (newActive != null) {
//                newActive.myInvalidate();
//            }
//            value = newActive;
//            if (value != null) {
//                spinTos = value.getSpinTos();
//            } else {
//                spinTos = new ArrayList<>();
//            }
//            for (Brick b : bricks.values()) {
//                b.myInvalidate();
//            }
//        }
//
//        @Override
//        public Brick get() {
//            return value;
//        }
//    };

    public Vector getStartAt() {
        return new Vector(startAt);
    }


    public boolean drawFace(Brick brick, Face f) {
        // TODO filter
        return true;
    }


    public void rotate(Index startAt, int dim1, int dim2, boolean flip, int clicks) {
        if (clicks == 1) {
            rotate(startAt, dim1, dim2, !flip);
        } else if (clicks == 2) {
            rotate(startAt, dim1, dim2, !flip);
            rotate(startAt, dim1, dim2, !flip);
        } else if (clicks == -1) {
            rotate(startAt, dim1, dim2, flip);
        }
    }
}
