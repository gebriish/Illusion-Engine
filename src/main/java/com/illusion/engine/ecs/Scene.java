package com.illusion.engine.ecs;

import com.illusion.engine.client.Debug;
import com.illusion.engine.client.LogLevel;
import com.illusion.engine.core.Camera2D;
import com.illusion.engine.graphics.Renderer;
import com.illusion.engine.graphics.Texture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Scene {

    private boolean isRunning = false, initialized = false;
    private int EntityCount;
    protected Camera2D camera;
    protected List<Entity> m_Entities;
    protected int m_DynamicEnttCount;

    public Scene() {
        m_Entities = new ArrayList<>();
        camera = new Camera2D();
    }

    public void init() {


        for(Entity en : m_Entities)
        {
            en.init();
        }

        isRunning = true;
        initialized = true;
    }

    public void update() {
        if(!initialized)
            init();

        m_DynamicEnttCount = 0;

        Renderer.get().flush();
        Renderer.get().batchScene(this);

        for(Entity en : m_Entities)
        {
            if(en.isStatic())
                continue;

            ++m_DynamicEnttCount;
            en.Update();
        }
    }


    public void render() {
        //Renderer.get().flush();
        //Renderer.get().batchScene(this);

        Renderer.get().render();
    }

    public void dispose() {
    }

    public void deleteEntity(int index) {
        if(index < EntityCount) {
            for (int i = index + 1; i < EntityCount; i++) {
                int id = m_Entities.get(i).id();
                m_Entities.get(i).setID(--id);
            }

            m_Entities.remove(index);
            EntityCount--;
        }
        else
        {
            Debug.Log(LogLevel.ERROR, index + " is out of bounds of " + this.getClass().getSimpleName() + "'s Entity list");
        }
    }

    public void instantiate(Entity en) {
        if(!m_Entities.contains(en) && en != null) {
            en.setID(EntityCount++);
            en.init();
            m_Entities.add(en);
        }
    }

    public void onResize(int w, int h){
    }

    public Entity loadEntityFromTexture(Texture tex) {
        Entity en = new Entity(tex.name);
        en.transform().setScale(tex.getWidth(), tex.getHeight());
        en.components().add(new CSpriteRenderer(tex));
        return en;
    }

    public List<Entity> getEntityList() {
        return m_Entities;
    }

    public void swapEntities(int i1, int i2) {
        m_Entities.get(i1).setID(i2);
        m_Entities.get(i2).setID(i1);
        Collections.swap(m_Entities, i1, i2);

    }

    public Camera2D getCamera() {
        return camera;
    }
}
