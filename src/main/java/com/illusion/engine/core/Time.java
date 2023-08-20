package com.illusion.engine.core;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time {
    public static float deltaTime = 0.0f;

    private static float beginTime = 0.0f;

    public static void beginFrame() {
        beginTime = getNanoTime();
    }

    public static void endFrame() {
        deltaTime = getNanoTime() - beginTime;
    }

    public static float getNanoTime() {
        return (float) glfwGetTime();
    }
}
