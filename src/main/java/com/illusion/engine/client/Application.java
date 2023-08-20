package com.illusion.engine.client;

import com.illusion.engine.core.Window;
import com.illusion.engine.core.WindowProps;
import com.illusion.engine.utils.FileManager;
import com.illusion.engine.utils.Globals;
import com.illusion.engine.core.Time;
import org.lwjgl.glfw.GLFWVidMode;

import static com.illusion.engine.core.Window.clear;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

public class Application implements Runnable {

    public WindowProps window_props;
    public IAppLogic app_logic;
    public Window window;


    public Application(IAppLogic logic) {
        app_logic = logic;
        window_props = new WindowProps();
        window = new Window(window_props);
        app_logic.onInit(window_props);
        window.create();


        glfwSetFramebufferSizeCallback(Globals.getGLContext(), (w, x, y) -> {
                window_props.setResolution(x, y);
                app_logic.onResize(x, y);
                clear(Globals.RESIZE_COLOR);
                glfwSwapBuffers(Globals.getGLContext());
        });


        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        Globals.DisplayY = vidmode.height();
        Globals.DisplayX = vidmode.width();
    }

    @Override
    public void run() {
        while (window.isOpen())
        {
            Time.beginFrame();
            app_logic.onUpdate();
            window.beginFrame();
            app_logic.onRender();

            KeyListener.endFrame();
            MouseListener.endFrame();
            window.endFrame();
            Time.endFrame();
        }

        dispose();
        FileManager.Flush();
        window.dispose();
    }

    public void dispose() {
        app_logic.onDispose();
    }

}
