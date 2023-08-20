package com.illusion.game.tech;

import com.illusion.engine.client.KeyListener;
import com.illusion.engine.core.SpriteSheet;
import com.illusion.engine.core.Time;
import com.illusion.engine.core.Vec2;
import com.illusion.engine.ecs.CSpriteRenderer;
import com.illusion.engine.ecs.Component;

import static org.lwjgl.glfw.GLFW.*;

public class CPlayerLocomotion extends Component {

    private CSpriteRenderer m_SpriteRenderer;
    private int index, direction;
    private float animT;
    private Vec2 movementVector;

    public CPlayerLocomotion() {

    }

    @Override
    public void init() {
        movementVector = new Vec2();
        m_SpriteRenderer = this.entity.components().getComponent(CSpriteRenderer.class);
    }

    @Override
    public void update() {
        animT += Time.deltaTime;
        if(animT >= 1/10f)
        {
            animT = 0.0f;
            ++index;
            index = index % 6;
        }

        movementVector.set(0);

        if(KeyListener.onKeyPressed(GLFW_KEY_W))
        {
            movementVector.set(0, 1);
            direction = 40;
        }
        if(KeyListener.onKeyPressed(GLFW_KEY_A))
        {
            movementVector.set(-1, 0);
            direction = 56;
        }
        if(KeyListener.onKeyPressed(GLFW_KEY_S))
        {
            movementVector.set(0,-1);
            direction = 32;
        }
        if(KeyListener.onKeyPressed(GLFW_KEY_D))
        {
            movementVector.set(1, 0);
            direction = 48;
        }

        if(movementVector.length() == 0)
        {
            switch (direction)
            {
                case 56->{
                    direction = 24;
                }
                case 32->{
                    direction = 0;
                }
                case 48->{
                    direction = 16;
                }
                case 40->{
                    direction = 8;
                }
            }
            index = 0;
        }
        SpriteSheet.CalcSpriteUV(m_SpriteRenderer, 8, 8, index + direction);

        this.entity.transform().Position().addSet(movementVector.scale(Time.deltaTime * 60));
    }

    public Vec2 getMovementVector() {
        return movementVector;
    }
}
