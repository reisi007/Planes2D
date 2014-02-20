package com.reisisoft.Planes2D;

public interface IMoveableGameObject extends IGameObject {
    public void setPosition(Anchor a, float x, float y);

    public void updatePosition(float x, float y);
}
