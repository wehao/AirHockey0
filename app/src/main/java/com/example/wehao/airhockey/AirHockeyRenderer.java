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
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private final static int POSITION_COMPONENT_COUNT = 2;
    private final static int BYTES_PER_FLOAT = 4;
    private FloatBuffer vertexData;
    private final Context mContext;
    private int mProgram;
    private final static String U_COLOR = "u_Color";
    private int uColorLocation;
    private final static String A_POSITION = "a_Position";
    private int aPositionLocation;

    public AirHockeyRenderer(Context context) {
        mContext = context;

        float[] tableVertices = {
                // Triangle 1
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,
                // Triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,
                // Line 1
                -0.5f, 0f,
                0.5f, 0f,
                // Mallets
                0f, -0.25f,
                0f, 0.25f,
                //Triangle3
                -0.5f, 0.25f,
                -0.25f, 0.5f,
                -0.5f, 0.5f,
                //Triangle4
                -0.5f, 0.25f,
                -0.25f, 0.25f,
                -0.25f, 0.5f,
                //Triangle5
                0.25f, 0.25f,
                0.5f, 0.5f,
                0.25f, 0.5f,
                //Triangle6
                0.25f, 0.25f,
                0.5f, 0.25f,
                0.5f, 0.5f
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

        uColorLocation = glGetUniformLocation(mProgram, U_COLOR);
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false,
                0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 6);

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 10, 6);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 16, 6);
    }
}
