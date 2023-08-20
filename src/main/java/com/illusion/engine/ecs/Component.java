package com.illusion.engine.ecs;

public abstract class Component {
    protected Entity entity;

    public Component() {

    }

    public void Update() {

    }

    public Entity getEntity() {
        return entity;
    }

    public void init() {

    }
}
