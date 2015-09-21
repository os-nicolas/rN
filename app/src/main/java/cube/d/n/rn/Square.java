package cube.d.n.rn;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

import java.lang.String;import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Colin_000 on 4/9/2015.
 */
public class Square {

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "attribute vec4 a_Color;" +
                    "varying vec4 v_Color;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "  v_Color = a_Color;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 v_Color;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = v_Color;" +
                    "}";
    private final int mProgram;
    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    // Our color buffer.
    private FloatBuffer colorBuffer;
    private float[] squareCoords;
    private short[] drawOrder; // order to draw vertices
    private float[] colors;
    private int mPositionHandle;
    //private int mColorHandle;
    private int mColorHandleList;
    private int vertexCount;

    public Square() {

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    public void updateCoords(float[] squareCoords) {
        this.squareCoords = squareCoords;
        if (vertexBuffer == null) {
            ByteBuffer bb = ByteBuffer.allocateDirect(
                    // (# of coordinate values * 4 bytes per float)
                    squareCoords.length * 4);
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
        } else {
            vertexBuffer.clear();
        }
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);
        vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    }

    public void updateColors(float[] newColors) {
        this.colors = newColors;
        if (colorBuffer == null) {
            ByteBuffer cbb = ByteBuffer.allocateDirect(newColors.length * 4);
            cbb.order(ByteOrder.nativeOrder());
            colorBuffer = cbb.asFloatBuffer();
        } else {
            colorBuffer.clear();
        }
        colorBuffer.put(newColors);
        colorBuffer.position(0);
    }

    public void updateOrder(short[] drawOrder) {
        this.drawOrder = drawOrder;
        if (drawListBuffer == null) {
            ByteBuffer dlb = ByteBuffer.allocateDirect(
                    // (# of coordinate values * 2 bytes per short)
                    drawOrder.length * 2);
            dlb.order(ByteOrder.nativeOrder());
            drawListBuffer = dlb.asShortBuffer();
        } else {
            drawListBuffer.clear();
        }
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }

    public void draw(GL10 gl) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        //mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

//        // Set color for drawing the triangle
        //GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to vertex shader's vPosition member
        mColorHandleList = GLES20.glGetAttribLocation(mProgram, "a_Color");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mColorHandleList);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mColorHandleList, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mColorHandleList);

    }
}

///*
// * Copyright (C) 2011 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.example.android.opengl;
//
//        import java.nio.ByteBuffer;
//        import java.nio.ByteOrder;
//        import java.nio.FloatBuffer;
//        import java.nio.ShortBuffer;
//
//        import android.opengl.GLES20;
//
///**
// * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
// */
//public class Square {
//
//    private final String vertexShaderCode =
//            // This matrix member variable provides a hook to manipulate
//            // the coordinates of the objects that use this vertex shader
//            "uniform mat4 uMVPMatrix;" +
//                    "attribute vec4 vPosition;" +
//                    "void main() {" +
//                    // The matrix must be included as a modifier of gl_Position.
//                    // Note that the uMVPMatrix factor *must be first* in order
//                    // for the matrix multiplication product to be correct.
//                    "  gl_Position = uMVPMatrix * vPosition;" +
//                    "}";
//
//    private final String fragmentShaderCode =
//            "precision mediump float;" +
//                    "uniform vec4 vColor;" +
//                    "void main() {" +
//                    "  gl_FragColor = vColor;" +
//                    "}";
//
//    private final FloatBuffer vertexBuffer;
//    private final ShortBuffer drawListBuffer;
//    private final int mProgram;
//    private int mPositionHandle;
//    private int mColorHandle;
//    private int mMVPMatrixHandle;
//
//    // number of coordinates per vertex in this array
//    static final int COORDS_PER_VERTEX = 3;
//    static float squareCoords[] = {
//            -0.5f,  0.5f, 0.0f,   // top left
//            -0.5f, -0.5f, 0.0f,   // bottom left
//            0.5f, -0.5f, 0.0f,   // bottom right
//            0.5f,  0.5f, 0.0f }; // top right
//
//    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
//
//    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
//
//    float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };
//
//    /**
//     * Sets up the drawing object data for use in an OpenGL ES context.
//     */
//    public Square() {
//        // initialize vertex byte buffer for shape coordinates
//        ByteBuffer bb = ByteBuffer.allocateDirect(
//                // (# of coordinate values * 4 bytes per float)
//                squareCoords.length * 4);
//        bb.order(ByteOrder.nativeOrder());
//        vertexBuffer = bb.asFloatBuffer();
//        vertexBuffer.put(squareCoords);
//        vertexBuffer.position(0);
//
//        // initialize byte buffer for the draw list
//        ByteBuffer dlb = ByteBuffer.allocateDirect(
//                // (# of coordinate values * 2 bytes per short)
//                drawOrder.length * 2);
//        dlb.order(ByteOrder.nativeOrder());
//        drawListBuffer = dlb.asShortBuffer();
//        drawListBuffer.put(drawOrder);
//        drawListBuffer.position(0);
//
//        // prepare shaders and OpenGL program
//        int vertexShader = MyGLRenderer.loadShader(
//                GLES20.GL_VERTEX_SHADER,
//                vertexShaderCode);
//        int fragmentShader = MyGLRenderer.loadShader(
//                GLES20.GL_FRAGMENT_SHADER,
//                fragmentShaderCode);
//
//        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
//        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
//        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
//        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
//    }
//
//    /**
//     * Encapsulates the OpenGL ES instructions for drawing this shape.
//     *
//     * @param mvpMatrix - The Model View Project matrix in which to draw
//     * this shape.
//     */
//    public void draw(float[] mvpMatrix) {
//        // Add program to OpenGL environment
//        GLES20.glUseProgram(mProgram);
//
//        // get handle to vertex shader's vPosition member
//        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
//
//        // Enable a handle to the triangle vertices
//        GLES20.glEnableVertexAttribArray(mPositionHandle);
//
//        // Prepare the triangle coordinate data
//        GLES20.glVertexAttribPointer(
//                mPositionHandle, COORDS_PER_VERTEX,
//                GLES20.GL_FLOAT, false,
//                vertexStride, vertexBuffer);
//
//        // get handle to fragment shader's vColor member
//        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
//
//        // Set color for drawing the triangle
//        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
//
//        // get handle to shape's transformation matrix
//        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
//        MyGLRenderer.checkGlError("glGetUniformLocation");
//
//        // Apply the projection and view transformation
//        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
//        MyGLRenderer.checkGlError("glUniformMatrix4fv");
//
//        // Draw the square
//        GLES20.glDrawElements(
//                GLES20.GL_TRIANGLES, drawOrder.length,
//                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
//
//        // Disable vertex array
//        GLES20.glDisableVertexAttribArray(mPositionHandle);
//    }
//
//}