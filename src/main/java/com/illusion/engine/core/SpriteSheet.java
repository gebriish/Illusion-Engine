package com.illusion.engine.core;

import com.illusion.engine.ecs.CSpriteRenderer;

public class SpriteSheet {

    public static void CalcSpriteUV(CSpriteRenderer spriteRenderer, int xDiv, int yDiv, int index) {
        float sizeX = 1.0f/xDiv;
        float sizeY = 1.0f/yDiv;

        int xAdd = index % xDiv;
        int yAdd = index / xDiv;

        spriteRenderer.uv1().set(sizeX * xAdd, 1.0f - (sizeY * (yAdd + 1)));
        spriteRenderer.uv2().set(sizeX * (xAdd + 1), 1.0f - (sizeY * (yAdd)));
    }
}
