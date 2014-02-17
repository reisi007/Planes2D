package com.reisisoft.Planes2D;

public class MovementContainer {
    public MovementContainer() {
        this(true, 0);
    }

    public MovementContainer(boolean isRelative, double Movement) {
        this.isRelative = isRelative;
        this.Movement = Movement;
    }
    public boolean isRelative;
    public double Movement;
}
