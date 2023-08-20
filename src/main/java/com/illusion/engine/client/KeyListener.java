package com.illusion.engine.client;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];
    private boolean keyBeginPress[] = new boolean[350];
    private boolean keyEndPress[] = new boolean[350];

    private KeyListener() {
    }

    public static void endFrame() {
        Arrays.fill(get().keyBeginPress, false);
        Arrays.fill(get().keyEndPress, false);
    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
            get().keyBeginPress[key] = true;
            get().keyEndPress[key] = false;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
            get().keyBeginPress[key] = false;
            get().keyEndPress[key] = true;
        }
    }

    public static boolean onKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
    public static boolean onKeyBegin(int keyCode) {
        return get().keyBeginPress[keyCode];
    }
    public static boolean onKeyEnd(int keyCode) {
        return get().keyEndPress[keyCode];
    }
}