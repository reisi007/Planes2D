package com.reisisoft.Planes2D;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DrawableText implements IDrawable {
    private BitmapFont font;
    private BitmapFont[] fonts;
    private String text;
    private float x, y;
    private BitmapFont.TextBounds bounds;
    private float oDesiredSide;
    private boolean oIsWidth;
    public static float currentH, currentW;

    public void setText(String s) {
        text = s;
        getDesiredFont(text, oDesiredSide, oIsWidth);
    }

    public void setText(int s) {
        setText(Integer.toString(s));
    }

    public DrawableText(BitmapFont[] fonts, String text, float desiredSide, boolean isWidth, float x, float y, Anchor anchor) {
        oDesiredSide = desiredSide;
        oIsWidth = isWidth;
        this.fonts = fonts;
        setText(text);
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
        this.y = y;
    }

    private void getDesiredFont(String text, float desiredSide, boolean isWidth) {
        for (int i = fonts.length - 1; i >= 0; i--) {
            BitmapFont.TextBounds bounds = fonts[i].getMultiLineBounds(text);
            if ((!isWidth && bounds.height < desiredSide && bounds.width <= currentW) || (isWidth && bounds.width < desiredSide && bounds.height < currentH)) {
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

    public String toString() {
        return "Draw: " + text + " @" + x + "|" + y;
    }
}
