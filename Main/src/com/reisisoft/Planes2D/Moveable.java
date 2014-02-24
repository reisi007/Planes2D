package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Moveable extends Drawable implements IMoveableGameObject {
    protected Vector2 speed;
    private float fspeed;
    public static INative iNative;

    public Moveable(Moveable m) {
        this(m.getTextureRegion(), new Vector2(m.getX(), m.getY()), m.speed, m.fspeed, Anchor.LowLeft, m.getWidth(), true, false, false);
    }

    public Moveable(TextureRegion sprite, Vector2 position, Vector2 direction, float speed, Anchor anchor, float setSide, boolean setWidth, boolean flipH, boolean flipV) {
        super(sprite, position, anchor, setSide, setWidth, flipH, flipV);
        direction.nor();
        direction.scl(fspeed = speed);
        this.speed = direction;
    }

    public Moveable(TextureRegion sprite, Vector2 position, Vector2 direction, float speed, Anchor anchor, float setWidth, boolean flipV) {
        this(sprite, position, direction, speed, anchor, setWidth, true, false, flipV);
    }

    public Moveable(TextureRegion sprite, Vector2 position, Vector2 direction, float speed, Anchor anchor, float setWidth) {
        this(sprite, position, direction, speed, anchor, setWidth, false);
    }

    public Moveable(TextureRegion sprite, Vector2 position, Vector2 direction, float speed, Anchor anchor) {
        this(sprite, position, direction, speed, anchor, sprite.getRegionWidth());
    }

    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        updateX(speed.x);
        updateY(speed.y);
        super.Update(gameTimeArgs);
    }

    public void setSpeed(Vector2 direction, float fspeed) {
        direction.nor();
        speed = direction;
        speed.scl(this.fspeed = fspeed);
    }

    public void updatePosition(float x, float y) {
        updateX(x);
        updateY(y);
    }

    public void updateX(float x) {
        super.updateX(x * getSpeedXModifier());
    }

    public void updateY(float y) {
        super.updateY(y * getSpeedYModifier());
    }

    public float getFspeed() {
        return fspeed;
    }

    @Override
    public String toString() {
        return "Moveable (" + super.toString() + ")\tSpeed:\t" + speed.x + " | " + speed.y;
    }

    public static float totalWidth, totalHeight;

    public static float getSpeedXModifier() {
        return iNative==null?1:iNative.speedX()*(totalWidth / 1280f) * (60f / Helper.getFPS());
    }

    public static float getSpeedYModifier() {
        return iNative==null?1:iNative.speedX()*(totalHeight / 720f) * (60f / Helper.getFPS());
    }

}
