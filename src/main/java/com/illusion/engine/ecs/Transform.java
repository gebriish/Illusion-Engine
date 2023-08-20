package com.illusion.engine.ecs;

import com.illusion.engine.client.Debug;
import com.illusion.engine.core.Vec2;
import org.lwjgl.system.windows.POINT;

public class Transform {
    private Vec2 position, right, up, scale;
    private float rotation;

    public Transform() {
        init(new Vec2(0));
    }

    public void init(Vec2 pos) {
        this.position = pos;
        this.right = new Vec2(1, 0);
        this.up = new Vec2(0);
        this.rotation = 0;
        this.scale = new Vec2(1.0f);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setScale(float s){
        this.setScale(s, s);
    }

    public void setScale(float x, float y) {
        this.scale.set(x, y);
    }
    public void rotate(float dr)  {
        this.rotation += dr;
    }

    public Vec2 Position() {
        return position;
    }

    public Vec2 Right() {
        return right;
    }

    public Vec2 Up() {
        return up;
    }

    public float Rotation() {
        return rotation;
    }
    public Vec2 Scale() {
        return scale;
    }

    public void copy(Transform t) {
        t.position = this.position;
        t.rotation = this.rotation;
        t.scale = this.scale;
    }

    public boolean isEqualTo(Transform t) {
        return this.position.equals(t.position) && this.rotation == t.rotation && this.scale == t.scale;
    }

    public boolean withinBounds(Vec2 point)
    {
        float x1, y1, x2, y2;
        x1 = this.position.x - this.scale.x/2f;
        y1 = this.position.y - this.scale.y/2f;

        x2 = this.position.x + this.scale.x/2f;
        y2 = this.position.y + this.scale.y/2f;

        return point.x >= x1 && point.x <= x2 && point.y >= y1 && point.y <= y2;
    }

    public void lerpScale(float xScale, float yScale, float t) {
        this.scale.x = (this.scale.x * (1 - t)) + (xScale * t);
        this.scale.y = (this.scale.y * (1 - t)) + (yScale * t);
    }
}
