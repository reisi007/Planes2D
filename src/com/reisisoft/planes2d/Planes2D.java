package com.reisisoft.planes2d;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

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

    private Texture txtHiRes, txtLowRes, txtShot, txtBombHi, txtBombLow;
    private Map<Resolutions, Map<GObjects, TextureRegion>> all = new EnumMap<>(
            Resolutions.class);
    private Map<Resolutions, Texture> explosions = new EnumMap<>(
            Resolutions.class);
    private Map<GObjects, TextureRegion> currentResolutionTextureRegion;
    private Resolutions currentResolution;
    private GameState GSlast, GScurrent = GameState.FirstFrame;
    private GameTime Time;
    private BombDrawer availableBombs;
    private LifeDrawer availableLives;
    private Texture txtRotatingStar, txtRotatingHeartHi, txtrotatingHeartLow;
    private BaseAnimation[] powerUpStart;
    /*
     * Game objects
     */
    private GrassManager grass;
    private CloudManager clouds;
    private UserPlane user;
    private ArrayList<Bomb> userBombs, enemyBombs;
    private ArrayList<EnemyPlane> enemyPlanes;
    private ArrayList<SingleAnimation> ALexplosions;
    private ArrayList<PowerUp> powerUps;
    /*
     * Drawable Text
     */
    private DrawableText dtScore, dtWelcome, dtGameover;

    public enum GameState {
        FirstFrame, Start, Paused, Resume, PrepareGame, InGame, PrepearScore, GameOver
    }

    private enum GObjects {
        PlaneR, PlaneG, PlaneB, Grass, Bullet, Star, Cloud, Heart
    }

    public enum Resolutions {
        LowRes, HiRes
    }

    private boolean setCurrentResolutions(Resolutions resolution) {
        if (resolution == null)
            return false;
        currentResolution = resolution;
        currentResolutionTextureRegion = all.get(resolution);
        return true;
    }

    private void loadFonts() {
        bitmapFonts = new BitmapFont[17];
        bitmapFonts[0] = new BitmapFont(Gdx.files.internal("fonts/20.fnt"));
        bitmapFonts[1] = new BitmapFont(Gdx.files.internal("fonts/25.fnt"));
        bitmapFonts[2] = new BitmapFont(Gdx.files.internal("fonts/30.fnt"));
        bitmapFonts[3] = new BitmapFont(Gdx.files.internal("fonts/35.fnt"));
        bitmapFonts[4] = new BitmapFont(Gdx.files.internal("fonts/40.fnt"));
        bitmapFonts[5] = new BitmapFont(Gdx.files.internal("fonts/45.fnt"));
        bitmapFonts[6] = new BitmapFont(Gdx.files.internal("fonts/50.fnt"));
        bitmapFonts[7] = new BitmapFont(Gdx.files.internal("fonts/55.fnt"));
        bitmapFonts[8] = new BitmapFont(Gdx.files.internal("fonts/60.fnt"));
        bitmapFonts[9] = new BitmapFont(Gdx.files.internal("fonts/65.fnt"));
        bitmapFonts[10] = new BitmapFont(Gdx.files.internal("fonts/70.fnt"));
        bitmapFonts[11] = new BitmapFont(Gdx.files.internal("fonts/75.fnt"));
        bitmapFonts[12] = new BitmapFont(Gdx.files.internal("fonts/80.fnt"));
        bitmapFonts[13] = new BitmapFont(Gdx.files.internal("fonts/85.fnt"));
        bitmapFonts[14] = new BitmapFont(Gdx.files.internal("fonts/90.fnt"));
        bitmapFonts[15] = new BitmapFont(Gdx.files.internal("fonts/95.fnt"));
        bitmapFonts[16] = new BitmapFont(Gdx.files.internal("fonts/100.fnt"));
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
        txtRotatingStar = new Texture(Gdx.files.internal("rotstar.png"));

        switch (resolutions) {
            case LowRes:
                map = new EnumMap<>(GObjects.class);
                map.put(GObjects.PlaneB, new TextureRegion(txtLowRes, 304, 50, 100, 46));
                map.put(GObjects.PlaneG, new TextureRegion(txtLowRes, 406, 2, 100, 46));
                map.put(GObjects.PlaneR, new TextureRegion(txtLowRes, 304, 2, 100, 46));
                map.put(GObjects.Grass, new TextureRegion(txtLowRes, 2, 188, 294, 52));
                map.put(GObjects.Bullet, new TextureRegion(txtShot, 0, 0, 256, 127));
                map.put(GObjects.Cloud, new TextureRegion(txtLowRes, 2, 2, 300, 184));
                map.put(GObjects.Star, new TextureRegion(txtLowRes, 406, 50, 26, 26));
                map.put(GObjects.Heart, new TextureRegion(txtRotatingHeartHi, 0, 0, 50, 50));
                all.put(Resolutions.LowRes, map);
                break;
            case HiRes:
                map = new EnumMap<>(GObjects.class);
                map.put(GObjects.PlaneB, new TextureRegion(txtHiRes, 2, 2, 594, 275));
                map.put(GObjects.PlaneG, new TextureRegion(txtHiRes, 2, 370, 600, 278));
                map.put(GObjects.PlaneR, new TextureRegion(txtHiRes, 604, 370, 600, 278));
                map.put(GObjects.Grass, new TextureRegion(txtHiRes, 1200, 2, 600, 104));
                map.put(GObjects.Bullet, new TextureRegion(txtShot, 0, 0, 256, 127));
                map.put(GObjects.Cloud, new TextureRegion(txtHiRes, 598, 2, 600, 366));
                map.put(GObjects.Star, new TextureRegion(txtHiRes, 1206, 370, 100, 100));
                map.put(GObjects.Heart, new TextureRegion(txtRotatingHeartHi, 0, 0, 100, 100));
                all.put(Resolutions.HiRes, map);
                break;
        }
    }

    // On creation
    @Override
    public void create() {
        curH = Gdx.graphics.getHeight();
        curW = Gdx.graphics.getWidth();
        Moveable.totalWidth = curW > curH ? curW : curH;
        Moveable.totalHeight = curW > curH ? curH : curW;
        DrawableText.currentH = Moveable.totalHeight;
        DrawableText.currentW = Moveable.totalWidth;
        Moveable.iNative = iNative;
        spriteBatch = new SpriteBatch();
        loadFonts();
        updateCamera();
        // Load texture
        txtHiRes = new Texture(Gdx.files.internal("x2.png"));
        txtLowRes = new Texture(Gdx.files.internal("x1.png"));
        explosions.put(Resolutions.LowRes, new Texture(Gdx.files.internal("explosion_x1.png")));
        explosions.put(Resolutions.HiRes, new Texture(Gdx.files.internal("explosion_x2.png")));
        txtShot = new Texture(Gdx.files.internal("shot.png"));
        txtBombHi = new Texture(Gdx.files.internal("bombs_x2.png"));
        txtBombLow = new Texture(Gdx.files.internal("bombs_x1.png"));
        txtrotatingHeartLow = new Texture(Gdx.files.internal("heart_x1.png"));
        txtRotatingHeartHi = new Texture(Gdx.files.internal("heart_x2.png"));
        // Create Sprites for EVERY supported resolution
        putTextures(Resolutions.LowRes);
        putTextures(Resolutions.HiRes);
        // Set resolution according to H/W
        setCurrentResolutions(iNative.prefferredResolution(this));
        iNative.Setup();
        // Set up time
        Time = new GameTime();
        setDebug(false, false);
        dtWelcome = new DrawableText(bitmapFonts, iNative.WelcomeMessage(), curH * 0.9f, false, curW / 2, curH / 2 + curH / 20, IDrawable.Anchor.MiddleMiddle, Color.WHITE);
        powerUpStart = new BaseAnimation[3];
        powerUpStart[0] = getRotatingHeart(new Vector2(curW, curH / 10), new Vector2(1, 0), -4, 100);
        powerUpStart[1] = getRotatingStar(new Vector2(curW + 2 * powerUpStart[0].getWidth(), 100), new Vector2(1, 0), -4, 100);
        powerUpStart[2] = getRotatingBomb(new Vector2(curW + 4 * powerUpStart[0].getWidth(), 100), new Vector2(1, 0), -4, 100);
    }

    // On pause
    @Override
    public void pause() {
        Time.Pause();
        GSlast = GScurrent;
        GScurrent = GameState.Paused;
    }

    // On resume
    @Override
    public void resume() {
        GScurrent = GSlast;
        GSlast = GameState.Paused;
        Time.Resume();
    }

    long nextEnemy = 0, MSbetweenEnemies;
    int SCORE, LIVES, HIGHSCORE;
    long MS_AcceptNext = -1;
    int nextPowerUp;
    int LastElapsedSecondForPowerUpStar;

    // Update all the game objects
    public void Update(GameTime.GameTimeArgs gameTime) {
        iNative.letQuit();
        switch (GScurrent) {
            case FirstFrame:
                HIGHSCORE = iNative.getHighScore();
                GSlast = GScurrent;
                GScurrent = GameState.Start;
                break;
            case Start:
                if (iNative.firstTouch()) {
                    GSlast = GScurrent;
                    GScurrent = GameState.PrepareGame;
                }
                for (int i = 0; i < powerUpStart.length; i++) {
                    powerUpStart[i].Update(gameTime);
                    if (powerUpStart[i].rightMost() < 0)
                        powerUpStart[i].setPosition(IDrawable.Anchor.LowLeft, curW, powerUpStart[i].getHeight());
                }
                break;
            case PrepareGame:
                user = new UserPlane(getExplosionForPlanes(), getBombForPlanes(true), requestTextureRegion(GObjects.PlaneR), curW, curH, iNative);
                userBombs = new ArrayList<>();
                enemyBombs = new ArrayList<>();
                enemyPlanes = new ArrayList<>();
                ALexplosions = new ArrayList<>();
                powerUps = new ArrayList<>();
                MSbetweenEnemies = 3500;
                SCORE = 0;
                LIVES = 3;
                nextPowerUp = 10;
                LastElapsedSecondForPowerUpStar = 0;
                GSlast = GScurrent;
                GScurrent = GameState.InGame;
                dtScore = new DrawableText(bitmapFonts, "Score: 0     Highscore: "
                        + HIGHSCORE, curH / 10, false, 4, curH - 4,
                        IDrawable.Anchor.TopLeft);
                clouds = new CloudManager((int) (curW / 128 + 0.5f), requestTextureRegion(GObjects.Cloud), curW, 2f * curH / 3, curH, curH / 3);
                grass = new GrassManager(requestTextureRegion(GObjects.Grass),
                        curH / 15f, curW);
                availableBombs = new BombDrawer(user, new Drawable(requestTextureRegion(GObjects.Bullet), Vector2.Zero, IDrawable.Anchor.LowLeft), 4, 0.9f * curH - 4, curW);
                availableLives = new LifeDrawer(new Drawable(requestTextureRegion(GObjects.Heart), Vector2.Zero, IDrawable.Anchor.LowLeft), 4, 0.85f * curH - 4, curH);
                availableLives.setLives(LIVES);
                // Last thing: Start time
                Time.Restart();
                break;

            case InGame:
                // Update PowerUps
                // Add PowerUps
                if (gameTime.ElapsedSeconds > nextPowerUp) {
                    nextPowerUp += 20;
                    int rand = Helper.getRandomInRange(0, 9);
                    if (rand <= 3)
                        powerUps.add(new PowerUpBomb(currentResolution == Resolutions.HiRes ? txtBombHi : txtBombLow, new Vector2(curW, 9 * curH / 10 * (float) Math.random()), new Vector2(1, 0), -8, currentResolution == Resolutions.HiRes ? 100 : 50, currentResolution == Resolutions.HiRes ? 100 : 50, 3, 5, 10, curH / 10f, true, 1));
                    else
                        powerUps.add(new PowerUpExtraLife(currentResolution == Resolutions.HiRes ? txtRotatingHeartHi : txtrotatingHeartLow, new Vector2(curW, 9 * curH / 10 * (float) Math.random()), new Vector2(1, 0), -8, currentResolution == Resolutions.HiRes ? 100 : 50, currentResolution == Resolutions.HiRes ? 100 : 50, 3, 5, 10, curH / 10f, true, 0));
                }
                if (gameTime.ElapsedSeconds % 60 == 0
                        && gameTime.ElapsedSeconds != LastElapsedSecondForPowerUpStar) {
                    powerUps.add(new PowerUpStar(txtRotatingStar, new Vector2(curW, 9 * curH / 10 * (float) Math.random()), new Vector2(1, 0), -8, 100, 100, 3, 2, 5, curH / 10f, true, 1));
                    LastElapsedSecondForPowerUpStar = gameTime.ElapsedSeconds;
                }
                for (int i = 0; i < powerUps.size(); i++) {
                    PowerUp p = powerUps.get(i);
                    if (p.rightMost() < 0) {
                        powerUps.remove(i);
                        i--;
                        if (i < 0)
                            i = 0;
                    } else
                        p.Update(gameTime);
                }
                clouds.Update(gameTime);
                grass.Update(gameTime);
                // Update User
                user.Update(gameTime);
                if (user.wantShoot(gameTime))
                    userBombs.add(user.getShot());
                // Update UserBombs and delete them, when they are out of screen
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
                    nextEnemy += (MSbetweenEnemies -= MSbetweenEnemies > 1500 ? 100 : 0);
                    enemyPlanes.add(getEnemyPlane());
                    user.addFreeShot();
                }
                nextEnemy -= gameTime.ELapsedMSSinceLastFrame;
                for (int i = 0; i < enemyPlanes.size(); i++) {
                    EnemyPlane plane = enemyPlanes.get(i);
                    plane.Update(gameTime);
                    if (plane.rightMost() < 0) {
                        // EnemyPlane out of screen
                        enemyPlanes.remove(i);
                        enemyPlanes.add(getEnemyPlane());
                        i--;
                    } else {
                        if (plane.wantShoot(gameTime))
                            enemyBombs.add(plane.getShot());
                    }
                }
                // Update Enemybombs and delete them, when they are out of screen
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
            /*
             * Intersection testing
			 */

                // User plane vs. enemy planes
                for (int i = 0; i < enemyPlanes.size(); i++) {
                    if (user.Intersects(enemyPlanes.get(i))) {
                        updateLives(1);
                        ALexplosions.add(enemyPlanes.get(i).getExplosion());
                        enemyPlanes.remove(i);
                        i--;
                        if (i < 0)
                            i = 0;

                    }
                }
                // User plane vs. enemy Bombs
                for (int i = 0; i < enemyBombs.size(); i++) {
                    if (user.Intersects(enemyBombs.get(i))) {
                        updateLives(1);
                        enemyBombs.remove(i);
                        i--;
                        if (i < 0)
                            i = 0;

                    }
                }
                // User bombs vs. Enemy planes
                for (int b = 0; b < userBombs.size(); b++) {
                    for (int p = 0; p < enemyPlanes.size() && b < userBombs.size(); p++) {
                        if (userBombs.get(b).Intersects(enemyPlanes.get(p))) {
                            userBombs.remove(b);
                            b--;
                            if (b < 0)
                                b = 0;
                            updateScore(1);
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
                // User plane vs. PowerUps
                for (int i = 0; i < powerUps.size(); i++) {
                    PowerUp p = powerUps.get(i);
                    if (p.Intersects(user)) {
                        if (p instanceof PowerUpExtraLife) {
                            updateLives(-1); // Remove one life
                        } else if (p instanceof PowerUpBomb) {
                            updateScore(enemyPlanes.size());
                            for (EnemyPlane e : enemyPlanes)
                                ALexplosions.add(e.getExplosion());
                            enemyPlanes.clear();
                            enemyBombs.clear();
                        } else if (p instanceof PowerUpStar)
                            updateScore(5);
                        powerUps.remove(i);
                        i--;
                        if (i < 0)
                            i = 0;
                    }
                }

                break;
            case PrepearScore:
                GSlast = GScurrent;
                GScurrent = GameState.GameOver;
                dtGameover = new DrawableText(bitmapFonts, iNative.GameOverMessage(SCORE, SCORE >= HIGHSCORE), 0.95f * curW, true, curW / 2f, 2f / 3 * curH, IDrawable.Anchor.MiddleMiddle, Color.WHITE);
                MS_AcceptNext = gameTime.ElapsedMilliSecond + 500;
                if (SCORE >= HIGHSCORE) {
                    iNative.saveScore(SCORE);
                    HIGHSCORE = SCORE;
                }
                break;
            case GameOver:
                if (gameTime.ElapsedMilliSecond > MS_AcceptNext
                        && iNative.continueStagesWorkflow()) {
                    GSlast = GScurrent;
                    GScurrent = GameState.PrepareGame;
                }
                break;
        }
    }

    private void updateScore(int add) {
        SCORE += add;
        if (HIGHSCORE < SCORE)
            HIGHSCORE = SCORE;
        dtScore.setText("Score: " + SCORE + "     Highscore: " + HIGHSCORE);
    }

    private void updateLives(int remove) {
        LIVES -= remove;
        availableLives.setLives(LIVES);
        if (LIVES <= 0)
            GameOver();
    }

    private void GameOver() {
        GSlast = GScurrent;
        GScurrent = GameState.PrepearScore;
    }

    // Draw all the game objects
    public void Draw() {
        switch (GScurrent == GameState.Paused || GScurrent == GameState.PrepareGame || GScurrent == GameState.PrepearScore ? GSlast : GScurrent) {
            case Start:
                dtWelcome.Draw(spriteBatch);
                for (int i = 0; i < powerUpStart.length; i++)
                    powerUpStart[i].Draw(spriteBatch);
                break;
            case InGame:
                // BG
                grass.Draw(spriteBatch);
                clouds.Draw(spriteBatch);
                for (Bomb b : userBombs)
                    b.Draw(spriteBatch);
                for (Bomb b : enemyBombs)
                    b.Draw(spriteBatch);
                for (PowerUp p : powerUps)
                    p.Draw(spriteBatch);
                for (EnemyPlane e : enemyPlanes)
                    e.Draw(spriteBatch);
                user.Draw(spriteBatch);
                for (SingleAnimation s : ALexplosions)
                    s.Draw(spriteBatch);
                availableBombs.Draw(spriteBatch);
                availableLives.Draw(spriteBatch);
                dtScore.Draw(spriteBatch);
                // FG
                break;
            case GameOver:
                dtGameover.Draw(spriteBatch);
                break;
        }
    }

    private SingleAnimation getExplosionForPlanes() {
        return new SingleAnimation(explosions.get(currentResolution), Vector2.Zero, Vector2.Zero, 0, currentResolution == Resolutions.HiRes ? 222 : 111, 3, 5, 4, 200);
    }

    private Bomb getBombForPlanes(boolean user) {
        return new Bomb(requestTextureRegion(GObjects.Bullet), Vector2.Zero, Vector2.Zero, 0, IFullGameObject.Anchor.MiddleLeft, user, curH);
    }

    private EnemyPlane getEnemyPlane() {
        return new EnemyPlane(getExplosionForPlanes(), getBombForPlanes(false), getRandomPlaneTextureRegion(), curH, curW);
    }

    private BaseAnimation getRotatingStar(Vector2 position, Vector2 direction,
                                          float speed, float setWidth) {
        return new BaseAnimation(txtRotatingStar, new Vector2(position), new Vector2(direction), speed, 100, 100, 3, 2, 5, setWidth, true, 1);
    }

    private BaseAnimation getRotatingHeart(Vector2 position, Vector2 direction, float speed, float setWidthHight) {
        return new BaseAnimation(currentResolution == Resolutions.HiRes ? txtRotatingHeartHi : txtrotatingHeartLow, new Vector2(position), new Vector2(direction), speed, currentResolution == Resolutions.HiRes ? 100 : 50, currentResolution == Resolutions.HiRes ? 100 : 50, 3, 5, 10, setWidthHight, true, 1);
    }

    private BaseAnimation getRotatingBomb(Vector2 position, Vector2 direction, float speed, float setWidthHight) {
        return new BaseAnimation(currentResolution == Resolutions.HiRes ? txtBombHi : txtBombLow, new Vector2(position), new Vector2(direction), speed, currentResolution == Resolutions.HiRes ? 100 : 50, currentResolution == Resolutions.HiRes ? 100 : 50, 3, 5, 10, setWidthHight, true, 0);
    }

    private TextureRegion getRandomPlaneTextureRegion() {
        GObjects rand = null;
        switch (Helper.getRandomInRange(0, 2)) {
            case 0:
                rand = GObjects.PlaneR;
                break;
            case 1:
                rand = GObjects.PlaneG;
                break;
            default:
                rand = GObjects.PlaneB;
                break;
        }
        return requestTextureRegion(rand);
    }

    @Override
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

    @Override
    public void dispose() {
        spriteBatch.dispose();
        txtLowRes.dispose();
        txtHiRes.dispose();
        super.dispose();
    }

    private void setDebug(boolean isDebugDRAWABLE, boolean isDebugPLANE) {
        // Set Debug for Drawable
        BasicPlane.blackDebug = Drawable.blackDebug = new TextureRegion(
                new Texture(Gdx.files.internal("black.png")));
        BasicPlane.DEBUG = isDebugPLANE;
        Drawable.DEBUG = isDebugDRAWABLE;
    }
}
