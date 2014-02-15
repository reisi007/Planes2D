package com.reisisoft.Planes2D;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.EnumMap;
import java.util.Map;

public class Planes2D extends Game {
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private float curH, curW;
    private Texture txtMidRes;
    private Map<Resolutions, Map<GObjects, Sprite>> all = new EnumMap<Resolutions, Map<GObjects, Sprite>>(Resolutions.class);
    private Map<GObjects, Sprite> currentResolutionSprites;
    private Resolutions currentResolution;
    private GameState GSlast, GScurrent = GameState.Start;

    public enum GameState {Start, Paused, Resume, InGame, GameOver}

    private enum GObjects {PlaneR, PlaneG, PlaneB, Grass, Bullet}

    private enum Resolutions {MidRes}

    private boolean setCurrentResolutions(Resolutions resolution) {
        if (resolution == null)
            return false;
        currentResolution = resolution;
        currentResolutionSprites = all.get(Resolutions.MidRes);
        return true;
    }

    private void updateCamera() {
        camera = new OrthographicCamera(curW, curH);
        camera.position.set(curH / 2f, curW / 2f, 0);
        camera.setToOrtho(false,curW, curH);
        camera.update();
    }

    private Sprite requestSprite(GObjects gObject) {
        return currentResolutionSprites.get(gObject);
    }

    private void putTextures(Resolutions resolutions) {
        Map<GObjects, Sprite> map;
        switch (resolutions) {
            case MidRes:
                map = new EnumMap<GObjects, Sprite>(GObjects.class);
                map.put(GObjects.PlaneR, new Sprite(txtMidRes, 0, 0, 600, 278));
                map.put(GObjects.PlaneG, new Sprite(txtMidRes, 0, 278, 600, 278));
                map.put(GObjects.PlaneB, new Sprite(txtMidRes, 0, 556, 600, 104));
                map.put(GObjects.Grass, new Sprite(txtMidRes, 0, 834, 600, 104));
                map.put(GObjects.Bullet, new Sprite(txtMidRes, 600, 834, 299, 138));
                all.put(Resolutions.MidRes, map);
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
        txtMidRes = new Texture(Gdx.files.internal("planes.png"));
        txtMidRes.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        // Create Sprites for EVERY supported resolution
        putTextures(Resolutions.MidRes);
        // Set resolution according to H/W
        setCurrentResolutions(Resolutions.MidRes);
    }

    // On pause
    public void pause() {
        GSlast = GScurrent;
        GScurrent = GameState.Paused;
    }

    public void resize(int width, int height) {
        curH = height;
        curW = width;
        updateCamera();
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
        // test drawing sprite
        Sprite s = requestSprite(GObjects.PlaneR);
        s.setOrigin(0,0);
        s.setPosition(0,0);
        s.draw(spriteBatch);
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
        txtMidRes.dispose();
        super.dispose();
    }
}
