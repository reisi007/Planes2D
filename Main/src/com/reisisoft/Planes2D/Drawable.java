package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/*
A class made for easyfied drawing
 */
public class Drawable implements IIntersectable, IDrawable {
    protected Sprite sprite;

    public Drawable(Sprite sprite, Vector2 position, Anchor anchor) {
        this(sprite, position, anchor, sprite.getWidth());
    }

    public Drawable(Sprite sprite, Vector2 position, Anchor anchor, float setWidth) {
        this(sprite, position, anchor, setWidth, true, false, false);
    }

    public Drawable(Sprite sprite, Vector2 position, Anchor anchor, float setWidth, boolean flipV) {
        this(sprite, position, anchor, setWidth, true, false, flipV);
    }

    public Drawable(Sprite sprite, Vector2 position, Anchor anchor, float setSide, boolean setWidth, boolean flipH, boolean flipV) {
        // Adapt x coordinate
        switch (anchor) {
            case TopMiddle:
            case MiddleMiddle:
            case LowMiddle:
                position.x -= sprite.getWidth() / 2;
                break;
            case TopRight:
            case MiddleRight:
            case LowRight:
                position.x -= sprite.getWidth();
                break;
            default: //*L
                break;
        }
        //Adpat y coordinate
        switch (anchor) {
            case TopLeft:
            case TopMiddle:
            case TopRight:
                position.y -= sprite.getHeight();
                break;
            case MiddleLeft:
            case MiddleMiddle:
            case MiddleRight:
                position.y -= sprite.getHeight() / 2;
                break;
            default: //L*
                break;
        }
        this.sprite = new Sprite(sprite);
        this.sprite.setOrigin(0, 0);
        this.sprite.setPosition(position.x, position.y);
        this.sprite.scale(setSide / (setWidth ? sprite.getWidth() : sprite.getHeight()));
        this.sprite.flip(flipV, flipH);
    }

    public void Draw(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

    public boolean Intersects(IIntersectable o) {
        return getBounds().overlaps(o.getBounds());
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

}
