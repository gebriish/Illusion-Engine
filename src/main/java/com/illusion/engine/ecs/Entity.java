package com.illusion.engine.ecs;

public class Entity {

    private int m_ID;
    private int m_Layer;
    private boolean m_Static;
    private String m_Name;
    private Transform m_Tranform;
    private ComponentList m_Components;


    public Entity(String name) {
        this.m_Name = name;
        this.m_Components = new ComponentList(8, this);
        this.m_Layer = 0;
        this.m_Tranform = new Transform();
    }

    public Transform transform() {
        return m_Tranform;
    }
    public ComponentList components() {
        return m_Components;
    }

    public String name() {
        return m_Name;
    }

    public int id() {
        return m_ID;
    }

    public int layer() {
        return m_Layer;
    }

    public void setID(int i) {
        m_ID = i;
    }

    public void init() {
        m_Components.init();
    }

    public boolean isStatic() {
        return m_Static;
    }

    public void setStatic(boolean value)
    {
        m_Static = value;
    }

    public void Update() {
        m_Components.update();
    }

    @Override
    public String toString() {
        return name() + " : (" + id() + ")";
    }
}
