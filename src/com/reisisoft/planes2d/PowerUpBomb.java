package com.reisisoft.planes2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class PowerUpBomb extends PowerUp {
    public PowerUpBomb(Texture texture, Vector2 position, Vector2 direction, float speed, int sizeX, int sizeY, int changeAfter, int NRows, int NColumns, float setSide, boolean setWidth, int missingLastRow) {
        super(texture, position, direction, speed, sizeX, sizeY, changeAfter, NRows, NColumns, setSide, setWidth, missingLastRow);
    }
}
