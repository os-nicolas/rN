package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Colin_000 on 4/21/2015.
 */
public abstract class BitmapBacked {

    private Bitmap myBitmap;
    private boolean dirty = true;
    protected View owner;

    public BitmapBacked(View owner){
        super();
        this.owner = owner;
    }

    public void myInvalidate(){
        if (owner != null){
            owner.invalidate();
        }
        dirty = true;
    }

    protected void drawBitmap(Canvas canvas, float x, float y, Paint paint) {
        if (dirty){
            // we set dirty to false first so animating objects can set it to true again in updateBitmap
            dirty= false;
            myBitmap = updateBitmap();

        }
        canvas.drawBitmap(myBitmap,x,y,paint);
    }



    protected abstract Bitmap updateBitmap() ;

    public void setOwner(View view) {
        owner = view;
    }


    public void updateBitmap(Bitmap bitmap) {
        this.myBitmap = bitmap;
        dirty = false;
    }
}
