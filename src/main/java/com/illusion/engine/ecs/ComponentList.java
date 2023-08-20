package com.illusion.engine.ecs;

import com.illusion.engine.client.Debug;
import com.illusion.engine.client.LogLevel;
import com.illusion.engine.utils.Globals;

import java.util.Arrays;

public class ComponentList {

    private Component[] componentArray;
    private int max;
    private int stackTop;

    private boolean[] hasComponentList;
    private final Entity entity;

    public ComponentList(int maxSize, Entity parent) {
        this.entity = parent;
        max = maxSize;
        componentArray = new Component[maxSize];
        stackTop = 0;
        hasComponentList = new boolean[Globals.NUM_COMPONENTS];
        Arrays.fill(hasComponentList, false);
    }

    public void add(Component comp) {
        if(stackTop < max){
            componentArray[stackTop] = comp;
            componentArray[stackTop].entity = this.entity;
            stackTop++;
        }else{
            max = (int) (max * 1.5f);
            Component[] newArray = new Component[max];
            System.arraycopy(componentArray, 0, newArray, 0, componentArray.length);
            componentArray = newArray;
            add(comp);
        }
    }

    public <T extends Component> T getComponent(Class<T> ComponentClass) {
        if(stackTop > 0) {
            for (Component c : componentArray) {
                if (ComponentClass.isAssignableFrom(c.getClass())) {
                    try {
                        return ComponentClass.cast(c);
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public void clear() {
        componentArray = new Component[1];
        max = 1;
        stackTop = 0;
    }

    public void debugInfo() {
        Debug.Log("Max: " + max);
        Debug.Log("StackTop: " + stackTop);
        for(int i=0; i<stackTop; i++)
            Debug.Log(LogLevel.INFO, i + ": " + componentArray[i].getClass().getSimpleName());
        Debug.Log("---");
    }

    public void init() {
        for(int i=0; i<stackTop; i++)
            componentArray[i].init();
    }

    public void update() {
        for(int i=0; i<stackTop; i++)
            componentArray[i].Update();
    }
}
