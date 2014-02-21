package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class CloudManager {
    protected Moveable[] clouds;
    private float bottomRegion, topRegion, totalWidth, height100;
    private Moveable original;

    public CloudManager(int numberOfClouds, TextureRegion sprite, float totalWidth, float bottomRegion, float topRegion, float height100) {
        clouds = new Moveable[numberOfClouds];
        original = new Moveable(sprite, Vector2.Zero, Vector2.Zero, 0, IGameObject.Anchor.LowLeft, height100, false, false, false);
        this.height100 = height100;
        this.totalWidth = totalWidth;
        this.bottomRegion = bottomRegion;
        this.topRegion = topRegion;
        for (int i = 0; i < clouds.length; i++) {
            clouds[i] = getNewCloud();
            clouds[i].updatePosition(3f * i, 0);
        }
    }

    public void Draw(SpriteBatch spriteBatch) {
        for (int i = 0; i < clouds.length; i++)
            clouds[i].Draw(spriteBatch);
    }

    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        for (int i = 0; i < clouds.length; i++) {
            if (clouds[i].rightMost() < -5)
                getCloudPosition(clouds[i]);
            clouds[i].Update(gameTimeArgs);
        }
    }

    private void getCloudPosition(Moveable tmp) {
        getCloudPosition(tmp, totalWidth + 1, 7f / 6 * totalWidth);
    }

    private void getCloudPosition(Moveable tmp, float from, float to) {
        tmp.setScale(getRandomInRange(0.25f, 1f));
        tmp.setSpeed(new Vector2(-1, 0), getRandomInRange(0.5f, 2.5f));
        tmp.setPosition(IGameObject.Anchor.TopLeft, getRandomInRange(from, to), getRandomInRange(bottomRegion, topRegion));
    }

    private Moveable getNewCloud() {
        Moveable tmp = new Moveable(original);
        getCloudPosition(tmp, 0, 4f / 3 * totalWidth);
        return tmp;
    }

    public static float getRandomInRange(float from, float to) {
        if (from > to) {
            float tmp = from;
            from = to;
            to = tmp;
        }
        return (float) ((Math.random() * (to - from)) + from);
    }
}
