package com.reisisoft.planes2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class PowerUp extends BaseAnimation implements IIntersectable {
    private Rectangle[] rectangles;

    public PowerUp(Texture texture, Vector2 position, Vector2 direction, float speed, int sizeX, int sizeY, int changeAfter, int NRows, int NColumns, float setSide, boolean setWidth, int missingLastRow) {
        super(texture, position, direction, speed, sizeX, sizeY, changeAfter, NRows, NColumns, setSide, setWidth, missingLastRow);
    }

    @Override
    public boolean Intersects(IIntersectable ii) {
        return Drawable.Intersects(this, ii);
    }

    @Override
    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        super.Update(gameTimeArgs);
        rectangles = animation.getBounds();
    }

    @Override
    public Rectangle[] getBounds() {
        return rectangles;
    }

    @Override
    public void UpdateRectangle() {
        animation.UpdateRectangle();
    }
}
