package com.reisisoft.Planes2D;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopStarter implements INative {
    private MovementContainer Input = new MovementContainer();

    public static void main(String[] args) {
        new DesktopStarter();
    }

    public DesktopStarter() {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Plane 2D";
        cfg.useGL20 = true;
        cfg.width = 1280;
        cfg.height = 720;
        cfg.resizable = false;
        cfg.initialBackgroundColor = Helper.CornFlowerBlue;
        cfg.foregroundFPS = 60;
        new LwjglApplication(new Planes2D(this), cfg);
    }

    public MovementContainer Input() {
        return Input;
    }
}
