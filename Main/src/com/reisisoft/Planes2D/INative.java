package com.reisisoft.Planes2D;

public interface INative {
    public MovementContainer Input();

    public void letQuit();

    public void Setup();

    public boolean ContinueStagesWorkflow();

    public float speedX();

    public float speedY();

    public Planes2D.Resolutions prefferredResolution(Planes2D game);
}
