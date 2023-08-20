package com.illusion.game.tech;

import com.illusion.engine.client.Debug;
import com.illusion.engine.client.LogLevel;
import com.illusion.engine.core.Time;
import com.illusion.engine.utils.FileManager;
import com.illusion.engine.utils.Globals;
import com.illusion.game.IGameLogic;
import imgui.*;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import java.io.File;
import java.util.Objects;

public class ImGuiLayer {
    private final ImGuiImplGlfw imguiGLFW= new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiImplGl3= new ImGuiImplGl3();

    private String glslVersion = null;

    public ImGuiLayer() {
        glslVersion = "#version 130";
    }

    public void init() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);

        if (new File("res/editor/JetBrainsMono-Regular.ttf").isFile()) {
            final ImFontAtlas fontAtlas = io.getFonts();
            final ImFontConfig fontConfig = new ImFontConfig();
            fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());
            fontConfig.setPixelSnapH(true);
            fontAtlas.addFontFromFileTTF("res/editor/JetBrainsMono-Regular.ttf", 18, fontConfig);
            fontConfig.destroy();
        }

        imguiGLFW.init(Globals.getGLContext(), true);
        imGuiImplGl3.init(glslVersion);

        ImGui.getStyle().setWindowRounding(4.0f);
        //setTheme();
    }


    public void render() {
        imguiGLFW.newFrame();
        ImGui.newFrame();
        //ImGui.showDemoWindow();

        EditorUI();

        ImGui.render();
        imGuiImplGl3.renderDrawData(ImGui.getDrawData());
    }

    public void dispose() {
        imGuiImplGl3.dispose();
        imguiGLFW.dispose();
        ImGui.destroyContext();
    }

    //======================================================================//

    private final int WindowFlags = ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove
            | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus |
            ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoFocusOnAppearing
            | ImGuiWindowFlags.NoDocking ;

    private File rootFile = new File("D:/.dev/illusionEngine/release/res/");

    private void EditorUI() {
        ImGui.begin("Main Window", WindowFlags);
        if(ImGui.button("load main menu"))
            IGameLogic.loadScene(0);

        ImGui.setWindowPos(0, 0);
        ImGui.setWindowSize(Globals.Width, Globals.Height);

        ImGui.begin("Performance");
        ImGui.text("FPS: " + (int) (1/Time.deltaTime));
        ImGui.end();

        ImGui.end();

        ImGui.begin("File Explorer");
        if(ImGui.arrowButton("back", 0))
        {
            rootFile = new File(rootFile.getParentFile().getAbsolutePath());
        }

        for(File f : Objects.requireNonNull(rootFile.listFiles()))
        {
            if(f.isDirectory())
            {
                if(ImGui.button("[ ] " + f.getName()))
                    rootFile = new File(f.getAbsolutePath());
            }
            else
            {
                if(ImGui.button("</> " + f.getName()))
                    Debug.Log(LogLevel.MESSAGE, FileManager.getType(f.getAbsolutePath()));
            }
        }

        ImGui.end();

    }

}
