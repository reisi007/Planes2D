package com.reisisoft.planes2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Helper {
    public static Color CornFlowerBlue = new Color(100 / 255f, 149 / 255f, 237 / 255f, 1);

    public static int getFPS() {
        return Gdx.graphics.getFramesPerSecond() < 10 ? 60 : Gdx.graphics.getFramesPerSecond();
    }

    public static void ClearColor(Color color) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
    }

    public static void ClearColor(int r, int g, int b) {
        ClearColor(r, g, b, 255);
    }

    public static void ClearColor(int r, int g, int b, int a) {
        if (r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255
                && a >= 0 && a <= 255) {
            Gdx.gl.glClearColor(r / 255f, g / 255f, b / 255f, a / 255f);
        }
    }

    public static float getRandomInRange(float from, float to) {
        if (from > to) {
            float tmp = from;
            from = to;
            to = tmp;
        }
        return (float) (Math.random() * (to - from) + from);
    }

    public static int getRandomInRange(int from, int to) {
        return (int) (0.5f + getRandomInRange((float) from, to));
    }

    public static float relativeXposition(IDrawable.Anchor a, float x, float w) {
        switch (a) {
            case TopMiddle:
            case MiddleMiddle:
            case LowMiddle:
                x -= w / 2;
                break;
            case TopRight:
            case MiddleRight:
            case LowRight:
                x -= w;
                break;
            default: // *L
                break;
        }
        return x;
    }

    public static float relativeYposition(IDrawable.Anchor a, float y, float h) {
        switch (a) {
            case TopLeft:
            case TopMiddle:
            case TopRight:
                y -= h;
                break;
            case MiddleLeft:
            case MiddleMiddle:
            case MiddleRight:
                y -= h / 2;
                break;
            default: // L*
                break;
        }
        return y;
    }

    public static String hash(int base) {
        return Integer.toString(Integer.reverseBytes(base));
    }

    public static String hash(String base) {
        try {
            return hash(Integer.parseInt(base));
        } catch (NumberFormatException nfE) {
            return hash(0);
        }
    }
}
