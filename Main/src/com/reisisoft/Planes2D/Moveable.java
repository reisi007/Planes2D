package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Moveable extends Drawable {
    protected Vector2 speed;
    private float fspeed;

    public Moveable(Moveable m) {
        this(m.getTextureRegion(), new Vector2(m.x, m.y), m.speed, m.fspeed, Anchor.LowLeft, m.w, true, false, false);
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
        x += speed.x;
        y += speed.y;
        super.Update(gameTimeArgs);
    }
}
