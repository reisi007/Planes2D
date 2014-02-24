package com.reisisoft.Planes2D;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BombDrawer implements IDrawable {
    private Drawable[] bombs = new Drawable[50];
    private UserPlane uPlane;
    private int drawTo;

    public BombDrawer(UserPlane userPlane, Drawable bomb, float x, float y, float currentW) {
        uPlane = userPlane;
        Drawable drawable = new Drawable(bomb);
        drawable.setScale(((currentW - bombs.length * 4 - x) / 20f) / drawable.getWidth());
        for (int i = 0; i < bombs.length; i++) {
            bombs[i] = new Drawable(drawable);
            bombs[i].setPosition(Anchor.MiddleLeft, (i % (bombs.length / 2)) * (drawable.getWidth() + 4) + x, (i < bombs.length / 2 ? y : y - drawable.getHeight() - 4));
        }
    }

    public void setUserPlane(UserPlane up) {
        uPlane = up;
    }

    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        drawTo = uPlane.ShotsAvailable() > 50 ? 50 : uPlane.ShotsAvailable();
    }

    public void Draw(SpriteBatch spriteBatch) {
        for (int i = 0; i < drawTo && i < bombs.length; i++) {
            bombs[i].Draw(spriteBatch);
        }

    }
}
