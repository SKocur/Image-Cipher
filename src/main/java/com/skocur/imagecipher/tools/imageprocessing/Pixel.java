package com.skocur.imagecipher.tools.imageprocessing;

public class Pixel {
    public int y, x, color;
    public boolean visited = false;

    public Pixel(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}
