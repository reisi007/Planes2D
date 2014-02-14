package com.reisisoft.Planes2D;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import  com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/**
 * Created by Florian on 14.02.14.
 */
public class DesktopStarter {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Title";
        cfg.useGL20 = true;
        cfg.width = 800;
        cfg.height = 480;
        new  LwjglApplication(new Planes2D(),cfg);
    }
}
