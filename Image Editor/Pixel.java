package com.example;

import java.util.Scanner;
import java.io.PrintWriter;
import java.lang.Integer;

public class Pixel {
    private int r;
    private int b;
    private int g;

    public Pixel(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public String toString(){
        return r + " " + g + " " + b + " " + "\n";
    }

    public void set_r(int red){
        r = red;
    }

    public void set_g(int green){
        g = green;
    }

    public void set_b(int blue){
        b = blue;
    }

    public int get_r(){
        return r;
    }

    public int get_g(){ return g; }

    public int get_b(){ return b; }
}
