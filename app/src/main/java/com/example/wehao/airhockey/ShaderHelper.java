package com.example.wehao.airhockey;

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String code) {
        return compileShader(GL_VERTEX_SHADER, code);
    }

    public static int compileFragmentShader(String code) {
        return compileShader(GL_FRAGMENT_SHADER, code);
    }

    private static int compileShader(int shaderType, String code) {
        final int shaderObjectId = glCreateShader(shaderType);
        if (shaderObjectId == 0) {
            Log.w(TAG, "glCreateShader failed");
            return 0;
        }

        glShaderSource(shaderObjectId, code);
        glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);
        Log.e(TAG, "compileShader: " + code + " " + glGetShaderInfoLog(shaderObjectId));
        if (compileStatus[0] == 0) {
            glDeleteShader(shaderObjectId);
            Log.e(TAG, "compileShader: Compilation of shader failed");
            return 0;
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            Log.e(TAG, "linkProgram: glCreateProgram failed");
            return 0;
        }

        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);
        glLinkProgram(programObjectId);
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        Log.e(TAG, "linkProgram: " + glGetProgramInfoLog(programObjectId));
        if (linkStatus[0] == 0) {
            Log.e(TAG, "linkProgram: failed");
            return 0;
        }
        return programObjectId;
    }

    public static boolean validateProgram(int programId) {
        glValidateProgram(programId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programId, GL_VALIDATE_STATUS, validateStatus, 0);

        return validateStatus[0] != 0;
    }
}
