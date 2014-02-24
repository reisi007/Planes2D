package com.reisisoft.Planes2D;

public interface IGameObject extends IDrawable {
    public void setPosition(Anchor a, float x, float y);

    public void setScale(float newScale);

    public float rightMost();

    public String toString();
}
