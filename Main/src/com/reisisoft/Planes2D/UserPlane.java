package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class UserPlane extends BasicPlane {
    private float maximumMovementPX;
    private INative iNative;
    private static final float percentOfHeightOneFrame = 1 / 80f;

    public UserPlane(SingleAnimation explosion, TextureRegion textureRegion, float currentW, float currentH, INative iNative) {
        super(explosion, textureRegion, new Vector2(currentW / 6f, currentH / 2), Vector2.Zero, 0, Anchor.MiddleLeft, currentH / 5f, true);
        maximumMovementPX = currentH * percentOfHeightOneFrame;
        this.iNative = iNative;
    }

    @Override
    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        super.Update(gameTimeArgs);
    }
}
