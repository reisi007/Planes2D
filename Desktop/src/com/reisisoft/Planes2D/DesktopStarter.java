package com.reisisoft.Planes2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopStarter implements INative {
    private MovementContainer Input;
    private LwjglApplication application;
    private LwjglApplicationConfiguration cfg;
    private int maxH;

    public static void main(String[] args) {
        DesktopStarter ds = new DesktopStarter();
        ds.Start();
    }

    public DesktopStarter() {
        cfg = new LwjglApplicationConfiguration();
        cfg.title = "Plane 2D";
        cfg.useGL20 = true;
        cfg.width = 1280;
        maxH = cfg.height = 720;
        cfg.resizable = false;
        cfg.initialBackgroundColor = Helper.CornFlowerBlue;
        cfg.foregroundFPS = 60;
        Input = new MovementContainer(cfg.height / 2f, false);
    }

    @Override
    public void letQuit() {
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            application.exit();
        }
    }

    public MovementContainer Input() {
        Input.doShoot = Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP))
            Input.Movement = 1f;
        else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN))
            Input.Movement = -1f;
        else
            Input.Movement = 0f;
           /* if (Input.Movement > maxH - 20) {
                Input.Movement = maxH - 20;
                Gdx.input.setCursorPosition(50, maxH - 20);
            } else if (Input.Movement < 20) {
                Input.Movement = 20;
                Gdx.input.setCursorPosition(50, 20);
            }*/
        return Input;
    }

    public void Start() {
        application = new LwjglApplication(new Planes2D(this), cfg);
    }

    public void Setup() {
        Gdx.input.setCursorCatched(true);
        Gdx.input.setCursorPosition(50, maxH / 2);
    }

    @Override
    public boolean ContinueStagesWorkflow() {
        return Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
    }

    @Override
    public float speedX() {
        return 1;
    }

    @Override
    public float speedY() {
        return 1;
    }

    @Override
    public Planes2D.Resolutions prefferredResolution(Planes2D game) {
        if (game.getCurH() > 500f)
            return Planes2D.Resolutions.HiRes;
        return Planes2D.Resolutions.LowRes;
    }
}
