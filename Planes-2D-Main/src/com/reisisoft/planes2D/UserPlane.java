package com.reisisoft.planes2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class UserPlane extends BasicPlane {
    private float maximumMovementPX;
    private INative iNative;
    private MovementContainer movementContainer;
    private static final float percentOfHeightOneFrame = 1 / 111f;
    private boolean wantShoot = false;
    private long nextAllowed;


    public UserPlane(SingleAnimation explosion, Bomb bomb, TextureRegion textureRegion, float currentW, float currentH, INative iNative) {
        super(explosion, bomb, textureRegion, new Vector2(currentW / 50f, currentH / 2), Vector2.Zero, 0, Anchor.MiddleLeft, currentH / 5f, true, currentH);
        this.maximumMovementPX = currentH * percentOfHeightOneFrame;
        this.iNative = iNative;
        movementContainer = iNative.Input();
        maxH = currentH;
        nextAllowed = 300;
    }

    public int ShotsAvailable() {
        return (int) (timeSincelastShot / MSbetweenShots());
    }

    public Bomb getShot() {
        Bomb b = super.getShot(true);
        b.setSpeed(new Vector2(1, 0), 2 * Moveable.getSpeedXModifier());
        return b;
    }

    public boolean wantShoot(GameTime.GameTimeArgs gameTimeArgs) {
        if (wantShoot) {
            wantShoot = false;
            nextAllowed = gameTimeArgs.ElapsedMilliSecond + 333;
            return true;
        }
        return false;
    }

    public void addFreeShot() {
        timeSincelastShot += MSbetweenShots();
    }

    @Override
    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        movementContainer = iNative.Input();
        if (nextAllowed <= gameTimeArgs.ElapsedMilliSecond && movementContainer.doShoot && !wantShoot && (timeSincelastShot >= MSbetweenShots())) {
            timeSincelastShot -= MSbetweenShots();
            wantShoot = true;
        }
        updatePosition(movementContainer.Movement);
        super.Update(gameTimeArgs);
    }

    @Override
    public void updatePosition(float delta) {
        super.updatePosition(maximumMovementPX * delta);
    }

	@Override
	protected long MSbetweenShots() {
		return 6000L;
	}
}
