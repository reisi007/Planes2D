package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IGameObject {

    public static enum Anchor {TopLeft, MiddleLeft, LowLeft, TopMiddle, MiddleMiddle, LowMiddle, TopRight, MiddleRight, LowRight}

    public void Update(GameTime.GameTimeArgs gameTimeArgs);

    public void Draw(SpriteBatch spriteBatch);

    public void setPosition(Anchor a, float x, float y);

    public void setScale(float newScale);
}
