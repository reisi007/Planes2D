package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LifeDrawer implements IDrawable {
    private Drawable[] lives = new Drawable[10];
    private int drawto = 1;

    public LifeDrawer(Drawable life, float x, float y, float currentH) {
        Drawable drawable = new Drawable(life);
        drawable.setScale((currentH / 20f) / drawable.getHeight());
        for (int i = 0; i < lives.length; i++) {
            lives[i] = new Drawable(drawable);
            lives[i].setPosition(Anchor.MiddleLeft, i * (drawable.getWidth() + 4) + x, y);
        }
    }


    public void setLives(int NoL) {
        drawto = Math.abs(NoL);
    }

    @Override
    public void Draw(SpriteBatch spriteBatch) {
        if (drawto >= lives.length)
            drawto = lives.length - 1;
        for (int i = 0; i < drawto; i++)
            lives[i].Draw(spriteBatch);
    }
}
