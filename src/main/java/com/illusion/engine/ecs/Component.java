package com.illusion.engine.ecs;

public abstract class Component {
    protected Entity entity;

    public Component() {

    }

    public void init() {

    }

    public void update() {

    }

    public Entity getEntity() {
        return entity;
    }


}
