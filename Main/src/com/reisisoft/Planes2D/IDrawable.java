package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IDrawable {
    public static enum Anchor {TopLeft, MiddleLeft, LowLeft, TopMiddle, MiddleMiddle, LowMiddle, TopRight, MiddleRight, LowRight}

    public void Update(GameTime.GameTimeArgs gameTimeArgs);

    public void Draw(SpriteBatch spriteBatch);
}
