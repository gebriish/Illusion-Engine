package com.illusion.engine.graphics;

import com.illusion.engine.core.Camera2D;
import com.illusion.engine.ecs.Entity;
import com.illusion.engine.ecs.CSpriteRenderer;
import com.illusion.engine.ecs.Scene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11C.*;

public class Renderer {
    public final static int MAX_BATCH_SIZE;
    public final static int TRIANGLES, LINES, POINTS;
    public final static int NEAREST_FILTERING, LINEAR_FILTERING;
    public final static int MIPMAP_N_L, MIPMAP_L_N, MIPMAP_N_N, MIPMAP_L_L, MIPMAP_NAN;
    public static final int FILL = GL_FILL,  WIREFRAME = GL_LINE;

    public  Camera2D MainCamera;

    private static List<RenderBatch> batches;
    private static Renderer instance = null;

    public static Renderer get() {
        if(instance == null)
            instance = new Renderer();
        return instance;
    }

    public void init() {
        Shader.unbind();
    }

    public void add(Entity en){
        CSpriteRenderer spr = en.components().getComponent(CSpriteRenderer.class);
        if(spr != null){
            add(spr);
        }
    }

    private void add(CSpriteRenderer spr) {
        boolean added = false;
        for(RenderBatch batch : batches)
        {
            if(batch.hasRoom()) {
                batch.addSpriteRenderer(spr);
                added = true;
                break;
            }
        }

        if(!added)
        {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSpriteRenderer(spr);
        }

    }

    public void resize(int x, int y){

    }



    public void render() {

        for(RenderBatch b : batches)
            b.render();
    }

    static
    {

        batches = new ArrayList<>();
        MAX_BATCH_SIZE = 5000;

        NEAREST_FILTERING = GL_NEAREST;
        LINEAR_FILTERING = GL_LINEAR;

        TRIANGLES = GL_TRIANGLES;
        LINES = GL_LINES;
        POINTS = GL_POINTS;

        MIPMAP_NAN = 0;
        MIPMAP_L_N = 1;
        MIPMAP_L_L = 2;
        MIPMAP_N_L = 3;
        MIPMAP_N_N = 4;
    }

    public void flush() {
        batches.clear();
    }

    public void batchScene(Scene scene) {
        for(int i=0; i<scene.getEntityList().size(); i++)
        {
            add(scene.getEntityList().get(i));
        }
    }
}
