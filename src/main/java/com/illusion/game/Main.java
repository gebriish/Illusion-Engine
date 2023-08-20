package com.illusion.game;

import com.illusion.engine.client.Application;


public class Main {

    public static void main(String[] args) {
        Application game = new Application(new IGameLogic());
        game.run();
    }

}
