package com.illusion.engine.graphics;

import com.illusion.engine.client.Debug;
import com.illusion.engine.client.LogLevel;
import com.illusion.engine.core.Time;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.*;

public class StaticMesh {

    private int m_vaoID, m_vboID;
    private int m_vertexCount, m_VertexSize;
    private int[] m_VertexFormat;



    public StaticMesh(float[] vertexarray, int[] vformat) {
        float beginTime = Time.getNanoTime();

        m_VertexFormat = vformat;
        m_VertexSize = sum_array(vformat.length, vformat);
        m_vertexCount = vertexarray.length/m_VertexSize;

        m_vaoID = glGenVertexArrays();
        glBindVertexArray(m_vaoID);

        m_vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, m_vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexarray, GL_STATIC_DRAW);

        for(int i=0; i<vformat.length; i++)
        {
            glVertexAttribPointer(i, vformat[i], GL_FLOAT, false, m_VertexSize * Float.BYTES, (long) sum_array(i, vformat) * Float.BYTES);
        }

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        Debug.Log(LogLevel.INFO, "Mesh generation time: " + (Time.getNanoTime() - beginTime));
    }

    public void draw(int drawmode) {
        glBindVertexArray(m_vaoID);
        for(int i=0; i < m_VertexFormat.length; i++)
        {
            glEnableVertexAttribArray(i);
        }
        glDrawArrays(drawmode, 0, m_vertexCount);
    }

    private int sum_array(int lim, int[] arr) {
        int sum = 0;
        for(int i=0; i<lim; i++)
        {
            sum += arr[i];
        }
        return sum;
    }

    public void dispose() {
        glDeleteBuffers(m_vboID);
        glDeleteVertexArrays(m_vaoID);
    }
}
