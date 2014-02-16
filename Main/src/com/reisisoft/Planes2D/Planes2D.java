package com.reisisoft.Planes2D;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;
import java.util.Map;

public class Planes2D extends Game {
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private float curH, curW;
    private Texture txtHiRes, txtLowRes;
    private Map<Resolutions, Map<GObjects, Sprite>> all = new EnumMap<Resolutions, Map<GObjects, Sprite>>(Resolutions.class);
    private Map<GObjects, Sprite> currentResolutionSprites;
    private Resolutions currentResolution;
    private GameState GSlast, GScurrent = GameState.Start;
    private IDrawable test;

    public enum GameState {Start, Paused, Resume, InGame, GameOver}

    private enum GObjects {PlaneR, PlaneG, PlaneB, Grass, Bullet, Star, Cloud}

    private enum Resolutions {LowRes, HiRes}

    private boolean setCurrentResolutions(Resolutions resolution) {
        if (resolution == null)
            return false;
        currentResolution = resolution;
        currentResolutionSprites = all.get(resolution);
        return true;
    }

    private void updateCamera() {
        camera = new OrthographicCamera(curW, curH);
        camera.position.set(curH / 2f, curW / 2f, 0);
        camera.setToOrtho(false, curW, curH);
        camera.update();
    }

    private Sprite requestSprite(GObjects gObject) {
        return currentResolutionSprites.get(gObject);
    }

    private void putTextures(Resolutions resolutions) {
        Map<GObjects, Sprite> map;
        switch (resolutions) {
            case LowRes:
                map = new EnumMap<GObjects, Sprite>(GObjects.class);
                map.put(GObjects.PlaneB, new Sprite(txtLowRes, 304, 50, 100, 46));
                map.put(GObjects.PlaneG, new Sprite(txtLowRes, 406, 2, 100, 46));
                map.put(GObjects.PlaneR, new Sprite(txtLowRes, 304, 2, 100, 46));
                map.put(GObjects.Grass, new Sprite(txtLowRes, 2, 188, 294, 52));
                map.put(GObjects.Bullet, new Sprite(txtLowRes, 600, 834, 299, 138));
                map.put(GObjects.Cloud, new Sprite(txtLowRes, 2, 2, 300, 184));
                map.put(GObjects.Star, new Sprite(txtLowRes, 406, 50, 26, 26));
                all.put(Resolutions.LowRes, map);
                break;
            case HiRes:
                map = new EnumMap<GObjects, Sprite>(GObjects.class);
                map.put(GObjects.PlaneB, new Sprite(txtHiRes, 2, 2, 594, 275));
                map.put(GObjects.PlaneG, new Sprite(txtHiRes, 2, 370, 600, 278));
                map.put(GObjects.PlaneR, new Sprite(txtHiRes, 604, 370, 600, 278));
                map.put(GObjects.Grass, new Sprite(txtHiRes, 1200, 2, 600, 104));
                map.put(GObjects.Bullet, new Sprite(txtHiRes, 600, 834, 299, 138));
                map.put(GObjects.Cloud, new Sprite(txtHiRes, 598, 2, 600, 366));
                map.put(GObjects.Star, new Sprite(txtHiRes, 1206, 370, 100, 100));
                all.put(Resolutions.HiRes, map);
                break;
        }
    }

    // On creation
    public void create() {
        curH = Gdx.graphics.getHeight();
        curW = Gdx.graphics.getWidth();
        spriteBatch = new SpriteBatch();
        updateCamera();
        // Load texture
        txtHiRes = new Texture(Gdx.files.internal("spritesheets/x2.png"));
        txtLowRes = new Texture(Gdx.files.internal("spritesheets/x1.png"));
        txtLowRes.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        txtHiRes.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        // Create Sprites for EVERY supported resolution
        putTextures(Resolutions.LowRes);
        putTextures(Resolutions.HiRes);
        // Set resolution according to H/W
        setCurrentResolutions(Resolutions.HiRes);
        test = new IDrawable(requestSprite(GObjects.Grass), new Vector2(10, 10), IDrawable.Anchor.LowLeft, requestSprite(GObjects.Grass).getWidth() / 2);
    }

    // On pause
    public void pause() {
        GSlast = GScurrent;
        GScurrent = GameState.Paused;
    }

    //On resume
    public void resume() {
        GScurrent = GSlast;
        GSlast = GameState.Paused;
    }

    // Update all the game objects
    public void Update() {

    }

    // Draw all the game objects
    public void Draw() {
        test.Draw(spriteBatch);
    }


    public void render() {
        Update();
        Helper.ClearColor(Helper.CornFlowerBlue);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        Draw();
        spriteBatch.end();
    }

    public void dispose() {
        spriteBatch.dispose();
        txtLowRes.dispose();
        txtHiRes.dispose();
        super.dispose();
    }
}
