package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Colin_000 on 4/22/2015.
 */
public class AnimatedBrick extends BitmapBacked implements HasVectorDims, Animation {
    private final Index from;
    private final int dim1;
    private final int dim2;
    private final boolean direction;
    public final Brick brick;
    private long startAt=-1;
    private final long runTime =1000;
    public AnimatedBrick next=null;

    public AnimatedBrick(Index from, int dim1,int dim2, boolean direction ,Brick brick){
        super(brick.owner);
        this.from = from;
        this.dim1 = dim1;
        this.dim2 = dim2;
        this.direction = direction;
        this.brick = brick;
    }


    @Override
    public Bitmap updateBitmap(){
        return brick.getBitmap();
    }

    public void draw(Canvas canvas) {
        if (startAt == -1){
            startAt = System.currentTimeMillis();
        }

        //myInvalidate();
        Vector to = brick.getIndex().getVector(brick.owner);
        if (next !=null){
            to = brick.getIndex().rotate(dim1,dim2,!direction,brick.owner).getVector(brick.owner);
        }
        Vector from  = brick.getIndex().rotate(dim1,dim2,!direction,brick.owner).getVector(brick.owner);
        if (next !=null){
            from = brick.getIndex().rotate(dim1,dim2,!direction,brick.owner).rotate(dim1,dim2,!direction,brick.owner).getVector(brick.owner);
        }
        Vector op  = brick.getIndex().rotate(dim1, dim2, !direction, brick.owner).rotate(dim1,dim2,!direction,brick.owner).getVector(brick.owner);
        if (next !=null){
            op  = brick.getIndex().rotate(dim1,dim2,direction,brick.owner).getVector(brick.owner);
        }

        Vector center = op.add(to,true).scale(.5f,false);

        Vector toArm = to.add(center.scale(-1f,true),true);
        Vector fromArm = from.add(center.scale(-1f,true),true);

        Vector result = new Vector();

        float angle = getAnlge();

        result.x = (float) (center.x+ fromArm.x*Math.cos(angle) + toArm.x*Math.sin(angle));
        result.y = (float) (center.y+ fromArm.y*Math.cos(angle) + toArm.y*Math.sin(angle));

        drawBitmap(canvas, result.x -  brick.myRadius(), result.y -  brick.myRadius(), new Paint());
    }

    private float getAnlge() {
        float angle = (System.currentTimeMillis()- startAt)/(float)runTime;
        angle = Math.min(angle, 1);
        // we put it through a sine so it spins a little nicer
        angle = (float) (angle*Math.PI - (Math.PI/2f));
        angle = (float) Math.sin(angle);
        angle = (angle+1)/2f;
        angle = (float)(angle*Math.PI/2f);
        return angle;
    }


    @Override
    public Vector vectorForDimension(int i) {
        Vector to = brick.owner.vectorForDimension(i);
        if (next !=null){
            if (direction) {
                if (i == dim1) {
                    to = brick.owner.vectorForDimension(dim2).scale(-1, false);
                } else if (i == dim2) {
                    to = brick.owner.vectorForDimension(dim1);
                }
            } else {
                if (i == dim1) {
                    to = brick.owner.vectorForDimension(dim2);
                } else if (i == dim2) {
                    to = brick.owner.vectorForDimension(dim1).scale(-1, false);
                }
            }
        }
        Vector from  = brick.owner.vectorForDimension(i);
        if (next ==null){
            if (direction) {
                if (i == dim1) {
                    from = brick.owner.vectorForDimension(dim2).scale(-1, false);
                } else if (i == dim2) {
                    from = brick.owner.vectorForDimension(dim1);
                }
            } else {
                if (i == dim1) {
                    from = brick.owner.vectorForDimension(dim2);
                } else if (i == dim2) {
                    from = brick.owner.vectorForDimension(dim1).scale(-1, false);
                }
            }
        }else{
            if (direction) {
                if (i == dim1) {
                    from = brick.owner.vectorForDimension(dim1).scale(-1, false);
                } else if (i == dim2) {
                    from = brick.owner.vectorForDimension(dim2).scale(-1, false);;
                }
            } else {
                if (i == dim1) {
                    from = brick.owner.vectorForDimension(dim1).scale(-1, false);;
                } else if (i == dim2) {
                    from = brick.owner.vectorForDimension(dim2).scale(-1, false);
                }
            }
        }
        Vector result = new Vector();

        float angle = getAnlge();

        result.x = (float) (from.x*Math.cos(angle) + to.x*Math.sin(angle));
        result.y = (float) (from.y*Math.cos(angle) + to.y*Math.sin(angle));

        return result;
    }

    @Override
    public Vector getStartAt() {
        return brick.owner.getStartAt();
    }

    @Override
    public boolean done() {
        return startAt!=-1 && System.currentTimeMillis()- startAt>runTime;
    }
}
