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
    private float x, y, w, h;
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
        setScale(setSide / (setWidth ? w : h));
        setPosition(anchor, position.x, position.y);
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
            DrawDebug(spriteBatch, blackDebug, bounds);
        }
        spriteBatch.draw(textureRegion, x, y, w, h);
    }

    public static void DrawDebug(SpriteBatch spriteBatch, TextureRegion textureRegion, Rectangle rectangle) {
        spriteBatch.draw(textureRegion, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
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

    public boolean Intersects(IIntersectable ii) {
        Rectangle[] origin = getBounds(), o = ii.getBounds();
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
        return "Drawable@\tX: " + x + "\tY: " + y + "\tWidth: " + w + "\tHeight: " + h;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return w;
    }

    public float getHeight() {
        return h;
    }

    public void updateX(float f) {
        x += f;
    }

    public void updateY(float f) {
        y += f;
    }
}
