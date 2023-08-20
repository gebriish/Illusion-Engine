package com.illusion.engine.ecs;

import com.illusion.engine.graphics.Texture;
import com.illusion.engine.core.Color;
import com.illusion.engine.core.Vec2;

public class CSpriteRenderer extends Component {
    private Texture texture;
    private Color color;
    private Vec2 uv1, uv2;
    private Transform refTransform;

    public CSpriteRenderer() {
        uv1 = new Vec2(0);
        uv2 = new Vec2(1);
        color = new Color(1.0f);
        texture = null;
    }

    public CSpriteRenderer(Color col) {
        color = col;
        texture = null;
        uv1 = new Vec2(0);
        uv2 = new Vec2(1);
    }

    public CSpriteRenderer(Texture tex) {
        this.texture = tex;
        color = new Color(1.0f);
        uv1 = new Vec2(0);
        uv2 = new Vec2(1);
    }

    public Transform getRefTransform() {
        return refTransform;
    }

    public Texture texture() {
        return texture;
    }

    public Vec2 uv1() {
        return uv1;
    }

    public Vec2 uv2() {
        return uv2;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void update() {
    }

    @Override
    public void init() {
        this.refTransform = new Transform();
        entity.transform().copy(refTransform);
    }
}
