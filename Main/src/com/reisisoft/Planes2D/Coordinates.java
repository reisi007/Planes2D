package com.reisisoft.Planes2D;

public class Coordinates {
    private int x, y;

    public int X() {
        return x;
    }

    public int Y() {
        return y;
    }

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(int a) {
        this(a, a);
    }

    public Coordinates() {
        this(0);
    }

    public void add(Coordinates v) {
        x += v.x;
        y += v.y;
    }


}
