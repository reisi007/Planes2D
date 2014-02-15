package com.reisisoft.Planes2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by Florian on 14.02.14.
 */
public class Helper {
    public static Color CornFlowerBlue = new Color(100 / 255f, 149 / 255f, 237 / 255f, 1);

    public static void ClearColor(Color color) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
    }

    public static void ClearColor(int r, int g, int b) {
        ClearColor(r, g, b, 255);
    }

    public static void ClearColor(int r, int g, int b, int a) {
        if (r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255 && a >= 0 && a <= 255) {
            Gdx.gl.glClearColor(r / 255f, g / 255f, b / 255f, a / 255f);
        }
    }
}
