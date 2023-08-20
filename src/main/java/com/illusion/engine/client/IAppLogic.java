package com.illusion.engine.client;

import com.illusion.engine.core.WindowProps;

public interface IAppLogic {
    Application parent_app = null;
    void onInit(WindowProps props);
    void onUpdate();
    void onRender();
    void onResize(int w, int h);
    void onDispose();
}
