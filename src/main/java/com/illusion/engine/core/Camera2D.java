package com.illusion.engine.core;

import com.illusion.engine.utils.Globals;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import static java.lang.Math.floor;

public class Camera2D {
    private Vec2 position, up, right;
    private float zPos, scale;
    private float width, height;
    private Matrix4f matrix, invMat;

    public Camera2D() {
        position = new Vec2(0.0f);
        scale = 1.0f;
        zPos = 4;
        matrix = new Matrix4f().identity();
        invMat = new Matrix4f().identity();
        up = new Vec2(.0f, 1.0f);
        right = new Vec2(1.0f, .0f);

        width  = Globals.Width;
        height = Globals.Height;
    }

    public void update() {
        if(scale <= 0.01f)
            scale = .01f;

        matrix.identity();
        calculateProjection();
        calculateView();

        matrix.invert(invMat);
    }

    public void resize(int nW, int nH) {
        width = (nW);
        height = (nH);
    }
    private void calculateView() {
        float xPos = position.x;
        float yPos = position.y;
        matrix.lookAt(xPos, yPos, zPos, xPos, yPos, zPos/Math.abs(zPos), up.x, up.y, .0f);
    }

    private void calculateProjection() {
        matrix.ortho(-(int) (width/2) * scale, (int)(width/2) * scale, -(int) (height/2) * scale, (int)(height/2) * scale, .001f, 100f);
    }

    public Matrix4f getMatrix() {
        return matrix;
    }

    public Vec2 getRight() {
        return right;
    }
    public Vec2 getUp() {
        return up;
    }

    public float getScale() {
        return this.scale;
    }


    public Vec2 getPosition() {
        return position;
    }

    public void setScale(float val) {
        this.scale = val;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public Vec2 ScreenToWorld(Vec2 ScrPos) {
        Vector4f vec4 = new Vector4f(ScrPos.x, ScrPos.y, .0f, 1.0f);
        vec4.mul(invMat);
        return new Vec2(vec4.x, vec4.y);
    }
}
