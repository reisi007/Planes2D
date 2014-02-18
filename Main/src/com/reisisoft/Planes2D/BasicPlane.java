package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class BasicPlane implements IGameObject {
    protected Moveable plane;

    public BasicPlane(Sprite sprite, Vector2 position, Vector2 direction, float speed, Anchor anchor, float setHeight) {
        plane = new Moveable(sprite, position, direction, speed, anchor, setHeight, false);
    }

    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        plane.Update(gameTimeArgs);
    }

    public void Draw(SpriteBatch spriteBatch) {
        plane.Draw(spriteBatch);
    }

    @Override
    public void setPosition(Anchor a, float x, float y) {
        plane.setPosition(a, x, y);
    }

    @Override
    public void setScale(float newScale) {
        plane.setScale(newScale);
    }
}
