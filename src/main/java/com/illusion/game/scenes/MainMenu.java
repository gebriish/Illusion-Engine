package com.illusion.game.scenes;

import com.illusion.engine.client.MouseListener;
import com.illusion.engine.core.Camera2D;
import com.illusion.engine.ecs.CSpriteRenderer;
import com.illusion.engine.ecs.Entity;
import com.illusion.engine.ecs.Scene;
import com.illusion.engine.graphics.Renderer;
import com.illusion.engine.utils.FileManager;
import com.illusion.engine.utils.Globals;
import com.illusion.game.IGameLogic;

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

        camera.setScale(0.175f);

        Renderer.get().MainCamera = camera;

        super.init();
    }

    @Override
    public void update(){
        camera.update();

        if(MouseListener.onMousePressed(0))
        {
            if(en1.transform().withinBounds(camera.ScreenToWorld(MouseListener.getPositionVec())))
                en1.components().getComponent(CSpriteRenderer.class).getColor().set(0.2f);
            else if(en2.transform().withinBounds(camera.ScreenToWorld(MouseListener.getPositionVec())))
                en2.components().getComponent(CSpriteRenderer.class).getColor().set(0.2f);
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
                en1.components().getComponent(CSpriteRenderer.class).getColor().set(0.6f);
                en2.components().getComponent(CSpriteRenderer.class).getColor().set(1.0f);
            }
            else if(en2.transform().withinBounds(camera.ScreenToWorld(MouseListener.getPositionVec()))) {
                en1.components().getComponent(CSpriteRenderer.class).getColor().set(1.0f);
                en2.components().getComponent(CSpriteRenderer.class).getColor().set(0.6f);
            }
            else
            {
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
