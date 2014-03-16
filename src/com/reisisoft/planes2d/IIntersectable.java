package com.reisisoft.planes2d;

import com.badlogic.gdx.math.Rectangle;

public interface IIntersectable {

	public boolean Intersects(IIntersectable ii);

	public Rectangle[] getBounds();

	public void UpdateRectangle();
}
