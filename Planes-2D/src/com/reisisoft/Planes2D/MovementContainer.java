package com.reisisoft.Planes2D;

public class MovementContainer {
    public MovementContainer() {
        this(0, false);
    }

    public MovementContainer(float Movement, boolean shoot) {
        this.Movement = Movement;
        doShoot = shoot;
    }

    public boolean doShoot;
    public float Movement;

    @Override
    public String toString() {
        return "Relative Movewwment:\t" + Movement + "\tShoot:\t" + (doShoot ? "YES" : "NO");
    }
}
