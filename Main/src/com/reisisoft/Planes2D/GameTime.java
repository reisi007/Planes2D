package com.reisisoft.Planes2D;


public class GameTime {
    private long startTime, pauseTime, startPause, elapsedMS;
    private int elapsedSec, elapsedFrames;
    public GameTimeArgs Time;

    public class GameTimeArgs {
        public GameTimeArgs(long ms, int sec, int frames) {
            ElapsedMilliSecond = ms;
            ElapsedSeconds = sec;
            ElapsedMinutes = sec / 60d;
            ElapsedHours = ElapsedMinutes / 60d;
            ElapsedDays = ElapsedHours / 24d;
            ElapsedFrames = frames;
        }

        public long ElapsedMilliSecond;
        public int ElapsedSeconds, ElapsedFrames;
        public double ElapsedMinutes, ElapsedHours, ElapsedDays;
    }

    public void Start() {
        startTime = System.currentTimeMillis();
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
        long endTime = System.currentTimeMillis();
        elapsedMS = endTime - startTime - pauseTime;
        elapsedSec = (int) (elapsedMS / 1000);
        elapsedFrames++;
        Time = new GameTimeArgs(elapsedMS, elapsedSec, elapsedFrames);
    }
}
