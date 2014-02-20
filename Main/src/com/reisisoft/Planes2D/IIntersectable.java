package com.reisisoft.Planes2D;

import com.badlogic.gdx.math.Rectangle;

public interface IIntersectable {

    public boolean Intersects(Rectangle[] o);

    public Rectangle[] getBounds();

    public void UpdateRectangle();
}
