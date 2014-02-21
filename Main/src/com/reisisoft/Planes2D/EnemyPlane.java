package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class EnemyPlane extends BasicPlane {
    public EnemyPlane(SingleAnimation singleAnimation, Bomb bomb, TextureRegion textureRegion, float curHeight, float curWidth) {
        super(singleAnimation, bomb, textureRegion, new Vector2(curWidth, Helper.getRandomInRange(0, 4 * curHeight / 5f)), new Vector2(-1, 0), Helper.getRandomInRange(0.8f, 1.1f) * 4, Anchor.LowLeft, curHeight / 5f, false, curHeight);
    }

    public Bomb getShot() {
        return getShot(false);
    }

    public boolean wantShoot(GameTime.GameTimeArgs gameTimeArgs) {
        if (timeSincelastShot >= MSbetweenShots) {
            timeSincelastShot -= MSbetweenShots;
            return true;
        }
        return false;
    }
}
