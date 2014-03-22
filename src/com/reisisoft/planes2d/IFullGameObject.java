package com.reisisoft.planes2d;

public interface IFullGameObject extends IGameObject {
	public void setPosition(Anchor a, float x, float y);

	public void setScale(float newScale);

	public float rightMost();

	@Override
	public String toString();
}
