package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class SingleAnimation extends BaseAnimation {

    public SingleAnimation(Texture texture, Vector2 position, Vector2 direction, float speed, int size, int changeAfter, int NRows, int NColumns, float setHeight) {
        this(texture, position, direction, speed, size, size, changeAfter, NRows, NColumns, setHeight, false);
    }

    public SingleAnimation(Texture texture, Vector2 position, Vector2 direction, float speed, int sizeX, int sizeY, int changeAfter, int NRows, int NColumns, float setSide, boolean setWidth) {
        super(texture, position, direction, speed, sizeX, sizeY, changeAfter, NRows, NColumns, setSide, setWidth);
    }


    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        if (runs < 1)
            super.Update(gameTimeArgs);
        else
            super.setRegion((Nrows - 1) * sizeX, (NColumns - 1) * sizeY, sizeX, sizeY);
    }

    public void Draw(SpriteBatch spriteBatch) {
        if (!isFinished())
            super.Draw(spriteBatch);
    }

    public boolean isFinished() {
        return runs >= 1;
    }
}
