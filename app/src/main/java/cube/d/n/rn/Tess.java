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

import cube.d.n.rn.filter.Filter;

/**
 * Created by Colin_000 on 4/17/2015.
 */
public class Tess extends View implements View.OnTouchListener, HasVectorDims {

    public HashMap<Index, Brick> bricks = new HashMap<>();
    final public GS<Integer> size = new GS<>();
    final public GS<Integer> dim = new GS<>();

    final public GS<Filter> filter = new GS<Filter>(){
        @Override
        public void set(Filter f){
            super.set(f);
            invalidateBricks();

        }
    };
    private final ArrayList<OnScreenButton> filterButtons = new ArrayList<>();

    private void invalidateBricks() {
        for (Brick b: bricks.values()){
            b.myInvalidate();
        }
    }

    private Vector startAt;
    ArrayList<SpinTo> spinTos = new ArrayList<SpinTo>();
    ArrayList<Animation> animations = new ArrayList<>();

    public Tess(Context context, int dim, int size) {
        super(context);
        if (dim > RN.rn().getMaxSize()) {
            Log.w("Tess", "dim exceeds max dimension");
        }
        init(dim, size);
    }

    private void init(int dim, int size) {
        this.dim.set(dim);
        this.size.set(size);
        initCube(new Index());

        final Tess that = this;
        this.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        intiDimVectors(getWidth(), getHeight());
                        that.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        //init the filters buttons
                        final int end = that.size.get()-1;
                        Index current = new Index(new int[]{0,0,0,0});
                        for (int at=0;at<that.dim.get();at++){
                            current = new Index(current);
                            current.set(at,end);
                            filterButtons.add(new CornoreFilterButton(that,current));
                            filterButtons.add(new EdgeFilterButton(that,at,end));
                        }
                        for (int at=0;at<that.dim.get();at++){
                            current = new Index(current);
                            current.set(at,0);
                            filterButtons.add(new CornoreFilterButton(that,current));
                            filterButtons.add(new EdgeFilterButton(that,at,0));
                        }
                    }
                }
        );







        this.setOnTouchListener(this);
    }

    public Tess(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(4, 3);
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


    public void initCube(Index at) {
        if (at.size() == dim.get()) {
            new Brick(new Index(at), this);
        } else {
            for (int i = 0; i < size.get(); i++) {
                Index temp = new Index(at);
                temp.add(i);
                initCube(temp);
            }
        }

    }

    long startTime = System.currentTimeMillis();
    int frames = 1;

    @Override
    public void onDraw(Canvas canvas) {

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

        // draw outline
        Paint light = new Paint();
        light.setColor(0x11888888);
        light.setStrokeWidth(5);
        Paint mid = new Paint();
        mid.setColor(0x55888888);
        mid.setStrokeWidth(5);
        Paint dark = new Paint();
        dark.setColor(0xBB888888);
        dark.setStrokeWidth(5);

        for (Brick b : bricks.values()) {
            Index index = b.getIndex();
            for (int at = 0; at < index.size(); at++) {
                if (isCorner(index) && index.get(at) == 0) {
                    // when the index is zero draw a line from b to
                    // to a copy of b where at have an index of size
                    Index indexTo = new Index(index);
                    indexTo.set(at, size.get() - 1);
                    if ((active.get() != null && active.get().sharesFace(b) && active.get().sharesFace(bricks.get(indexTo)))&&(filter.get() == null || filter.get().drakLine(index,indexTo))){
                        Util.drawLine(canvas, index.getVector(this), indexTo.getVector(this), dark);
                    }else if (filter.get() != null && filter.get().drakLine(index,indexTo)){
                        Util.drawLine(canvas, index.getVector(this), indexTo.getVector(this), mid);
                    } else {
                        Util.drawLine(canvas, index.getVector(this), indexTo.getVector(this), light);
                    }
                }
            }

        }

        // draw bricks
        for (Brick b : bricks.values()) {
            b.draw(canvas, 0xff);
        }

        // draw spin tos
        for (SpinTo st: spinTos){
            if (drawSpinTo(st)) {
                st.draw(canvas);
            }
        }

        for (OnScreenButton osb: filterButtons){
            osb.draw(canvas);
        }

        long now = System.currentTimeMillis();
        float elapsedTime = (now - startTime) / 1000f;
        frames++;
        if (frames % 100 == 0) {
            Log.i("fps", "" + frames / elapsedTime);
        }

        invalidate();
    }

    private boolean isCorner(Index index) {
        for (int at = 0; at < index.size(); at++) {
            if (index.get(at)!=0 && index.get(at)!=size.get()-1){
                return false;
            }
        }

        return true;
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
        for (float i = 0; i < dim.get(); i++) {
            double angle = Math.PI * (i + .5) / (dim.get()) - Math.PI / 2f;
            Vector v = new Vector((float) Math.sin(angle), (float) Math.cos(angle));
            Log.i("adding vector:", v + "");
            dimensionVectors.put((int) i, v);
        }

        // scale the vectors to fit in the screen
        float xSum = 0;
        float ySum = 0;
        for (Vector v : dimensionVectors.values()) {
            xSum += Math.abs(v.x) * (size.get() - 1);
            ySum += Math.abs(v.y) * (size.get() - 1);
        }
        float buffer = 30;//TODO scale by dpi
        scale = Math.min(
                (width - 2 * buffer) / xSum,
                (height - 2 * buffer) / ySum);

        for (Vector v : dimensionVectors.values()) {
            v.scale(scale, false);
        }

        // find out start point
        startAt = new Vector(width / 2f, (height - ySum * scale) / 2f);
    }

    public void addBrick(Index i, Brick brick) {
        bricks.put(i, brick);
    }

    boolean dead = false;
    boolean justSelected = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return onTouch(event);


    }

    public boolean onTouch(MotionEvent event) {
        if (event.getPointerCount() == 1 && (!dead || event.getAction() == MotionEvent.ACTION_DOWN)) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && active.get() == null) {
                Brick closest = null;
                float minDis = Float.MAX_VALUE;
                Vector eventAt = new Vector(event);
                dead = false;
                for (Brick b : bricks.values()) {
                    if (b.in(eventAt)) {
                        float myDistance = b.distance(eventAt);
                        if (myDistance < minDis) {
                            closest = b;
                            minDis = myDistance;
                        }
                    }
                }
                if (closest != null) {
                    Log.i("new Active", closest + "");
                    active.set(closest);
                    justSelected = true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                // did they click a spin to?
                boolean keepGoing = clickSpinTos(event);
                // did they click a filter?
                if (keepGoing){
                    keepGoing = clickFileters(event);
                }

                if (!justSelected) {
                    active.set(null);
                }
                justSelected = false;
            }
        } else {
            dead = true;
        }
        return true;
    }

    private boolean clickFileters(MotionEvent event) {
        Vector eventAt = new Vector(event);
        OnScreenButton closest = null;
        float minDis = Float.MAX_VALUE;
        for (OnScreenButton st : filterButtons) {
            if (st.in(eventAt)) {
                float myDistance = st.distance(eventAt);
                if (myDistance < minDis) {
                    closest = st;
                    minDis = myDistance;
                }
            }
        }
        if (closest != null) {
            Log.i("filter", closest + "");
            closest.go();
            active.set(null);
            return false;
        }
        return true;
    }

    private boolean clickSpinTos(MotionEvent event) {
        Vector eventAt = new Vector(event);
        SpinTo closest = null;
        float minDis = Float.MAX_VALUE;
        for (SpinTo st : spinTos) {
            if (st.in(eventAt)) {
                float myDistance = st.distance(eventAt);
                if (myDistance < minDis) {
                    closest = st;
                    minDis = myDistance;
                }
            }
        }
        if (closest != null) {
            Log.i("spintTo", closest + "");
            closest.go();
            active.set(null);
            return false;
        }
        return true;
    }

    GS<Brick> active = new GS<Brick>() {
        @Override
        public void set(Brick newActive) {
            if (value != null) {
                value.myInvalidate();
            }
            if (newActive != null) {
                newActive.myInvalidate();
            }
            super.set(newActive);
            if (value != null) {
                spinTos = value.getSpinTos();
            } else {
                spinTos = new ArrayList<>();
            }
            for (Brick b : bricks.values()) {
                b.myInvalidate();
            }
        }

        @Override
        public Brick get() {
            return value;
        }
    };

    public Vector getStartAt() {
        return new Vector(startAt);
    }

    public boolean drawFace(Brick brick, Face f) {
        if (filter.get() != null && filter.get().filter(brick,f)){
            return false;
        }

//        if (active.get() != null) {
//            if (active.get().sharesSpecificFace(brick, f)) {
//            } else {
//                return false;
//            }
//        }
        return true;
    }

    private boolean drawSpinTo(SpinTo st) {
        if (filter.get() != null && filter.get().filter(st)){
            return false;
        }

//        if (active.get() != null) {
//            if (active.get().sharesSpecificFace(brick, f)) {
//            } else {
//                return false;
//            }
//        }
        return true;
    }

    public void draw(DrawInfo base) {
        //TODO
    }

    public void setBounds(int i, int i1, int width, int height, DrawInfo base) {
        //TODO
    }
}
