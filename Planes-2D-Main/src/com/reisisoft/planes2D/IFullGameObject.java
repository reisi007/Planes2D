package com.reisisoft.planes2D;

public interface IFullGameObject extends IGameObject {
    public void setPosition(Anchor a, float x, float y);

    public void setScale(float newScale);

    public float rightMost();

    public String toString();
}
