package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Grass implements IDrawable, IUpdateAble {
    private ArrayList<Moveable> grass = new ArrayList<>();
    private float totalWidth, rightMost = 0;
    private Moveable original;

    public Grass(Sprite sprite, float setHeight, float totalWidth) {
        original = new Moveable(sprite, Vector2.Zero, new Vector2(-1, 0), 2, Anchor.LowLeft, setHeight, false, false, false);
        this.totalWidth = totalWidth;
        while (rightMost < this.totalWidth) {
            grass.add(getMoveable(rightMost));
            rightMost += original.sprite.getWidth();
        }
    }

    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        rightMost += original.speed.x;
        if (rightMost < totalWidth) {
            grass.add(getMoveable(rightMost));
            rightMost += original.sprite.getWidth();
        }
        for (Moveable moveable : grass)
            moveable.Update(gameTimeArgs);
    }

    public void Draw(SpriteBatch spriteBatch) {
        for (Moveable moveable : grass)
            moveable.Draw(spriteBatch);
    }

    private Moveable getMoveable(float x) {
        Moveable tmp = new Moveable(original);
        tmp.sprite.setPosition(x, 0);
        return tmp;
    }
}
