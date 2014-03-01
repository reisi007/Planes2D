package com.reisisoft.Planes2D;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidStarter extends AndroidApplication implements INative {
    private MovementContainer Input = new MovementContainer();
    private SharedPreferences preferences;
    private int cWidth;
    private float cScreenSize, cDPI;
    private InputMethod method = InputMethod.Default;

    private enum InputMethod {Default, Desktop}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        cWidth = dm.widthPixels > dm.heightPixels ? dm.widthPixels : dm.heightPixels;
        cDPI = cWidth == dm.widthPixels ? dm.xdpi : dm.ydpi;
        cScreenSize = cWidth / cDPI;
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = true;
        cfg.useCompass = true;
        cfg.useWakelock = true;
        cfg.useGL20 = true;
        cfg.hideStatusBar = true;
        preferences = this.getSharedPreferences("SCORE", Context.MODE_PRIVATE);
        initialize(new Planes2D(this), cfg);
    }

    float max = 30f;

    @Override
    public MovementContainer Input() {
        switch (method) {
            case Desktop:
                Input.doShoot = Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
                if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP))
                    Input.Movement = -1f;
                if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN))
                    Input.Movement = 1f;
                return Input;
            default:
                Input.doShoot = Gdx.input.isTouched();
                float m = 0f;
                switch (Gdx.input.getRotation()) {
                    case (0):
                    case (90):
                        m = -Gdx.input.getPitch();
                        break;
                    case (180):
                    case (270):
                        m = Gdx.input.getPitch();
                        break;
                }
                if (m > max)
                    m = max;
                if (m < -max)
                    m = -max;
                Input.Movement = m / max;
                return Input;
        }
    }

    @Override
    public void letQuit() {
        //Not needed for Android
    }

    @Override
    public void Setup() {
        //Not needed for Android
    }

    @Override
    public boolean continueStagesWorkflow() {
        return Gdx.input.isTouched() || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
    }

    @Override
    public float speedX() {
        switch (method) {
            case Desktop:
                return 1f;
            default:
                return 0.7f;
        }
    }

    @Override
    public float speedY() {
        switch (method) {
            case Desktop:
                return 1f;
            default:
                return 2f;
        }
    }

    @Override
    public Planes2D.Resolutions prefferredResolution(Planes2D game) {
        if (cScreenSize < 6f)
            return Planes2D.Resolutions.LowRes;
        return Planes2D.Resolutions.HiRes;
    }

    @Override
    public String WelcomeMessage() {
        StringBuilder sb = new StringBuilder("Welcome to Planes 2D");
        sb.append("\nTouch the screen to start the game");
        sb.append("\n`'Steer' left and right to move");
        sb.append("\nTouch the screen to shoot");
        sb.append("\nIf you hit <SPACE> you can use <UP> and <DOWN>");
        return sb.toString();
    }

    @Override
    public String GameOverMessage(int score, boolean newHighScore) {
        StringBuilder sb = new StringBuilder("Game Over!");
        if (newHighScore)
            sb.append("\nNEW HIGHSCORE!!!");
        sb.append("\nYour score is:\n");
        sb.append(score);
        sb.append("\nPress <SPACE> to retry!");
        return sb.toString();
    }

    private int HIGHSCORE = 0;

    @Override
    public void saveScore(int score) {
        HIGHSCORE = score;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("HIGHSCORE", HIGHSCORE);
        editor.apply();
    }

    @Override
    public int getHighScore() {
        return (HIGHSCORE = preferences.getInt("HIGHSCORE", 0));
    }

    @Override
    public boolean firstTouch() {
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            method = InputMethod.Desktop;
            return true;
        }
        if (Gdx.input.isTouched()) {
            method = InputMethod.Default;
            return true;
        }
        return false;
    }
}