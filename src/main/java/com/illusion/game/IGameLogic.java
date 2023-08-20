package com.illusion.game;

import com.illusion.game.scenes.DemoScene;
import com.illusion.engine.client.IAppLogic;
import com.illusion.engine.core.*;
import com.illusion.engine.ecs.Scene;
import com.illusion.engine.graphics.Renderer;
import com.illusion.engine.utils.Globals;
import com.illusion.game.scenes.MainMenu;
import com.illusion.game.tech.ImGuiLayer;

import static org.lwjgl.glfw.GLFW.*;

public class IGameLogic implements IAppLogic {

    private boolean isFirstFrame = true;
    private static Scene[] m_Scenes = new Scene[2];
    private static int activeScene = 0;
    private ImGuiLayer imGuilayer;

    public static void loadScene(int i) {
        Renderer.get().MainCamera = m_Scenes[i].getCamera();
        activeScene = i;
        m_Scenes[activeScene].onResize(Globals.Width, Globals.Height);
    }


    @Override
    public void onInit(WindowProps props) {
        Globals.CLEAR_COLOR.set(51, 50, 61);
        props.setTitle("Mirage");
        props.setResolution(1280, 760);
        props.setWindowHint(Globals.HINTS_DECORATED, true);
        props.setWindowHint(Globals.HINTS_RESIZABLE, true);
    }

    @Override
    public void onUpdate() {
        if(isFirstFrame){   // gl specific initializations
            isFirstFrame = false;
            init();
            Renderer.get().init();
        }
        update();
    }

    @Override
    public void onRender() {
        render();

        imGuilayer.render();
    }

    @Override
    public void onResize(int w, int h) {
        Renderer.get().resize(w, h);
        m_Scenes[activeScene].onResize(w, h);
    }

    @Override
    public void onDispose() {
        imGuilayer.dispose();
        dispose();
    }

    private void init() {
        m_Scenes[0] = new MainMenu();
        m_Scenes[1] = new DemoScene();
        activeScene = 0;

        m_Scenes[activeScene].init();
        imGuilayer = new ImGuiLayer();
        imGuilayer.init();
    }


    private void update() {
        m_Scenes[activeScene].update();
    }

    private void render() {
        m_Scenes[activeScene].render();
    }

    private void dispose() {
        m_Scenes[activeScene].dispose();
    }

}
