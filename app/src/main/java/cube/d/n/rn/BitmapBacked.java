package cube.d.n.rn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Colin_000 on 4/21/2015.
 */
public abstract class BitmapBacked {

    private Bitmap myBitmap;
    private boolean dirty = true;

    public void invalidate(){
        dirty = true;
    }

    protected void drawBitmap(Canvas canvas, float x, float y, Paint paint) {
        if (dirty){
            myBitmap = updateBitmap();
            dirty= false;
        }
        canvas.drawBitmap(myBitmap,x,y,paint);
    }

    protected abstract Bitmap updateBitmap() ;

}
