package com.reisisoft.planes2D;

import com.badlogic.gdx.math.Rectangle;

public interface IIntersectable {

    public boolean Intersects(IIntersectable ii);

    public Rectangle[] getBounds();

    public void UpdateRectangle();
}
