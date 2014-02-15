package com.reisisoft.Planes2D;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import  com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/**
 * Created by Florian on 14.02.14.
 */
public class DesktopStarter {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Plane 2D";
        cfg.useGL20 = true;
        cfg.width = 1280;
        cfg.height = 720;
        cfg.resizable = false;
        cfg.initialBackgroundColor = Helper.CornFlowerBlue;
        new  LwjglApplication(new Planes2D(),cfg);
    }
}
