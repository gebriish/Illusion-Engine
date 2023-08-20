package com.illusion.engine.graphics;

import com.illusion.engine.core.Vec2;
import com.illusion.engine.ecs.CSpriteRenderer;
import com.illusion.engine.core.Color;
import com.illusion.engine.utils.FileManager;

import java.util.ArrayList;
import java.util.List;

import static org.joml.Math.cos;
import static org.joml.Math.sin;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
    //vertex format
    //  pos                     color                           tex-Coordinates
    //  float, float,          float, float, float, float       float, float

    private final int POS_SIZE = 2;
    private final int COL_SIZE = 4;
    private final int TEX_COORD_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COL_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORD_OFFSET = COL_OFFSET + COL_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORD_OFFSET + TEX_COORD_SIZE * Float.BYTES;
    private final int VERT_SIZE = 9;
    private final int VERT_SIZE_IN_BYTES = VERT_SIZE * Float.BYTES;

    private CSpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private List<Texture> textures;
    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    private boolean wireframe = false;

    public RenderBatch(int batchSize) {
        shader = FileManager.loadShader("res/shaders/default");

        this.sprites = new CSpriteRenderer[batchSize];
        this.maxBatchSize = batchSize;

        vertices = new float[maxBatchSize * 4 * VERT_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;

        this.textures = new ArrayList<>();

    }

    public void addSpriteRenderer(CSpriteRenderer spr) {
        if(hasRoom) {
            int index = this.numSprites;
            this.sprites[index] = spr;
            this.numSprites++;

            if(spr.texture() != null)
                if(!textures.contains(spr.texture())){
                    textures.add(spr.texture());
                }

            loadVertexProperties(index);

            if (numSprites >= this.maxBatchSize)
                this.hasRoom = false;
        }
    }

    private void loadVertexProperties(int index) {
        CSpriteRenderer spr = this.sprites[index];

        int offset = index  * 4 * VERT_SIZE;
        Color col = spr.getColor();

        int texID = 0;
        if(spr.texture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if(textures.get(i) == spr.texture()) {
                    texID = i + 1;
                    break;
                }
            }
        }

        float rotation = spr.getEntity().transform().Rotation();

        float xAdd = 0.5f;
        float yAdd = 0.5f;
        float texX = spr.uv2().x, texY = spr.uv2().y;

        for(int i=0; i<4; i++){

            switch (i)
            {
                case 1->{
                    yAdd = -.5f;
                    texY = spr.uv1().y;
                }
                case 2->{
                    xAdd = -.5f;
                    texX = spr.uv1().x;
                }
                case 3->{
                    yAdd = .5f;
                    texY = spr.uv2().y;
                }
            }

            Vec2 vec = new Vec2(xAdd, yAdd);
            rotate(vec, rotation);

            //position
            vertices[offset] = spr.getEntity().transform().Position().x + (vec.x *  spr.getEntity().transform().Scale().x);
            vertices[offset + 1] = spr.getEntity().transform().Position().y + (vec.y *  spr.getEntity().transform().Scale().y);

            //color
            vertices[offset + 2] = col.r;
            vertices[offset + 3] = col.g;
            vertices[offset + 4] = col.b;
            vertices[offset + 5] = col.a;

            //tex coordinates
            vertices[offset + 6] = texX;
            vertices[offset + 7] = texY;

            // tex id
            vertices[offset + 8] = texID;

            offset += VERT_SIZE;
        }
    }

    public void start() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);

        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int eboID = glGenBuffers();
        int[] indices = genIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERT_SIZE_IN_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COL_SIZE, GL_FLOAT, false, VERT_SIZE_IN_BYTES, COL_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORD_SIZE, GL_FLOAT, false, VERT_SIZE_IN_BYTES, TEX_COORD_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERT_SIZE_IN_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);

    }

    public void render() {

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.bind();
        shader.UploadMat4f("camera", Renderer.get().MainCamera.getMatrix());
        for(int i=0; i<textures.size(); i++)
        {
            textures.get(i).activate(i + 1);
        }
        shader.UploadIntArray("uTextures", texSlots);


        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glDrawElements(Renderer.TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);

        for(int i=0; i<textures.size(); i++)
            Texture.deactivate(i + 1);

        glBindVertexArray(0);
        Shader.unbind();
    }

    private int[] genIndices() {
        //6 indices per quad (3 per tri)
        int[] elements = new int[6 * maxBatchSize];
        for(int i=0; i<maxBatchSize; i++)
        {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int i) {
        int offsetArrayIndex = 6 * i;
        int offset = 4 * i;

        elements[offsetArrayIndex]     = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;


        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;

    }

    public boolean hasRoom() {
        return hasRoom;
    }

    private void rotate(Vec2 vec, float angleDeg) {
        float x = vec.x;
        float y = vec.y;

        float cos = (float)Math.cos(Math.toRadians(angleDeg));
        float sin = (float)Math.sin(Math.toRadians(angleDeg));

        float xPrime = (x * cos) - (y * sin);
        float yPrime = (x * sin) + (y * cos);

        vec.x = xPrime;
        vec.y = yPrime;
    }

}
