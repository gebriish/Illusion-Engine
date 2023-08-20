package com.illusion.engine.core;

import com.illusion.engine.client.KeyListener;
import com.illusion.engine.client.MouseListener;
import com.illusion.engine.utils.Globals;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static com.illusion.engine.utils.Globals.CLEAR_COLOR;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private long m_Pointer;
    private WindowProps m_props;
    public Window(WindowProps properties) {
        m_props = properties;
    }

    public void create() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        //configure glfw
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, m_props.getWindowHints(Globals.HINTS_RESIZABLE) ? 1 : 0);
        glfwWindowHint(GLFW_MAXIMIZED, m_props.getWindowHints(Globals.HINTS_MAXIMIZED) ? 1 : 0);
        glfwWindowHint(GLFW_DECORATED, m_props.getWindowHints(Globals.HINTS_DECORATED) ? 1 : 0);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        m_Pointer = glfwCreateWindow(m_props.getWidth(), m_props.getHeight(), m_props.getTitle(), NULL, NULL);

        if(m_Pointer == NULL)
            throw new RuntimeException("Failed to create GLFW window");
        glfwMakeContextCurrent(m_Pointer);

        Globals.centreWindow();



        glfwSetCursorPosCallback(m_Pointer, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(m_Pointer, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(m_Pointer, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(m_Pointer, KeyListener::keyCallback);

        glfwSwapInterval(m_props.getWindowHints(Globals.HINTS_VSYNC) ? 1 : 0);
        glfwShowWindow(m_Pointer);
        GL.createCapabilities();

        glfwSwapInterval(m_props.getWindowHints(Globals.HINTS_VSYNC) ? 1 : 0);
    }

    public boolean isOpen() {
        return !glfwWindowShouldClose(m_Pointer);
    }

    public void beginFrame() {
        setViewport(m_props.getWidth(), m_props.getHeight());
        clear(CLEAR_COLOR);
    }

    public static void setViewport(int x, int y) {
        glViewport(0, 0, x, y);
    }

    public static void clear(Color clearColor) {
        glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void endFrame() {
        glfwSwapBuffers(m_Pointer);
        glfwPollEvents();
    }

    public int getHeight() {
        return m_props.getHeight();
    }

    public int getWidth() {
        return m_props.getWidth();
    }

    public void dispose() {
        glfwDestroyWindow(m_Pointer);
        glfwTerminate();
    }
}
