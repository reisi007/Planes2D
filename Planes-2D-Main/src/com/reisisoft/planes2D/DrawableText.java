package com.reisisoft.planes2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DrawableText implements IDrawable {
	public static float currentH, currentW;
	public static BitmapFont[] fonts;
	private Color color;
	private BitmapFont font = new BitmapFont(fonts[fonts.length - 1].getData(), fonts[fonts.length - 1].getRegions(), true);
	private float maxW, totalH;
	private Anchor oAnchor;
	private float oDesiredSide, oX, oY;
	private boolean oIsWidth;
	private String text;
	private float x, y;

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

	@Override
	public void Draw(SpriteBatch spriteBatch) {
		float textWidthHalf = 0;
		/*for (int i = text.length - 1; i >= 0; i--) {
			String str = text[i];
			textWidthHalf = getTextWidth(str, font) / 2f;
			font.draw(spriteBatch, str, x - textWidthHalf, y + getTextHeight(font) * (text.length - 1 - i));
		}*/
		font.drawMultiLine(spriteBatch, text, x, y,0,BitmapFont.HAlignment.CENTER);
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
				if (tmpW > maxW)
					maxW = tmpW;
			}
			/* System.out.println("CurrentW\t" + currentW + "\tMaxW\t" + maxW + "\nCurrentH\t" + currentH + "\tTotalH\t" + totalH + "\nFont index: [" + (i + 1) + "|" + fonts.length + "]" +
                    "\nBool is:\t" + (!((isWidth && maxW <= desiredSide && totalH < currentH) || (!isWidth && totalH <= desiredSide && maxW <= currentW)) && i >= 0));*/
			i--;
		}while (!(((isWidth && maxW <= desiredSide && totalH < currentH) || (!isWidth && totalH <= desiredSide && maxW <= currentW))) && i >= 0);
	}

	private float getTextHeight(BitmapFont font) {
		return font.getLineHeight();
	}

	private float getTextWidth(String s, BitmapFont font) {
		return font.getBounds(s).width;
	}

	public void setText(String s) {
		text = s;
		getDesiredFont(text.split("\\r?\\n"), oDesiredSide, oIsWidth);
		font.setColor(color);
		setTextPosition(oAnchor, oX, oY);
	}

	private void setTextPosition(Anchor anchor, float x, float y) {
		switch (anchor) {
		case TopLeft:
		case TopMiddle:
		case TopRight:
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
		this.y = y+ totalH;
	}

	@Override
	public String toString() {
		return "Draw: " + text + " @" + x + "|" + y;
	}
}
