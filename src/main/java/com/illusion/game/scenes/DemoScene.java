package com.illusion.game.scenes;

import com.illusion.engine.client.Debug;
import com.illusion.engine.client.KeyListener;
import com.illusion.engine.core.Camera2D;
import com.illusion.engine.ecs.Entity;
import com.illusion.engine.ecs.Scene;
import com.illusion.engine.graphics.Renderer;
import com.illusion.engine.utils.FileManager;
import com.illusion.engine.utils.Globals;
import com.illusion.engine.core.Time;
import com.illusion.engine.core.Vec2;
import com.illusion.game.tech.TileLevelMaker;


import static org.lwjgl.glfw.GLFW.*;

public class DemoScene extends Scene {

    private final float cameraSpeed = 256;
    private Vec2 rawPos = new Vec2(0.0f), movementVector = new Vec2(.0f);
    private Entity[] ens;

    @Override
    public void init() {

        camera.getPosition().set(rawPos);
        camera.setScale(1/4f);
        camera.resize(Globals.Width, Globals.Height);

        Renderer.get().MainCamera = camera;

        ens = TileLevelMaker.CreateTileSetFromString("res/levels/01.json");

        for(Entity en : ens)
        {
            instantiate(en);
        }
        swapEntities(0, 139);
        super.init();
    }

    @Override
    public void update() {

        super.update();

        movementVector.set(.0f);
        movementVector.addSet(camera.getRight().scale((KeyListener.onKeyPressed(GLFW_KEY_D) ? 1 : (KeyListener.onKeyPressed(GLFW_KEY_A) ? -1 : 0))));
        movementVector.addSet(camera.getUp().scale((KeyListener.onKeyPressed(GLFW_KEY_W) ? 1 : (KeyListener.onKeyPressed(GLFW_KEY_S) ? -1 : 0))));
        camera.setScale(camera.getScale() + (KeyListener.onKeyPressed(GLFW_KEY_E) ? 1 : (KeyListener.onKeyPressed(GLFW_KEY_Q) ? -1 : 0)) * Time.deltaTime * .2f) ;
        movementVector.normalize();
        rawPos.addSet(movementVector.scale(Time.deltaTime * cameraSpeed));
        camera.getPosition().lerp(rawPos, Time.deltaTime * 6.0f);
        camera.update();

        if(KeyListener.onKeyBegin(GLFW_KEY_P))
            Debug.Log("Dynamic Entity Count: " + this.m_DynamicEnttCount);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        Renderer.get().flush();
        FileManager.Flush();
        super.dispose();
    }

    @Override
    public void onResize(int w, int h) {
        super.onResize(w, h);
        camera.resize(w, h);
    }
}
