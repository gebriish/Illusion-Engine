package com.illusion.game.tech;

public class TileSet {
    private int tileSize;
    private int x, y;
    private int[] tiles;
    private String texFileLoc;

    public TileSet(int x, int y, int tS, int[] tiles)
    {
        this.tiles = tiles;
        this.x = x;
        this.y = y;
        this.tileSize = tS;
        this.texFileLoc = "\0";
    }

    public String getTexFileLoc() {
        return texFileLoc;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getWidth() {
        return x;
    }

    public int getHeight() {
        return y;
    }

    public int[] getTiles() {
        return tiles;
    }
}
