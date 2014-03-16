package com.reisisoft.planes2D;


public class GameTime {
    public static final double FRAME = (1000 / 60d);
    private long startTime, curTime, lastTime, pauseTime, startPause, elapsedMS;
    private int elapsedSec, elapsedFrames;
    public GameTimeArgs Time;

    public class GameTimeArgs {
        public GameTimeArgs(long ms, int sec, int frames, long last) {
            ElapsedMilliSecond = ms;
            ElapsedSeconds = sec;
            ElapsedMinutes = sec / 60d;
            ElapsedHours = ElapsedMinutes / 60d;
            ElapsedDays = ElapsedHours / 24d;
            ElapsedFrames = frames;
            ELapsedMSSinceLastFrame = last;
        }

        public long ElapsedMilliSecond, ELapsedMSSinceLastFrame;
        public int ElapsedSeconds, ElapsedFrames, FPS = Helper.getFPS();
        public double ElapsedMinutes, ElapsedHours, ElapsedDays;
    }

    public void Start() {
        startTime = System.currentTimeMillis();
        lastTime = startTime - (long) (FRAME + 0.5d);
    }

    public void Restart() {
        pauseTime = startPause = pauseTime = elapsedFrames = 0;
        Start();
    }

    public void Pause() {
        startPause = System.currentTimeMillis();
    }

    public void Resume() {
        pauseTime += (System.currentTimeMillis() - startPause);
    }

    public void Update() {
        curTime = System.currentTimeMillis();
        elapsedMS = curTime - startTime - pauseTime;
        elapsedSec = (int) (elapsedMS / 1000);
        elapsedFrames++;
        Time = new GameTimeArgs(elapsedMS, elapsedSec, elapsedFrames, (curTime - lastTime));
        lastTime = curTime;
    }
}
