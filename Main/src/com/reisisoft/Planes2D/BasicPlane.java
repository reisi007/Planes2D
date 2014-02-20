package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class BasicPlane implements IMoveableGameObject, IIntersectable {
    public static TextureRegion blackDebug = null;
    public static boolean DEBUG = false;
    protected Moveable plane;
    private SingleAnimation baseAnimation;
    private boolean isFlipped;
    private Rectangle[] bounds = new Rectangle[3];

    public BasicPlane(SingleAnimation explosion, TextureRegion sprite, Vector2 position, Vector2 direction, float speed, Anchor anchor, float setHeight, boolean flipV) {
        plane = new Moveable(sprite, position, direction, speed, anchor, setHeight, isFlipped = flipV);
        baseAnimation = explosion;
        UpdateRectangle();
    }

    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        plane.Update(gameTimeArgs);
        UpdateRectangle();
    }

    public void Draw(SpriteBatch spriteBatch) {
        if (DEBUG)
            for (int i = 0; i < bounds.length; i++)
                Drawable.DrawDebug(spriteBatch, blackDebug, bounds[i]);
        plane.Draw(spriteBatch);
    }

    @Override
    public void setPosition(Anchor a, float x, float y) {
        plane.setPosition(a, x, y);
    }

    @Override
    public void updatePosition(float x, float y) {
        plane.updatePosition(x, y);
    }

    public void updatePosition(float delta) {
        updatePosition(0, delta);
    }

    @Override
    public void setScale(float newScale) {
        plane.setScale(newScale);
    }

    public SingleAnimation getAnimation() {
        baseAnimation.setPosition(Anchor.LowLeft, plane.getX(), plane.getY());
        return baseAnimation;
    }


    @Override
    public boolean Intersects(Rectangle[] o) {
        for (int i = 0; i < bounds.length; i++)
            for (int y = 0; y < o.length; y++)
                if (o[y].overlaps(bounds[i]))
                    return true;
        return false;
    }

    @Override
    public Rectangle[] getBounds() {
        return bounds;
    }

    @Override
    public void UpdateRectangle() {
        if (isFlipped) {
            bounds[0] = new Rectangle(plane.getX() + plane.getWidth() / 2, plane.getY() + plane.getHeight() / 5f, plane.getWidth() / 2f, 3 / 5f * plane.getHeight());
            bounds[1] = new Rectangle(plane.getX() + plane.getWidth() / 6, plane.getY() + 3 / 10f * plane.getHeight(), plane.getWidth() / 3, 4 / 10f * plane.getHeight());
            bounds[2] = new Rectangle(plane.getX(), plane.getY() + 5f / 10 * plane.getHeight(), plane.getWidth() / 6f, 4f / 10 * plane.getHeight());
        } else {
            bounds[0] = new Rectangle(plane.getX(), plane.getY() + plane.getHeight() / 5f, plane.getWidth() / 2f, 3 / 5f * plane.getHeight());
            bounds[1] = new Rectangle(plane.getX() + plane.getWidth() / 2, plane.getY() + 3 / 10f * plane.getHeight(), plane.getWidth() / 3, 4 / 10f * plane.getHeight());
            bounds[2] = new Rectangle(plane.getX() + 5f / 6 * plane.getWidth(), plane.getY() + 5f / 10 * plane.getHeight(), plane.getWidth() / 6f, 4f / 10 * plane.getHeight());

        }

    }
}
