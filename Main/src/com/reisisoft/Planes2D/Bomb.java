package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bomb implements IMoveableGameObject, IIntersectable {
    private Moveable bomb;
    private static final float relativeHeight = 1 / 1000f;

    public Bomb(TextureRegion region, Vector2 position, Vector2 direction, float speed, Anchor anchor, boolean flipV, float curHeight) {
        bomb = new Moveable(region, position, direction, speed, anchor, curHeight * relativeHeight, flipV);
    }

    @Override
    public void setPosition(Anchor a, float x, float y) {
        bomb.setPosition(a, x, y);
    }

    @Override
    public void updatePosition(float x, float y) {
        bomb.updatePosition(x, y);
    }

    @Override
    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        UpdateRectangle();
    }

    @Override
    public void Draw(SpriteBatch spriteBatch) {
        bomb.Draw(spriteBatch);
    }

    @Override
    public void setScale(float newScale) {
        bomb.setScale(newScale);
    }

    @Override
    public boolean Intersects(Rectangle[] o) {
        return bomb.Intersects(o);
    }

    @Override
    public Rectangle[] getBounds() {
        return bomb.getBounds();
    }

    @Override
    public void UpdateRectangle() {
        bomb.UpdateRectangle();
    }
}
