package com.example.wehao.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private final static int POSITION_COMPONENT_COUNT = 2;
    private final static int COLOR_COMPONENT_COUNT = 3;
    private final static int BYTES_PER_FLOAT = 4;
    private FloatBuffer vertexData;
    private final Context mContext;
    private int mProgram;
    private final static String A_POSITION = "a_Position";
    private final static String A_COLOR = "a_Color";
    private final static String U_MATRIX = "u_Matrix";
    private int aPositionLocation;
    private int aColorLocation;
    private int uMatrixLocation;
    private int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private float[] matrix = new float[16];

    public AirHockeyRenderer(Context context) {
        mContext = context;

        float[] tableVertices = {
                // Order of coordinates: X, Y, R, G, B
                // Triangle Fan
                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                // Line 1
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 1f, 0f, 0f,
                // Mallets
                0f, -0.4f, 0f, 0f, 1f,
                0f, 0.4f, 1f, 0f, 0f
        };

        vertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(tableVertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShaderResource = TextResourceReader.readTextFileFromResource(mContext,
                R.raw.simple_vertex_shaders);
        String fragmentShaderResource = TextResourceReader.readTextFileFromResource(mContext,
                R.raw.simple_fragment_shaders);
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderResource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderResource);
        mProgram = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        ShaderHelper.validateProgram(mProgram);

        glUseProgram(mProgram);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        aColorLocation = glGetAttribLocation(mProgram, A_COLOR);
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false,
                STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false,
                STRIDE, vertexData);
        glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        final float aspectRatio = width > height? (float) width/(float) height :
                (float) height/(float) width;
        if (width > height) {
            orthoM(matrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            orthoM(matrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        glDrawArrays(GL_LINES, 6, 2);

        glDrawArrays(GL_POINTS, 8, 1);

        glDrawArrays(GL_POINTS, 9, 1);
    }
}
