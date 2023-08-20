package com.illusion.game.tech;

import com.google.gson.Gson;
import com.illusion.engine.core.SpriteSheet;
import com.illusion.engine.ecs.CSpriteRenderer;
import com.illusion.engine.ecs.Entity;
import com.illusion.engine.graphics.Renderer;
import com.illusion.engine.graphics.Texture;
import com.illusion.engine.utils.FileManager;

public class TileLevelMaker {

    private static Gson gson;

    static
    {
        gson = new Gson();
    }

    public static Entity[] CreateTileSetFromString(String fileLoc)
    {
        TileSet ts = gson.fromJson(FileManager.loadFileToString(fileLoc), TileSet.class);
        Entity[] entities = new Entity[ts.getWidth() * ts.getHeight()];
        Texture tex = FileManager.loadTexture(ts.getTexFileLoc(), Renderer.NEAREST_FILTERING, Renderer.MIPMAP_NAN);

        for(int i=0; i<ts.getWidth(); i++)
        {
            for(int j=0; j<ts.getHeight(); j++)
            {
                int linearIndex = (j * ts.getWidth()) + i;
                int index = ts.getTiles()[linearIndex];
                if(index == 17)
                    continue;

               Entity en = new Entity("tile no: " + linearIndex);
               CSpriteRenderer spr = new CSpriteRenderer(tex);
               int xDiv = tex.getWidth()/ts.getTileSize();
               int yDiv = tex.getHeight()/ts.getTileSize();

               SpriteSheet.CalcSpriteUV(spr, xDiv, yDiv, index);
               en.transform().setScale(ts.getTileSize());
               en.transform().Position().set(i * ts.getTileSize(), -j * ts.getTileSize());
               en.components().add(spr);
               en.setStatic(true);
               entities[linearIndex] = en;
            }
        }

        return entities;
    }
}
