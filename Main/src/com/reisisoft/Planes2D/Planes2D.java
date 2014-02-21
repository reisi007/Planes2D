package com.reisisoft.Planes2D;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;
import java.util.Map;

public class Planes2D extends Game {
    public Planes2D(INative iNative) {
        super();
        this.iNative = iNative;
    }

    private INative iNative;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private float curH, curW;
    private Texture txtHiRes, txtLowRes, txtBomb;
    private Map<Resolutions, Map<GObjects, TextureRegion>> all = new EnumMap<Resolutions, Map<GObjects, TextureRegion>>(Resolutions.class);
    private Map<Resolutions, Texture> explosions = new EnumMap<Resolutions, Texture>(Resolutions.class);
    private Map<GObjects, TextureRegion> currentResolutionTextureRegion;
    private Resolutions currentResolution;
    private GameState GSlast, GScurrent = GameState.Start;
    private GameTime Time;
    /*
    Game objects
     */
    private GrassManager grass;
    private CloudManager clouds;
    private UserPlane user;

    public enum GameState {Start, Paused, Resume, InGame, GameOver}

    private enum GObjects {PlaneR, PlaneG, PlaneB, Grass, Bullet, Star, Cloud}

    private enum Resolutions {LowRes, HiRes}

    private boolean setCurrentResolutions(Resolutions resolution) {
        if (resolution == null)
            return false;
        currentResolution = resolution;
        currentResolutionTextureRegion = all.get(resolution);
        return true;
    }

    private void updateCamera() {
        camera = new OrthographicCamera(curW, curH);
        camera.position.set(curH / 2f, curW / 2f, 0);
        camera.setToOrtho(false, curW, curH);
        camera.update();
    }

    private TextureRegion requestTextureRegion(GObjects gObject) {
        return currentResolutionTextureRegion.get(gObject);
    }

    private void putTextures(Resolutions resolutions) {
        Map<GObjects, TextureRegion> map;
        switch (resolutions) {
            case LowRes:
                map = new EnumMap<GObjects, TextureRegion>(GObjects.class);
                map.put(GObjects.PlaneB, new TextureRegion(txtLowRes, 304, 50, 100, 46));
                map.put(GObjects.PlaneG, new TextureRegion(txtLowRes, 406, 2, 100, 46));
                map.put(GObjects.PlaneR, new TextureRegion(txtLowRes, 304, 2, 100, 46));
                map.put(GObjects.Grass, new TextureRegion(txtLowRes, 2, 188, 294, 52));
                map.put(GObjects.Bullet, new TextureRegion(txtBomb, 0, 0, 256, 127));
                map.put(GObjects.Cloud, new TextureRegion(txtLowRes, 2, 2, 300, 184));
                map.put(GObjects.Star, new TextureRegion(txtLowRes, 406, 50, 26, 26));
                all.put(Resolutions.LowRes, map);
                break;
            case HiRes:
                map = new EnumMap<GObjects, TextureRegion>(GObjects.class);
                map.put(GObjects.PlaneB, new TextureRegion(txtHiRes, 2, 2, 594, 275));
                map.put(GObjects.PlaneG, new TextureRegion(txtHiRes, 2, 370, 600, 278));
                map.put(GObjects.PlaneR, new TextureRegion(txtHiRes, 604, 370, 600, 278));
                map.put(GObjects.Grass, new TextureRegion(txtHiRes, 1200, 2, 600, 104));
                map.put(GObjects.Bullet, new TextureRegion(txtBomb, 0, 0, 256, 127));
                map.put(GObjects.Cloud, new TextureRegion(txtHiRes, 598, 2, 600, 366));
                map.put(GObjects.Star, new TextureRegion(txtHiRes, 1206, 370, 100, 100));
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
        txtHiRes = new Texture(Gdx.files.internal("x2.png"));
        txtLowRes = new Texture(Gdx.files.internal("x1.png"));
        explosions.put(Resolutions.LowRes, new Texture(Gdx.files.internal("explosion_x1.png")));
        explosions.put(Resolutions.HiRes, new Texture(Gdx.files.internal("explosion_x2.png")));
        txtBomb = new Texture(Gdx.files.internal("bomb.png"));
        txtLowRes.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        txtHiRes.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        // Create Sprites for EVERY supported resolution
        putTextures(Resolutions.LowRes);
        putTextures(Resolutions.HiRes);
        // Set resolution according to H/W
        setCurrentResolutions(Resolutions.HiRes);
        iNative.Setup();
        //Set up time
        Time = new GameTime();
        setDebug(false, false);
        clouds = new CloudManager((int) (curW / 128 + 0.5f), requestTextureRegion(GObjects.Cloud), curW, 2f * curH / 3, curH, curH / 3);
        grass = new GrassManager(requestTextureRegion(GObjects.Grass), curH / 15f, curW);
        user = new UserPlane(getExplosionForPlanes(), getBombForPlanes(true), requestTextureRegion(GObjects.PlaneR), curW, curH, iNative);
        //Last thing to do!! Start the time
        Time.Start();
    }

    // On pause
    public void pause() {
        Time.Pause();
        GSlast = GScurrent;
        GScurrent = GameState.Paused;
    }

    //On resume
    public void resume() {
        GScurrent = GSlast;
        GSlast = GameState.Paused;
        Time.Resume();
    }

    // Update all the game objects
    public void Update(GameTime.GameTimeArgs gameTime) {
        clouds.Update(gameTime);
        grass.Update(gameTime);
        user.Update(gameTime);
    }

    // Draw all the game objects
    public void Draw() {
        grass.Draw(spriteBatch);
        clouds.Draw(spriteBatch);
        user.Draw(spriteBatch);
    }

    private SingleAnimation getExplosionForPlanes() {
        return new SingleAnimation(explosions.get(currentResolution), Vector2.Zero, Vector2.Zero, 0, currentResolution == Resolutions.HiRes ? 222 : 111, 3, 4, 5, 200);
    }

    private Bomb getBombForPlanes(boolean user) {
        return new Bomb(requestTextureRegion(GObjects.Bullet), Vector2.Zero, Vector2.Zero, 0, IGameObject.Anchor.MiddleLeft, user, curH);
    }

    public void render() {
        Time.Update();
        Update(Time.Time);
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

    private void setDebug(boolean isDebugDRAWABLE, boolean isDebugPLANE) {
        //Set Debug for Drawable
        BasicPlane.blackDebug = Drawable.blackDebug = new TextureRegion(new Texture(Gdx.files.internal("black.png")));
        BasicPlane.DEBUG = isDebugPLANE;
        Drawable.DEBUG = isDebugDRAWABLE;
    }
}
