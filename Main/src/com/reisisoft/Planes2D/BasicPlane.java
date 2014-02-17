package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Florian on 17.02.14.
 */
public class BasicPlane implements IDrawable, IUpdateAble {
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
}
