package com.illusion.engine.graphics;

import com.illusion.engine.core.Vec2;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20C.*;

public class Shader {

    private final int m_ID;

    public Shader(String vSource, String fSource)
    {
        int vertexID, fragmentID;
        // First load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(vertexID, vSource);
        glCompileShader(vertexID);

        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: \n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // First load and compile the vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(fragmentID, fSource);
        glCompileShader(fragmentID);

        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR:\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders and check for errors
        m_ID = glCreateProgram();
        glAttachShader(m_ID, vertexID);
        glAttachShader(m_ID, fragmentID);
        glLinkProgram(m_ID);

        success = glGetProgrami(m_ID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(m_ID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR:\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(m_ID, len));
            assert false : "";
        }
    }

    public void UploadMat4f(String varName, Matrix4f mat4){
        int varlocation = glGetUniformLocation(m_ID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varlocation, false , matBuffer);
    }

    public void UploadVec2f(String varName, Vec2 value) {
        int varLocation = glGetUniformLocation(m_ID, varName);
        glUniform2f(varLocation, value.x, value.y);
    }

    public void UploadFloat(String varName , float value){
        int varLocation = glGetUniformLocation(m_ID , varName);
        glUniform1f(varLocation , value);
    }
    public void UploadInt(String varName , int value){
        int varLocation = glGetUniformLocation(m_ID, varName);
        glUniform1i(varLocation , value);
    }
    public void UploadTexture(String varName , int slot){
        int varLocation = glGetUniformLocation(m_ID, varName);
        glUniform1i(varLocation , slot);
    }

    public void UploadIntArray(String varName, int[] value) {
        int varLocation = glGetUniformLocation(m_ID, varName);
        glUniform1iv(varLocation, value);
    }

    public static void unbind() {
        glUseProgram(0);
    }

    public void bind() {
        glUseProgram(m_ID);
    }

    public void dispose() {
        glDeleteProgram(m_ID);
    }

}
