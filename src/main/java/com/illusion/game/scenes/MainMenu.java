package com.illusion.game.scenes;

import com.illusion.engine.client.MouseListener;
import com.illusion.engine.core.Camera2D;
import com.illusion.engine.core.Time;
import com.illusion.engine.ecs.CSpriteRenderer;
import com.illusion.engine.ecs.Entity;
import com.illusion.engine.ecs.Scene;
import com.illusion.engine.graphics.Renderer;
import com.illusion.engine.utils.FileManager;
import com.illusion.engine.utils.Globals;
import com.illusion.game.IGameLogic;

import static org.lwjgl.opengl.GL11C.glClearColor;

public class MainMenu extends Scene {

    private Entity en1, en2;

    @Override
    public void init() {

        en1 = loadEntityFromTexture(FileManager.loadTexture("res/images/startbutton.png", Renderer.NEAREST_FILTERING, Renderer.MIPMAP_NAN));
        en1.transform().Position().set(0,  8);
        instantiate(en1);

        en2 = loadEntityFromTexture(FileManager.loadTexture("res/images/quitbutton.png", Renderer.NEAREST_FILTERING, Renderer.MIPMAP_NAN));
        en2.transform().Position().set(0, -8);
        instantiate(en2);

        camera.setScale(0.2f);

        Renderer.get().MainCamera = camera;
        Globals.CLEAR_COLOR.set(0.1f);

        super.init();
    }

    @Override
    public void onReload() {
        Globals.CLEAR_COLOR.set(0.1f);
    }

    @Override
    public void update(){
        camera.update();

        if(MouseListener.onMousePressed(0))
        {
            if(en1.transform().withinBounds(camera.ScreenToWorld(MouseListener.getPositionVec())))
            {
                en1.transform().lerpScale(60, 14, Time.deltaTime * 20f);
                en1.components().getComponent(CSpriteRenderer.class).getColor().set(0.2f);
            }
            else if(en2.transform().withinBounds(camera.ScreenToWorld(MouseListener.getPositionVec())))
            {
                en2.transform().lerpScale(60, 14, Time.deltaTime * 20f);
                en2.components().getComponent(CSpriteRenderer.class).getColor().set(0.2f);
            }
        }else
        {
            if(MouseListener.onMouseEnd(0))
            {
                if(en1.transform().withinBounds(camera.ScreenToWorld(MouseListener.getPositionVec())))
                    IGameLogic.loadScene(1);
                else if(en2.transform().withinBounds(camera.ScreenToWorld(MouseListener.getPositionVec())))
                    Globals.sendQuitCall();
            }

            if(en1.transform().withinBounds(camera.ScreenToWorld(MouseListener.getPositionVec())))
            {
                en1.transform().lerpScale(68, 18, Time.deltaTime * 20f);
                en2.transform().lerpScale(64, 16, Time.deltaTime * 20f);
            }
            else if(en2.transform().withinBounds(camera.ScreenToWorld(MouseListener.getPositionVec()))) {
                en2.transform().lerpScale(68, 18, Time.deltaTime * 20f);
                en1.transform().lerpScale(64, 16, Time.deltaTime * 20f);
            }
            else
            {
                en1.transform().lerpScale(64, 16, Time.deltaTime * 20f);
                en2.transform().lerpScale(64, 16, Time.deltaTime * 20f);

                en1.components().getComponent(CSpriteRenderer.class).getColor().set(1.0f);
                en2.components().getComponent(CSpriteRenderer.class).getColor().set(1.0f);
            }
        }

        super.update();
    }

    @Override
    public void render() {
        Renderer.get().flush();
        Renderer.get().batchScene(this);

        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void onResize(int w, int h)
    {
        camera.resize(w, h);
        super.onResize(w, h);
    }
}
