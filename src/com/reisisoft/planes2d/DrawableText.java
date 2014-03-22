package com.reisisoft.planes2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DrawableText implements IDrawable {
    private BitmapFont font;
    private BitmapFont[] fonts;
    private String text;
    private float x, y;
    private BitmapFont.TextBounds bounds;
    private float oDesiredSide, oX, oY;
    private boolean oIsWidth;
    public static float currentH, currentW;
    private Color color;
    private Anchor oAnchor;

    public void setText(String s) {
        text = s;
        getDesiredFont(text, oDesiredSide, oIsWidth);
        font.setColor(color);
        setTextPosition(oAnchor, oX, oY);
    }

    public void setText(int s) {
        setText(Integer.toString(s));
    }

    public DrawableText(BitmapFont[] fonts, String text, float desiredSide, boolean isWidth, float x, float y, Anchor anchor) {
        this(fonts, text, desiredSide, isWidth, x, y, anchor, Color.BLACK);
    }

    public DrawableText(BitmapFont[] fonts, String text, float desiredSide, boolean isWidth, float x, float y, Anchor anchor, Color color) {
        this.color = color;
        oDesiredSide = desiredSide;
        oIsWidth = isWidth;
        this.fonts = fonts;
        oAnchor = anchor;
        oX = x;
        oY = y;
        setText(text);

    }

    private void setTextPosition(Anchor anchor, float x, float y) {
        switch (anchor) {
            case TopLeft:
            case TopMiddle:
            case TopRight:
                y -= bounds.height;
                break;
            case MiddleLeft:
            case MiddleMiddle:
            case MiddleRight:
                y -= bounds.height / 2f;
                break;
        }
        switch (anchor) {
            case TopRight:
            case MiddleRight:
            case LowRight:
                x -= bounds.width;
                break;
            case TopMiddle:
            case MiddleMiddle:
            case LowMiddle:
                x -= bounds.width / 2f;
                break;
        }
        this.x = x + bounds.width / 2f;
        this.y = y + bounds.height / 2f;
    }

    private void getDesiredFont(String text, float desiredSide, boolean isWidth) {
        for (int i = fonts.length - 1; i >= 0; i--) {
            BitmapFont.TextBounds bounds = fonts[i].getMultiLineBounds(text);
            if (!isWidth && bounds.height < desiredSide
                    && bounds.width <= currentW || isWidth
                    && bounds.width < desiredSide && bounds.height < currentH) {
                font = new BitmapFont(fonts[i].getData(),
                        fonts[i].getRegions(), true);
                this.bounds = bounds;
                return;
            }
        }
    }

    @Override
    public void Draw(SpriteBatch spriteBatch) {
        font.drawMultiLine(spriteBatch, text, x, y, 0,
                BitmapFont.HAlignment.CENTER);
    }

    @Override
    public String toString() {
        return "Draw: " + text + " @" + x + "|" + y;
    }
}
