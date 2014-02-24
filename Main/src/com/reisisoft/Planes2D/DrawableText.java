package com.reisisoft.Planes2D;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DrawableText implements IDrawable {
    private BitmapFont font;
    private BitmapFont[] fonts;
    private String text;
    private float x, y;
    private BitmapFont.TextBounds bounds;
    private Anchor anchor;
    private float oX, oY, oDesiredSide;
    private boolean oIsWidth;

    public void setText(String s) {
        text = s;
        getDesiredFont(text, oDesiredSide, oIsWidth);
        this.x = Helper.relativeXposition(anchor, x, bounds.width);
        this.y = Helper.relativeYposition(anchor, y, bounds.height);
    }

    public DrawableText(BitmapFont[] fonts, String text, float desiredSide, boolean isWidth, Anchor anchor, float x, float y) {
        this.anchor = anchor;
        oX = x;
        oY = y;
        oDesiredSide = desiredSide;
        oIsWidth = isWidth;
        this.fonts = fonts;
        setText(text);
    }

    private void getDesiredFont(String text, float desiredSide, boolean isWidth) {
        for (int i = fonts.length; i >= 0; i--) {
            BitmapFont.TextBounds bounds = fonts[i].getMultiLineBounds(text);
            if ((!isWidth && bounds.height < desiredSide) || (isWidth && bounds.width < desiredSide)) {
                font = fonts[i];
                this.bounds = bounds;
                return;
            }
        }
    }

    @Override
    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
// Nothing to Update
    }

    @Override
    public void Draw(SpriteBatch spriteBatch) {
        font.drawMultiLine(spriteBatch, text, x, y, 0, BitmapFont.HAlignment.CENTER);
    }
}
