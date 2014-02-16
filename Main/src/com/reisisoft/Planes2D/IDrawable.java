package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/*
A class made for easyfied drawing
 */
public class IDrawable {
    public static enum Anchor {TopLeft, MiddleLeft, LowLeft, TopMiddle, MiddleMiddle, LowMiddle, TopRight, MiddleRight, LowRight}

    protected Sprite sprite;

    public IDrawable(Sprite sprite, Vector2 position, Anchor anchor, float setWidth) {
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
        sprite.setOrigin(0, 0);
        sprite.setPosition(position.x, position.y);
        sprite.scale(setWidth / sprite.getWidth());
        this.sprite = sprite;
    }

    public IDrawable(Sprite sprite, Vector2 position, Anchor anchor) {
        this(sprite, position, anchor, sprite.getWidth());
    }

    public void Draw(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

}
