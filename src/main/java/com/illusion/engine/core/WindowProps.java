package com.illusion.engine.core;

import com.illusion.engine.utils.Globals;

import java.util.Arrays;

public class WindowProps {

    private boolean[] m_WindowHints;
    private int m_width, m_height;
    private String m_title;

    public WindowProps() {
        this.m_WindowHints = new boolean[]{false, false, false, false};
        setResolution(800, 800);
        this.m_title = "Default Window";
    }

    public void setWindowHint(int index, boolean value) {
        this.m_WindowHints[index] = value;
    }

    public void setResolution(int x, int y) {
        this.m_width = x;
        Globals.Width = x;
        this.m_height = y;
        Globals.Height = y;
    }

    public void setTitle(String t) {
        this.m_title = t;
    }

    public int getWidth() {
        return m_width;
    }

    public int getHeight() {
        return m_height;
    }

    public String getTitle() {
        return m_title;
    }

    public boolean getWindowHints(int index) {
        return m_WindowHints[index];
    }
}
