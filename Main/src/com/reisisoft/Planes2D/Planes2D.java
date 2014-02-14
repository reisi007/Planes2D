package com.reisisoft.Planes2D;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Created by Florian on 14.02.14.
 */
public class Planes2D extends Game {
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    float curH, curW;

    // On creation
    public void create() {
        curH = Gdx.graphics.getHeight();
        curW = Gdx.graphics.getWidth();
        camera = new OrthographicCamera(1, curH / curW);
    }

    // On pause
    public void pause() {

    }

    public void resize(int width, int height) {
        curH = height;
        curW = width;
        camera = new OrthographicCamera(1, curH / curW);
    }

    //On resume
    public void resume() {

    }

    // Update all the game objects
    public void Update() {

    }

    // Draw all the game objects
    public void Draw() {

    }


    public void render() {
        Update();
        Helper.ClearColor(0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Draw();
    }
}
