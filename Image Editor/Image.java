package com.example;

        import java.util.Scanner;
        import java.io.PrintWriter;
        import java.lang.Integer;

public class Image {
    //AN IMAGE IS A 2-D ARRAY OF PIXELS

    private Pixel[][] image;
    private int rows;
    private int columns;

    public Image(int rows, int columns){
        //setting the width, height, and creating the image
        this.rows = rows;
        this.columns = columns;
        image = new Pixel[rows][columns];
    }

    public int get_col(){ return columns; }

    public int get_row(){ return rows; }

    public Pixel[][] get_image(){
        return image;
    }

    public void set_pixel(int row, int col, Pixel p){
        image[row][col] = p;
    }

    public void invert(){
        //invert the existing pixels to a new value
        for(int row = rows-1; row >= 0; row--){
            for(int col = columns - 1; col >= 0; col--){
                //invert the red component
                image[row][col].set_r((255 - image[row][col].get_r()));
                //green
                image[row][col].set_g((255 - image[row][col].get_g()));
                //blue
                image[row][col].set_b((255 - image[row][col].get_b()));
            }
        }
    }

    public void grayscale(){
        //averages the RGB components for each pixel
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < columns; col++){
                //find the average of the color components
                int average = (image[row][col].get_r() + image[row][col].get_g() + image[row][col].get_b()) / 3;
                //set each component to that average
                image[row][col].set_r(average);
                image[row][col].set_g(average);
                image[row][col].set_b(average);
            }
        }
    }

    public void emboss(){
        int v = 128;
        //for each pixel, calculate the difference in color values with the pixel to the upper-left
        for(int row = rows-1; row >= 0; row--){
            for(int col = columns-1; col >= 0; col--){
                //check if the pixel has no upper-lefft pixel to compare to
                //for the far-left and top row
                if(col == 0 || row == 0){
                    //all the pixels color values are set to 128
                    image[row][col].set_r(128);
                    image[row][col].set_g(128);
                    image[row][col].set_b(128);
                }
                //the rest of the pixels
                else{
                    //if the pixel is on the far left
                    //find the difference between the color values
                    int redDif = image[row][col].get_r() - image[row - 1][col - 1].get_r();
                    int greenDif = image[row][col].get_g() - image[row - 1][col - 1].get_g();
                    int blueDif = image[row][col].get_b() - image[row - 1][col - 1].get_b();
                    //define the int v
                    //find the largest of those 3 differences (can be positive or negative)

                    if((Math.abs(redDif) >= Math.abs(greenDif)) && (Math.abs(redDif) >= Math.abs(blueDif))){
                        v = 128 + redDif;
                    }

                    else if((Math.abs(greenDif) >= Math.abs(redDif)) && (Math.abs(greenDif) >= Math.abs(blueDif))){
                        v = 128 + greenDif;
                    }

                    else if((Math.abs(blueDif) >= Math.abs(redDif)) && (Math.abs(blueDif) >= Math.abs(greenDif))){
                        v = 128 + blueDif;
                    }
                    //check if v needs to be changed
                    if (v < 0) {
                        v = 0;
                    }
                    if (v > 255) {
                        v = 255;
                    }
                    //set the pixels to v
                    image[row][col].set_r(v);
                    image[row][col].set_g(v);
                    image[row][col].set_b(v);
                }
            }
        }
    }

    //motionblur is given an int to determine how blurred the photo will be
    public void motionblur(int n) {
        //for each pixel, average the adjacent color values with the given n value
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                //create values for the new picture and the total
                int total = 0;
                int newR = 0;
                int newG = 0;
                int newB = 0;
                //do this loop until you run out of room for the pixels to the side
                //and the total counter doesn't go out of bounds
                do {
                    newR += image[i][j + total].get_r();
                    newG += image[i][j + total].get_g();
                    newB += image[i][j + total].get_b();
                    total++;
                } while ((j + total) < columns && total < n);
                //set the new color values and average them
                image[i][j].set_r(newR / total);
                image[i][j].set_g(newG / total);
                image[i][j].set_b(newB / total);
            }
        }
    }
}