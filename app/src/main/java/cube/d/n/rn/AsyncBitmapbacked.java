package cube.d.n.rn;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Colin_000 on 5/21/2015.
 */

public abstract class AsyncBitmapbacked {

    private Bitmap myBitmap;
    private boolean dirty = true;
    private boolean alreadyUpdating = false;
    protected View owner;

    public AsyncBitmapbacked(View owner){
        super();
        this.owner = owner;
    }

    public void myInvalidate(){
        if (owner != null){
            ((Activity)owner.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    owner.invalidate();
                }
            });

        }
        dirty = true;
    }

    protected void drawBitmap(Canvas canvas, float x, float y, Paint paint) {
        if (dirty || myBitmap == null){
            if (myBitmap == null){
                update();
            }else {
                if (!alreadyUpdating) {
                    alreadyUpdating = true;
                    Thread th = new Thread() {
                        @Override
                        public void run() {
                            update();
                        }
                    };
                    th.start();
                    alreadyUpdating = false;
                }
            }

        }
        canvas.drawBitmap(myBitmap,x,y,paint);
    }

    private void update() {
        // we set dirty to false first so animating objects can set it to true again in updateBitmap
        dirty= false;
        myBitmap = updateBitmap(myBitmap);
    }

    protected abstract Bitmap updateBitmap(Bitmap old) ;

    public void setOwner(View view) {
        owner = view;
    }
}
