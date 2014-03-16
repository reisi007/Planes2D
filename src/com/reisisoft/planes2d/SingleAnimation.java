package com.reisisoft.planes2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class SingleAnimation extends BaseAnimation {
    public SingleAnimation(SingleAnimation singleAnimation) {
        this(singleAnimation.animation.getTextureRegion().getTexture(), new Vector2(singleAnimation.animation.getX(), singleAnimation.animation.getY()), singleAnimation.animation.speed, singleAnimation.animation.getFspeed(), singleAnimation.sizeX, singleAnimation.sizeY, (int) (singleAnimation.changeAfter / GameTime.FRAME + 0.5d), singleAnimation.Nrows, singleAnimation.NColumns, singleAnimation.animation.getHeight(), false);
    }

    public SingleAnimation(Texture texture, Vector2 position, Vector2 direction, float speed, int size, int changeAfter, int NRows, int NColumns, float setHeight) {
        this(texture, position, direction, speed, size, size, changeAfter, NRows, NColumns, setHeight, false);
    }

    public SingleAnimation(Texture texture, Vector2 position, Vector2 direction, float speed, int sizeX, int sizeY, int changeAfter, int NRows, int NColumns, float setSide, boolean setWidth) {
        super(texture, position, direction, speed, sizeX, sizeY, changeAfter, NRows, NColumns, setSide, setWidth);
    }

    @Override
    public void Draw(SpriteBatch spriteBatch) {
        if (!isFinished())
            super.Draw(spriteBatch);
    }

    public boolean isFinished() {
        return runs >= 1;
    }
}
