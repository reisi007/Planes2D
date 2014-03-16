package com.reisisoft.planes2D;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DrawableText implements IDrawable {
    private BitmapFont font = new BitmapFont(fonts[fonts.length - 1].getData(), fonts[fonts.length - 1].getRegions(), true);
    public static BitmapFont[] fonts;
    private String[] text;
    private float x, y;
    private float oDesiredSide, oX, oY;
    private boolean oIsWidth;
    public static float currentH, currentW;
    private Color color;
    private Anchor oAnchor;
    private float maxW, totalH;

    private void setText(String s) {
        setText(s.split("\\r?\\n"));
    }

    public void updateText(String s) {
        text = s.split("\\r?\\n");
    }

    private void setText(String[] s) {
        text = s;
        getDesiredFont(text, oDesiredSide, oIsWidth);
        font.setColor(color);
        setTextPosition(oAnchor, oX, oY);
    }

    public DrawableText(String text, float desiredSide, boolean isWidth, float x, float y, Anchor anchor) {
        this(text, desiredSide, isWidth, x, y, anchor, Color.BLACK);
    }

    public DrawableText(String text, float desiredSide, boolean isWidth, float x, float y, Anchor anchor, Color color) {
        this.color = color;
        oDesiredSide = desiredSide;
        oIsWidth = isWidth;
        oAnchor = anchor;
        oX = x;
        oY = y;
        setText(text);
    }

    private void setTextPosition(Anchor anchor, float x, float y) {
        switch (anchor) {
            case LowLeft:
            case LowMiddle:
            case LowRight:
                y -= totalH;
                break;
            case MiddleLeft:
            case MiddleMiddle:
            case MiddleRight:
                y -= totalH / 2f;
                break;
        }
        switch (anchor) {
            case TopRight:
            case MiddleRight:
            case LowRight:
                x -= maxW / 2;
                break;
            case TopLeft:
            case MiddleLeft:
            case LowLeft:
                x += maxW / 2f;
                break;
        }
        this.x = x;
        this.y = y;
    }

    private void getDesiredFont(String[] text, float desiredSide, boolean isWidth) {
        maxW = Float.MIN_VALUE;
        float tmpW = 0;
        int i = fonts.length - 1;
        do {
            maxW = Float.MIN_VALUE;
            font = new BitmapFont(fonts[i].getData(), fonts[i].getRegions(), true);
            totalH = text.length * getTextHeight(font);
            for (String s : text) {
                tmpW = getTextWidth(s, font);
                if (tmpW > maxW) {
                    maxW = tmpW;
                }
            }
           /* System.out.println("CurrentW\t" + currentW + "\tMaxW\t" + maxW + "\nCurrentH\t" + currentH + "\tTotalH\t" + totalH + "\nFont index: [" + (i + 1) + "|" + fonts.length + "]" +
                    "\nBool is:\t" + (!((isWidth && maxW <= desiredSide && totalH < currentH) || (!isWidth && totalH <= desiredSide && maxW <= currentW)) && i >= 0));*/
            i--;
        }while (!(((isWidth && maxW <= desiredSide && totalH < currentH) || (!isWidth && totalH <= desiredSide && maxW <= currentW))) && i >= 0);
    }

    private float getTextWidth(String s, BitmapFont font) {
        return font.getBounds(s).width;
    }

    private float getTextHeight(BitmapFont font) {
        return font.getLineHeight();
    }

    @Override
    public void Draw(SpriteBatch spriteBatch) {
        float textWidthHalf = 0;
        for (int i = text.length - 1; i >= 0; i--) {
            String str = text[i];
            textWidthHalf = getTextWidth(str, font) / 2f;
            font.draw(spriteBatch, str, x - textWidthHalf, y + getTextHeight(font) * (text.length - 1 - i));
        }
    }

    public String toString() {
        return "Draw: " + text + " @" + x + "|" + y;
    }
}
