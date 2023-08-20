package com.illusion.game.scenes;

import com.illusion.engine.client.Debug;
import com.illusion.engine.client.KeyListener;
import com.illusion.engine.core.SpriteSheet;
import com.illusion.engine.ecs.CSpriteRenderer;
import com.illusion.engine.ecs.Entity;
import com.illusion.engine.ecs.Scene;
import com.illusion.engine.graphics.Renderer;
import com.illusion.engine.utils.FileManager;
import com.illusion.engine.utils.Globals;
import com.illusion.engine.core.Time;
import com.illusion.engine.core.Vec2;
import com.illusion.game.IGameLogic;
import com.illusion.game.tech.CPlayerLocomotion;
import com.illusion.game.tech.TileLevelMaker;


import static org.lwjgl.glfw.GLFW.*;

public class DemoScene extends Scene {

    private final float cameraSpeed = 256;
    private Vec2 rawPos = new Vec2(0.0f), movementVector = new Vec2(.0f);
    private Entity Player;
    private CPlayerLocomotion locomotion;

    @Override
    public void init() {
        camera.setRotation(0);
        camera.getPosition().set(rawPos);
        camera.setScale(1/4f);
        camera.resize(Globals.Width, Globals.Height);

        Renderer.get().MainCamera = camera;

        Entity[] ens = TileLevelMaker.CreateTileSetFromString("res/levels/01.json");

        for(Entity en : ens)
        {
            instantiate(en);
        }

        Player = new Entity("Player");
        Player.transform().setScale(64, 64);
        CSpriteRenderer spr = new CSpriteRenderer(FileManager.loadTexture("res/images/char_a_p1/human_base_anims.png",
                Renderer.NEAREST_FILTERING, Renderer.MIPMAP_NAN));
        spr.getColor().set("#ffffff");
        SpriteSheet.CalcSpriteUV(spr, 8, 8, 0);
        Player.components().add(spr);
        locomotion = new CPlayerLocomotion();
        Player.components().add(locomotion);

        instantiate(Player);

        Globals.CLEAR_COLOR.set(51, 50, 61);

        super.init();
    }

    @Override
    public void onReload() {
        Globals.CLEAR_COLOR.set(51, 50, 61);
    }

    @Override
    public void update() {
        super.update();
        if(KeyListener.onKeyPressed(GLFW_KEY_RIGHT))
            camera.rotate(-Time.deltaTime * 90);
        if(KeyListener.onKeyPressed(GLFW_KEY_LEFT))
            camera.rotate(Time.deltaTime * 90);
        if(KeyListener.onKeyBegin(GLFW_KEY_ENTER))
            camera.setRotation(0);

        camera.setScale(camera.getScale() + (KeyListener.onKeyPressed(GLFW_KEY_E) ? 1 : (KeyListener.onKeyPressed(GLFW_KEY_Q) ? -1 : 0)) * Time.deltaTime * .2f) ;
        camera.getPosition().lerp(Player.transform().Position().add(locomotion.getMovementVector().scale(20)), Time.deltaTime * 5);
        camera.update();

        if(KeyListener.onKeyBegin(GLFW_KEY_P))
            IGameLogic.loadScene(0);
    }

    @Override
    public void render() {
        Renderer.get().flush();
        Renderer.get().batchScene(this);

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
