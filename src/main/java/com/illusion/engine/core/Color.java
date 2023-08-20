package com.illusion.engine.core;

public class Color {
    public float r, g, b, a;

    public Color() {
        r = 0;
        g = 0;
        b = 0;
        a = 0;
    }

    public Color(float v) {
        r  = v;
        g =  v;
        b =  v;
        a = 1.0f;
    }

    public Color(float rV, float gV, float bV, float aV) {
        this.r = rV;
        this.g = gV;
        this.b = bV;
        this.a = aV;
    }

    public Color(float rV, float gV, float bV) {
        this.r = rV;
        this.g = gV;
        this.b = bV;
        this.a = 1.0f;
    }

    public void set(float v) {
        this.set(v, v, v, 1.0f);
    }

    public void set(String hexCode) {
        if(hexCode.contains("#"))
        {
            hexCode = hexCode.replace("#", "");
        }

        int rR = Integer.valueOf(hexCode.substring(0, 2), 16);
        int rG = Integer.valueOf(hexCode.substring(2, 4), 16);
        int rB = Integer.valueOf(hexCode.substring(4, 6), 16);

        this.r = rR/256.0f;
        this.g = rG/256.0f;
        this.b = rB/256.0f;
    }

    public void set(int rV, int gV, int bV) {
        set(rV/256.0f, gV/256.0f, bV/256.0f);
    }

    public void set(float rV, float gV,float bV) {
        this.set(rV, gV, bV, 1.0f);
    }

    public void set(float rV, float gV, float bV, float aV) {
        this.r = rV;
        this.g = gV;
        this.b = bV;
        this.a = aV;
    }
}
