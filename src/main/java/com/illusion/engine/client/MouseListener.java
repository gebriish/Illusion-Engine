package com.illusion.engine.client;

import com.illusion.engine.core.Vec2;
import com.illusion.engine.utils.Globals;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static MouseListener instance;
    private double scrollX, scrollY;
    private int xPos, yPos, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[9];
    private boolean mouseButtonBegin[] = new boolean[9];
    private boolean mouseButtonEnd[] = new boolean[9];
    private boolean isDragging;
    private int mouseButtonDown = 0;

    private Vec2 posVec;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
        this.posVec = new Vec2(0.0f);
    }

    public static void endFrame() {
        Arrays.fill(get().mouseButtonBegin, false);
        Arrays.fill(get().mouseButtonEnd, false);

        get().scrollY = 0.0;
        get().scrollX = 0.0;
        get().lastX = getX();
        get().lastY = getY();
    }

    public static void clear() {
        get().scrollX = 0.0;
        get().scrollY = 0.0;
        get().xPos = 0;
        get().yPos = 0;
        get().mouseButtonDown = 0;
        get().isDragging = false;
        Arrays.fill(get().mouseButtonPressed, false);
    }

    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
        if (get().mouseButtonDown > 0) {
            get().isDragging = true;
        }
        get().xPos = (int) xpos;
        get().yPos = (int) ypos;

        get().posVec.set(((float) get().xPos/Globals.Width) - 0.5f, (((float) get().yPos/Globals.Height) - 0.5f) * -1);
        get().posVec.scaleSet(2.0f);
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().mouseButtonDown++;

            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
                get().mouseButtonBegin[button] = true;
                get().mouseButtonEnd[button] = false;
            }
        } else if (action == GLFW_RELEASE) {
            get().mouseButtonDown--;

            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().mouseButtonBegin[button] = false;
                get().mouseButtonEnd[button] = true;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static int getX() {
        return get().xPos;
    }

    public static int getY() {
        return get().yPos;
    }

    public static float getScrollX() { return (float)get().scrollX; }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean isDragging() { return get().isDragging; }

    public static boolean onMousePressed(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }

    public static boolean onMouseBegin(int button) {
        if (button < get().mouseButtonBegin.length) {
            return get().mouseButtonBegin[button];
        } else {
            return false;
        }
    }

    public static boolean onMouseEnd(int button) {
        if (button < get().mouseButtonEnd.length) {
            return get().mouseButtonEnd[button];
        } else {
            return false;
        }
    }

    public static int getDX() {
        return (int) (getX() - get().lastX);
    }

    public static int getDY() {
        return (int) (getY() - get().lastY);
    }

    public static Vec2 getPositionVec() {
        return get().posVec;
    }
}