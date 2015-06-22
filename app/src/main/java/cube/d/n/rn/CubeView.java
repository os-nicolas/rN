package cube.d.n.rn;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;import java.lang.Override;

/**
 * Created by Colin_000 on 4/3/2015.
 */
public class CubeView extends GLSurfaceView implements View.OnTouchListener {

    private Tess tess;

    private MyGLRenderer mRenderer;

    public CubeView(Context context) {
        super(context);
        //init();

        init();
    }

    public CubeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Tess getCube() {
        return tess;
    }

    public void setCube(Tess tess) {
        this.tess = tess;
    }

    private void init() {
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8 , 8, 8, 8, 16, 0);

        mRenderer = new MyGLRenderer(this);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }

    public void activate() {
        setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Log.i("stop touching me!", event.getAction()+"");

        int[] yo = new int[2];
        getLocationOnScreen(yo);
        DrawInfo base = new DrawInfo(new ToDraw());//,0,canvas.getHeight(),0,canvas.getWidth()
        tess.setBounds(0, 0, getWidth(), getHeight(), base);

        tess.onTouch(event);
        return true;
    }
}
