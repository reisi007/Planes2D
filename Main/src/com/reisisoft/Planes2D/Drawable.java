package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/*
A class made for easyfied drawing
 */
public class Drawable implements IIntersectable, IGameObject {
    public static TextureRegion blackDebug = null;
    public static boolean DEBUG = false;
    private TextureRegion textureRegion;
    protected float x, y, w, h;
    private Rectangle bounds;

    public Drawable(TextureRegion textureRegion, Vector2 position, Anchor anchor) {
        this(textureRegion, position, anchor, textureRegion.getRegionWidth());
    }

    public Drawable(TextureRegion textureRegion, Vector2 position, Anchor anchor, float setWidth) {
        this(textureRegion, position, anchor, setWidth, true, false, false);
    }

    public Drawable(TextureRegion textureRegion, Vector2 position, Anchor anchor, float setWidth, boolean flipV) {
        this(textureRegion, position, anchor, setWidth, true, false, flipV);
    }

    public Drawable(TextureRegion textureRegion, Vector2 position, Anchor anchor, float setSide, boolean setWidth, boolean flipH, boolean flipV) {
        this.textureRegion = textureRegion;
        w = textureRegion.getRegionWidth();
        h = textureRegion.getRegionHeight();
        setPosition(anchor, position.x, position.y);
        setScale(setSide / (setWidth ? w : h));
        this.textureRegion.flip(flipV, flipH);
        UpdateRectangle();
    }

    public void UpdateRectangle() {
        bounds = new Rectangle(x, y, w, h);
    }

    @Override
    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        UpdateRectangle();
    }

    public void Draw(SpriteBatch spriteBatch) {
        if (DEBUG) {
            spriteBatch.draw(blackDebug, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
        }
        spriteBatch.draw(textureRegion, x, y, w, h);
    }

    public float rightMost() {
        return x + w;
    }

    @Override
    public void setScale(float newScale) {
        setScale(newScale, newScale);
    }

    public void setScale(float sx, float sy) {
        w *= sx;
        h *= sy;
    }

    public static void setSclae(float newScale, IGameObject object) {
        object.setScale(newScale);
    }

    public void setPosition(Anchor a, float x, float y) {
        // Adapt x coordinate
        switch (a) {
            case TopMiddle:
            case MiddleMiddle:
            case LowMiddle:
                x -= w / 2;
                break;
            case TopRight:
            case MiddleRight:
            case LowRight:
                x -= w;
                break;
            default: //*L
                break;
        }
        //Adpat y coordinate
        switch (a) {
            case TopLeft:
            case TopMiddle:
            case TopRight:
                y -= h;
                break;
            case MiddleLeft:
            case MiddleMiddle:
            case MiddleRight:
                y -= h / 2;
                break;
            default: //L*
                break;
        }
        this.x = x;
        this.y = y;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }


    public boolean Intersects(Rectangle[] o) {
        Rectangle[] origin = getBounds();
        for (int j = 0; j < origin.length; j++)
            for (int i = 0; i < o.length; i++)
                if (origin[j].overlaps(o[i]))
                    return true;
        return false;

    }

    public Rectangle[] getBounds() {
        return new Rectangle[]{bounds};
    }

    public String toString() {
        return "X; " + x + " Y: " + y + " W: " + w + "H: " + h;
    }

}
