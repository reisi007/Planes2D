package com.reisisoft.Planes2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.awt.*;

public class DesktopStarter implements INative {
    private MovementContainer Input;
    private LwjglApplication application;
    private LwjglApplicationConfiguration cfg;
    private int maxH;
    private boolean goneFullscreen = false;
    private static final boolean DEBUG = false;

    public static void main(String[] args) {
        DesktopStarter ds = new DesktopStarter();
        ds.Start();
    }

    public DesktopStarter() {
        cfg = new LwjglApplicationConfiguration();
        cfg.title = "Plane 2D";
        cfg.useGL20 = true;
        if (DEBUG) {
            cfg.width = 1280;
            cfg.height = 720;
            cfg.fullscreen = false;
        } else {
            cfg.width = Toolkit.getDefaultToolkit().getScreenSize().width;
            cfg.height = Toolkit.getDefaultToolkit().getScreenSize().height;
            goneFullscreen = cfg.fullscreen = true;
        }
        maxH = cfg.height;
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
        return Input;
    }

    public void Start() {
        application = new LwjglApplication(new Planes2D(this), cfg);
    }

    public void Setup() {
        if (goneFullscreen)
            Gdx.input.setCursorCatched(true);
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

    @Override
    public String WelcomeMessage() {
        StringBuilder sb = new StringBuilder("Welcome to Planes 2D");
        sb.append("\nPress <SPACE> to start the game");
        sb.append("\nUse <ARROW_UP> and <ARROW_DOWN> to move");
        sb.append("\nUse <SPACE> to shoot");
        return sb.toString();
    }

    @Override
    public String GameOverMessage(int score) {
        StringBuilder sb = new StringBuilder("Game Over!");
        sb.append("\nYour score is:\n");
        sb.append(score);
        sb.append("\nPress <SPACE> to retry!");
        return sb.toString();
    }
}
