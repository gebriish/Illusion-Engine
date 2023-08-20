package com.illusion.engine.core;

import com.illusion.engine.client.Debug;
import com.illusion.engine.client.LogLevel;

import static org.joml.Math.sqrt;

public class Vec2 {
    public float x, y;

    public Vec2(float x1, float y1) {
        x = x1;
        y = y1;
    }

    public Vec2(float d) {
        x = d;
        y = d;
    }

    public Vec2(Vec2 V) {
        x = V.x;
        y = V.y;
    }

    public Vec2() {
         x = 0.0f;
         y = 0.0f;
    }

    public Vec2 add(Vec2 V) {
        Vec2 Vec = new Vec2(this);
        Vec.x += V.x;
        Vec.y += V.y;
        return Vec;
    }

    public Vec2 sub(Vec2 V) {
        Vec2 Vec = new Vec2(this);
        Vec.x -= V.x;
        Vec.y -= V.y;
        return Vec;
    }

    public void addSet(Vec2 V) {
        x += V.x;
        y += V.y;
    }

    public void subSet(Vec2 V) {
        x -= V.x;
        y -= V.y;
    }

    public float getRatio() {
        return x/y;
    }

    public void set(float x1, float y1){
        x = x1;
        y = y1;
    }

    public void set(float d) {
        this.set(d, d);
    }

    public void set(Vec2 vec)
    {
        this.set(vec.x, vec.y);
    }

    public void lerp(Vec2 b, float t) {
        this.x = (this.x * (1-t)) + (b.x * t);
        this.y = (this.y * (1-t)) + (b.y * t);
    }

    public Vec2 scale(float scale) {
        Vec2 vec = new Vec2(this);
        vec.scaleSet(scale);
        return vec;
    }

    public void scaleSet(float scale) {
        this.x *= scale;
        this.y *= scale;
    }

    public float length(){
        float val = (x * x) + (y * y);
        val = sqrt(val);
        return val;
    }

    public void normalize() {
        float len = this.length();
        if(len == 0)
        {
            this.x = 0;
            this.y = 0;
        }else{
            this.x /= len;
            this.y /= len;
        }
    }

    public Vec2 normalized() {
        Vec2 v = new Vec2(this);
        v.normalize();
        return v;
    }

    public void setTheta(float theta) {
        this.set((float) Math.cos(Math.toRadians(theta)), (float) Math.sin(Math.toRadians(theta)));
    }

    public void addSet(float x1, float y1) {
        this.x += x1;
        this.y += y1;
    }


    public boolean equals(Vec2 v) {
        return v.x == x && v.y == y;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
