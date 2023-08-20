package com.illusion.engine.utils;

import com.illusion.engine.client.Application;
import com.illusion.engine.core.Color;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Globals {
    public static final int NUM_COMPONENTS = 1;
    public static int DisplayX, DisplayY;
    public static int HINTS_RESIZABLE,
            HINTS_MAXIMIZED,
            HINTS_DECORATED,
            HINTS_VSYNC;
    public static Color CLEAR_COLOR, RESIZE_COLOR;
    public static int Width, Height;

    static
    {
        Width = 0;
        Height = 0;
        CLEAR_COLOR  = new Color(51, 50, 61);
        RESIZE_COLOR = new Color(1.0f);

        DisplayY = 0;
        DisplayX = 0;

        HINTS_RESIZABLE = 0;
        HINTS_MAXIMIZED = 1;
        HINTS_DECORATED = 2;
        HINTS_VSYNC     = 3;
    }

    public static void sendResizeCall(int x, int y) {
        glfwSetWindowSize(getGLContext(), x, y);
        centreWindow();
    }

    public static long getGLContext() { return glfwGetCurrentContext(); }

    public static void centreWindow() {
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(getGLContext(), pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    getGLContext(),
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically
    }

    public static void moveWindow(int dx, int dy) {
        int xPos[] = {0}, yPos[] = {0};
        glfwGetWindowPos(getGLContext(), xPos, yPos);
        System.out.println(xPos[0] + dx + ", " +  yPos[0] + dy);
        glfwSetWindowPos(getGLContext(), xPos[0] + dx, yPos[0] + dy);
    }

    public static void sendQuitCall() {
        glfwSetWindowShouldClose(getGLContext(), true);
    }
}
