package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SingleAnimation extends BaseAnimation {

    public SingleAnimation(Texture texture, Coordinates position, Coordinates direction, int size, Coordinates Npictures, int changeAfter) {
        this(texture, position, direction, size, size, Npictures, changeAfter);
    }

    public SingleAnimation(Texture texture, Coordinates position, Coordinates direction, int sizeX, int sizeY, Coordinates Npictures, int changeAfter) {
        super(texture, position, direction, sizeX, sizeY, Npictures, changeAfter);
    }


    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        if (runs < 1)
            super.Update(gameTimeArgs);
    }

    public void Draw(SpriteBatch spriteBatch) {
        if (!isFinished())
            super.Draw(spriteBatch);
    }

    public boolean isFinished() {
        return runs >= 1;
    }
}
