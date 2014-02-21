package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bomb implements IMoveableGameObject, IIntersectable {
    private Moveable bomb;
    private static final float relativeHeight = 1 / 75f;

    public Bomb(Bomb bomb, boolean flipV) {
        this(bomb.bomb.getTextureRegion(), new Vector2(bomb.bomb.getX(), bomb.bomb.getY()), bomb.bomb.speed, bomb.bomb.getFspeed(), Anchor.LowLeft, flipV, bomb.bomb.getHeight() / relativeHeight);
    }

    public Bomb(TextureRegion region, Vector2 position, Vector2 direction, float speed, Anchor anchor, boolean flipV, float curHeight) {
        bomb = new Moveable(region, position, direction, speed, anchor, curHeight * relativeHeight, false, false, flipV);
        //System.out.println(bomb.toString());
    }

    public TextureRegion getTextureRegion() {
        return bomb.getTextureRegion();
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
        bomb.Update(gameTimeArgs);
    }

    @Override
    public void Draw(SpriteBatch spriteBatch) {
        bomb.Draw(spriteBatch);
    }

    @Override
    public void setScale(float newScale) {
        bomb.setScale(newScale);
    }

    public void setSpeed(Vector2 direction, float speed) {
        bomb.setSpeed(direction, speed);
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
