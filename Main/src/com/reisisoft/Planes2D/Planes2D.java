package com.reisisoft.Planes2D;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class Planes2D extends Game {
    public Planes2D(INative iNative) {
        super();
        this.iNative = iNative;
    }

    private BitmapFont[] bitmapFonts;
    private INative iNative;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private float curH, curW;

    public float getCurH() {
        return curH;
    }


    private Texture txtHiRes, txtLowRes, txtBomb;
    private Map<Resolutions, Map<GObjects, TextureRegion>> all = new EnumMap<Resolutions, Map<GObjects, TextureRegion>>(Resolutions.class);
    private Map<Resolutions, Texture> explosions = new EnumMap<Resolutions, Texture>(Resolutions.class);
    private Map<GObjects, TextureRegion> currentResolutionTextureRegion;
    private Resolutions currentResolution;
    private GameState GSlast, GScurrent = GameState.Start;
    private GameTime Time;
    private BombDrawer availableBombs;
    /*
    Game objects
     */
    private GrassManager grass;
    private CloudManager clouds;
    private UserPlane user;
    private ArrayList<Bomb> userBombs, enemyBombs;
    private ArrayList<EnemyPlane> enemyPlanes;
    private ArrayList<SingleAnimation> ALexplosions;
    /*
    Drawable Text
     */
    private DrawableText dtScore, dtWelcome, dtGameover;

    public enum GameState {Start, Paused, Resume, PrepareGame, InGame, PrepearScore, GameOver}

    private enum GObjects {PlaneR, PlaneG, PlaneB, Grass, Bullet, Star, Cloud}

    public enum Resolutions {LowRes, HiRes}

    private boolean setCurrentResolutions(Resolutions resolution) {
        if (resolution == null)
            return false;
        currentResolution = resolution;
        currentResolutionTextureRegion = all.get(resolution);
        return true;
    }

    private void loadFonts() {
        bitmapFonts = new BitmapFont[4];
        bitmapFonts[0] = new BitmapFont(Gdx.files.internal("fonts/25.fnt"));
        bitmapFonts[1] = new BitmapFont(Gdx.files.internal("fonts/30.fnt"));
        bitmapFonts[2] = new BitmapFont(Gdx.files.internal("fonts/35.fnt"));
        bitmapFonts[3] = new BitmapFont(Gdx.files.internal("fonts/40.fnt"));
        for (int f = 0; f < bitmapFonts.length; f++)
            bitmapFonts[f].setColor(Color.BLACK);
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
        Moveable.totalWidth = curW > curH ? curW : curH;
        Moveable.totalHeight = curW > curH ? curH : curW;
        DrawableText.currentH = Moveable.totalHeight;
        DrawableText.currentW = Moveable.totalWidth;
        //System.out.println("X Modifier:\t" + Moveable.getSpeedXModifier() + "\tY Modifier:\t" + Moveable.getSpeedYModifier());
        spriteBatch = new SpriteBatch();
        loadFonts();
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
        setCurrentResolutions(iNative.prefferredResolution(this));
        iNative.Setup();
        //Set up time
        Time = new GameTime();
        setDebug(false, false);
        clouds = new CloudManager((int) (curW / 128 + 0.5f), requestTextureRegion(GObjects.Cloud), curW, 2f * curH / 3, curH, curH / 3);
        grass = new GrassManager(requestTextureRegion(GObjects.Grass), curH / 15f, curW);
        availableBombs = new BombDrawer(user, new Drawable(requestTextureRegion(GObjects.Bullet), Vector2.Zero, IDrawable.Anchor.LowLeft), 4, 0.9f * curH - 4, curW);
        dtWelcome = new DrawableText(bitmapFonts, iNative.WelcomeMessage(), curW * 0.95f, true, curW / 2, 2 * curH / 3, IDrawable.Anchor.MiddleMiddle);
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

    long nextEnemy = 0, MSbetweenEnemies = 3000;
    int SCORE;
    int SEC_AcceptNext = -1;

    // Update all the game objects
    public void Update(GameTime.GameTimeArgs gameTime) {
        iNative.letQuit();
        switch (GScurrent) {
            case Start:
                if (iNative.ContinueStagesWorkflow()) {
                    GSlast = GScurrent;
                    GScurrent = GameState.PrepareGame;
                }
                break;
            case PrepareGame:
                user = new UserPlane(getExplosionForPlanes(), getBombForPlanes(true), requestTextureRegion(GObjects.PlaneR), curW, curH, iNative);
                availableBombs.setUserPlane(user);
                userBombs = new ArrayList<>();
                enemyBombs = new ArrayList<>();
                enemyPlanes = new ArrayList<>();
                ALexplosions = new ArrayList<>();
                SCORE = 0;
                GSlast = GScurrent;
                GScurrent = GameState.InGame;
                dtScore = new DrawableText(bitmapFonts, "Score: 0", curH / 10, false, 4, 1.015f * curH, IDrawable.Anchor.TopLeft);
                //Last thing: Start time
                Time.Restart();
                break;

            case InGame:
               /* if (gameTime.ElapsedFrames % 20 == 0)
                    System.out.println("Number of enemies:\t" + enemyPlanes.size());*/
                clouds.Update(gameTime);
                grass.Update(gameTime);
                //Update User
                user.Update(gameTime);
                if (user.wantShoot(gameTime))
                    userBombs.add(user.getShot());
                //Update UserBombs and delete them, when they are out of screen
                for (int i = 0; i < userBombs.size(); i++) {
                    Bomb b = userBombs.get(i);
                    b.Update(gameTime);
                    if (b.leftMost() > curW) {
                        userBombs.remove(i);
                        i--;
                    }
                }
                // Update enemyPlanes
                if (nextEnemy <= 0) {
                    nextEnemy += (MSbetweenEnemies -= 50);
                    enemyPlanes.add(getEnemyPlane());
                    user.addFreeShot();
                }
                nextEnemy -= gameTime.ELapsedMSSinceLastFrame;
                for (int i = 0; i < enemyPlanes.size(); i++) {
                    EnemyPlane plane = enemyPlanes.get(i);
                    plane.Update(gameTime);
                    if (plane.rightMost() < 0) {
                        //EnemyPlane out of screen
                        enemyPlanes.remove(i);
                        enemyPlanes.add(getEnemyPlane());
                        i--;
                    } else {
                        if (plane.wantShoot(gameTime))
                            enemyBombs.add(plane.getShot());
                    }
                }
                //Update Enemybombs and delete them, when they are out of screen
                for (int i = 0; i < enemyBombs.size(); i++) {
                    Bomb b = enemyBombs.get(i);
                    b.Update(gameTime);
                    if (b.rightMost() < 0) {
                        enemyBombs.remove(i);
                        i--;
                    }
                }
                // Update explosions
                for (int i = 0; i < ALexplosions.size(); i++) {
                    SingleAnimation s = ALexplosions.get(i);
                    if (s.isFinished()) {
                        ALexplosions.remove(i);
                        i--;
                    } else
                        s.Update(gameTime);
                }
                availableBombs.Update(gameTime);
                /*
                Intersection testing
                 */

                //User plane vs. enemy planes
                for (int i = 0; i < enemyPlanes.size(); i++) {
                    if (user.Intersects(enemyPlanes.get(i))) {
                        GameOver();
                    }
                }
                //User plane vs. enemy Bombs
                for (int i = 0; i < enemyBombs.size(); i++) {
                    if (user.Intersects(enemyBombs.get(i)))
                        GameOver();
                }
                //User bombs vs. Enemy planes
                for (int b = 0; b < userBombs.size(); b++) {
                    // System.out.println("UserBombsSize" + userBombs.size());
                    for (int p = 0; p < enemyPlanes.size() && b < userBombs.size(); p++) {
                        //  System.out.println("b:\t" + b + "bSIZE:\t" + userBombs.size() + "p:\t" + p + "pSIZE:\t" + enemyPlanes.size());
                        if (userBombs.get(b).Intersects(enemyPlanes.get(p))) {
                            userBombs.remove(b);
                            b--;
                            if (b < 0)
                                b = 0;
                            SCORE++;
                            dtScore.setText("Score: " + SCORE);
                            ALexplosions.add(enemyPlanes.get(p).getExplosion());
                            enemyPlanes.remove(p);
                            p--;
                            if (p < 0)
                                p = 0;
                            if (Helper.getRandomInRange(0, 5) <= 1)
                                enemyPlanes.add(getEnemyPlane());
                        }
                    }
                }
                break;
            case PrepearScore:
                GSlast = GScurrent;
                GScurrent = GameState.GameOver;
                dtGameover = new DrawableText(bitmapFonts, iNative.GameOverMessage(SCORE), 0.95f * curW, true, curW / 2f, 2f / 3 * curH, IDrawable.Anchor.MiddleMiddle);
                SEC_AcceptNext = gameTime.ElapsedSeconds + 1;
                break;
            case GameOver:
                if ((gameTime.ElapsedSeconds > SEC_AcceptNext) && iNative.ContinueStagesWorkflow()) {
                    GSlast = GScurrent;
                    GScurrent = GameState.PrepareGame;
                }
                break;
        }
    }

    private void GameOver() {
        GSlast = GScurrent;
        GScurrent = GameState.PrepearScore;
    }

    // Draw all the game objects
    public void Draw() {
        switch (GScurrent == GameState.Paused ? GSlast : GScurrent) {
            case Start:
                dtWelcome.Draw(spriteBatch);
                break;
            case InGame:
                //BG
                grass.Draw(spriteBatch);
                clouds.Draw(spriteBatch);
                for (Bomb b : userBombs)
                    b.Draw(spriteBatch);
                for (Bomb b : enemyBombs)
                    b.Draw(spriteBatch);
                for (EnemyPlane e : enemyPlanes)
                    e.Draw(spriteBatch);
                user.Draw(spriteBatch);
                for (SingleAnimation s : ALexplosions)
                    s.Draw(spriteBatch);
                availableBombs.Draw(spriteBatch);
                dtScore.Draw(spriteBatch);
                //FG
                break;
            case GameOver:
                dtGameover.Draw(spriteBatch);
                break;
        }
    }

    private SingleAnimation getExplosionForPlanes() {
        return new SingleAnimation(explosions.get(currentResolution), Vector2.Zero, Vector2.Zero, 0, currentResolution == Resolutions.HiRes ? 222 : 111, 3, 4, 5, 200);
    }

    private Bomb getBombForPlanes(boolean user) {
        return new Bomb(requestTextureRegion(GObjects.Bullet), Vector2.Zero, Vector2.Zero, 0, IGameObject.Anchor.MiddleLeft, user, curH);
    }

    private EnemyPlane getEnemyPlane() {
        return new EnemyPlane(getExplosionForPlanes(), getBombForPlanes(false), getRandomPlaneTextureRegion(), curH, curW);
    }

    private TextureRegion getRandomPlaneTextureRegion() {
        GObjects rand = null;
        switch (Helper.getRandomInRange(0, 2)) {
            case (0):
                rand = GObjects.PlaneR;
                break;
            case (1):
                rand = GObjects.PlaneG;
                break;
            default:
                rand = GObjects.PlaneB;
                break;
        }
        return requestTextureRegion(rand);
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
