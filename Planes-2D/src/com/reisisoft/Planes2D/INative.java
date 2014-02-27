package com.reisisoft.Planes2D;

public interface INative {
    public MovementContainer Input();

    public void letQuit();

    public void Setup();

    public boolean continueStagesWorkflow();

    public float speedX();

    public float speedY();

    public Planes2D.Resolutions prefferredResolution(Planes2D game);

    public String WelcomeMessage();

    public String GameOverMessage(int score, boolean newHighScore);

    public void saveScore(int score);

    public int getHighScore();

    public boolean firstTouch();
}
