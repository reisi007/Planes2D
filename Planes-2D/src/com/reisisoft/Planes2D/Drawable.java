package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/*
A class made for easyfied drawing
 */
public class Drawable implements IIntersectable, IFullGameObject {
    public static TextureRegion blackDebug = null;
    public static boolean DEBUG = false;
    private TextureRegion textureRegion;
    private float x, y, w, h;
    private Rectangle bounds;

    public Drawable(Drawable drawable) {
        this(drawable.textureRegion, new Vector2(drawable.x, drawable.y), Anchor.LowLeft, drawable.w, true, drawable.textureRegion.isFlipX(), drawable.textureRegion.isFlipY());
    }

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

    public static void setSclae(float newScale, IFullGameObject object) {
        object.setScale(newScale);
    }

    public void setPosition(Anchor a, float x, float y) {
        // Adapt x coordinate
        this.x = Helper.relativeXposition(a, x, w);
        //Adpat y coordinate
        this.y = Helper.relativeYposition(a, y, h);
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public boolean Intersects(IIntersectable iIntersectable) {
        return Intersects(this, iIntersectable);
    }

    public static boolean Intersects(IIntersectable a, IIntersectable b) {
        Rectangle[] ra = a.getBounds(), rb = b.getBounds();
        for (int j = 0; j < ra.length; j++)
            for (int i = 0; i < rb.length; i++)
                if (ra[j].overlaps(rb[i]))
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
