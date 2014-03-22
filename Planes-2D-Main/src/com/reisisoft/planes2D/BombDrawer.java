package com.reisisoft.planes2D;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BombDrawer implements IDrawable {
    private Drawable[] bombs = new Drawable[25];
    private UserPlane uPlane;
    private int drawTo;

    public BombDrawer(UserPlane userPlane, Drawable bomb, float x, float y, float currentW) {
        uPlane = userPlane;
        Drawable drawable = new Drawable(bomb);
        drawable.setScale(((currentW - bombs.length * 4 - x) / 25f) / drawable.getWidth());
        for (int i = 0; i < bombs.length; i++) {
            bombs[i] = new Drawable(drawable);
            bombs[i].setPosition(Anchor.MiddleLeft, i * (drawable.getWidth() + 4) + x, y);
        }
    }

    public void setUserPlane(UserPlane up) {
        uPlane = up;
    }

    public void Draw(SpriteBatch spriteBatch) {
        drawTo = uPlane.ShotsAvailable() > bombs.length ? bombs.length : uPlane.ShotsAvailable();
        for (int i = 0; i < drawTo; i++) {
            bombs[i].Draw(spriteBatch);
        }

    }
}
