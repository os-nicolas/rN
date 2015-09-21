package cube.d.n.rn;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.lang.String;import java.lang.System;import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Colin_000 on 4/9/2015.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    public Tess owner;
    int frames = 0;
    private Square square;
    private long startedAt;

    public MyGLRenderer(Tess owner) {
        super();
        this.owner = owner;
    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        startedAt = System.currentTimeMillis();

        // initialize a triangle

        square = new Square();
    }

    public void onDrawFrame(GL10 gl) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);


        gl.glEnable(gl.GL_BLEND);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);

        long now = System.currentTimeMillis();


        Tess c = owner;
        if (c != null) {
            DrawInfo base = new DrawInfo(new ToDraw());//,0,canvas.getHeight(),0,canvas.getWidth()
            //TODO
            //c.draw(base);

            square.updateColors(base.toDraw.getColors());
            square.updateCoords(base.toDraw.getCoords());
            square.updateOrder(base.toDraw.getOrder());

            square.draw(gl);
        }
        frames++;
        float seconds = (now - startedAt) / 1000f;
        if (frames % 200 == 0) {
            Log.i("fps", "fps: " + frames / seconds + "");
        }
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }
}