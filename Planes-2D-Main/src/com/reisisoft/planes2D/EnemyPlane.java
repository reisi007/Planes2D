package com.reisisoft.planes2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class EnemyPlane extends BasicPlane {
    public EnemyPlane(SingleAnimation singleAnimation, Bomb bomb, TextureRegion textureRegion, float curHeight, float curWidth) {
        super(singleAnimation, bomb, textureRegion, new Vector2(curWidth, Helper.getRandomInRange(0, 4 * curHeight / 5f)), new Vector2(-1, 0), Helper.getRandomInRange(0.8f, 1.1f) * 4, Anchor.LowLeft, curHeight / 5f, false, curHeight);
    }

    public Bomb getShot() {
        Bomb b = getShot(false);
        b.setSpeed(new Vector2(-1, 0), 1.5f * plane.getFspeed());
        return b;
    }

    public boolean wantShoot(GameTime.GameTimeArgs gameTimeArgs) {
        if (timeIndicatingShots >= MSbetweenShots()) {
            timeIndicatingShots -= MSbetweenShots();
            return true;
        }
        return false;
    }

	@Override
	protected long MSbetweenShots() {
		return 2500L;
	}
}
