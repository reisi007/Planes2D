package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class UserPlane extends BasicPlane {
    private float maximumMovementPX;
    private INative iNative;
    private MovementContainer movementContainer;
    private static final float percentOfHeightOneFrame = 1 / 111f;
    private float minH = 0;
    private boolean wantShoot = false;
    private long timeSincelastShot;

    public UserPlane(SingleAnimation explosion, Bomb bomb, TextureRegion textureRegion, float currentW, float currentH, INative iNative) {
        super(explosion, bomb, textureRegion, new Vector2(currentW / 50f, currentH / 2), Vector2.Zero, 0, Anchor.MiddleLeft, currentH / 5f, true, currentH);
        this.maximumMovementPX = currentH * percentOfHeightOneFrame;
        this.iNative = iNative;
        movementContainer = iNative.Input();
        maxH = currentH;
    }

    public boolean wantShoot() {
        if (wantShoot) {
            wantShoot = false;
            return true;
        }
        return false;
    }

    @Override
    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        //Do update UserPlane
        movementContainer = iNative.Input();
        timeSincelastShot += gameTimeArgs.ELapsedMSSinceLastFrame;
        if (movementContainer.doShoot && (timeSincelastShot >= MSbetweenShots)) {
            timeSincelastShot -= MSbetweenShots;
            wantShoot = true;
        }
        updatePosition(movementContainer.Movement);
       /*if (gameTimeArgs.ElapsedFrames % 20 == 0) {
            //System.out.println("Delta: +Up:\t" + (-getY(Anchor.MiddleMiddle) + movementContainer.Movement));
            System.out.println("Mouse:\t" + movementContainer.Movement);
        }
        //  System.out.println("UPlane Y: " + getY(Anchor.LowLeft));*/
        super.Update(gameTimeArgs);
    }

    @Override
    public void updatePosition(float delta) {
        super.updatePosition(maximumMovementPX * delta);
    }
}
