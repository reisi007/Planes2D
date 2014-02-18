package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Moveable extends Drawable implements IUpdateAble {
    protected Vector2 speed;
    private float fspeed;

    public Moveable(Moveable m) {
        this(m.sprite, new Vector2(m.sprite.getX(), m.sprite.getY()), m.speed, m.fspeed, Anchor.LowLeft, m.sprite.getWidth(), true, false, false);
    }

    public Moveable(Sprite sprite, Vector2 position, Vector2 direction, float speed, Anchor anchor, float setSide, boolean setWidth, boolean flipH, boolean flipV) {
        super(sprite, position, anchor, setSide, setWidth, flipH, flipV);
        direction.nor();
        direction.scl(fspeed = speed);
        this.speed = direction;
    }

    public Moveable(Sprite sprite, Vector2 position, Vector2 direction, float speed, Anchor anchor, float setWidth, boolean flipV) {
        this(sprite, position, direction, speed, anchor, setWidth, true, false, flipV);
    }

    public Moveable(Sprite sprite, Vector2 position, Vector2 direction, float speed, Anchor anchor, float setWidth) {
        this(sprite, position, direction, speed, anchor, setWidth, false);
    }

    public Moveable(Sprite sprite, Vector2 position, Vector2 direction, float speed, Anchor anchor) {
        this(sprite, position, direction, speed, anchor, sprite.getWidth());
    }

    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        sprite.setPosition(sprite.getX() + speed.x, sprite.getY() + speed.y);
    }
}
