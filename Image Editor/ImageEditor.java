package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.lang.Integer;

public class ImageEditor {

    public static void main( String[] args) throws Exception {
        //define the delimiter string
        String delim = "((#[^\\n]*\\n)|(\\s+))+";
        //create a file object
        File file = new File(args[0]);
        //create a scanner object using the file
        Scanner s = null;
        try {
            s = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        //use the delimiter
        s.useDelimiter(delim);

        //begin scanning the object; remove the P3
        s.next();
        //create ints for the width and height
        int col = s.nextInt();
        int row = s.nextInt();
        //remove the max color value(255)
        s.next();
        //create an image object
        Image pic = new Image(row, col);
        //fill the image with values for each pixel
        for(int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Pixel p = new Pixel(s.nextInt(), s.nextInt(), s.nextInt());
                pic.set_pixel(i, j, p);
            }
        }
        //the image is now ready to be modified
        //close the scanner
        s.close();
        //print the result

        if(args[2].equals("invert")){
            pic.invert();
        }
        else if(args[2].equals("grayscale")){
            pic.grayscale();
        }
        else if(args[2].equals("emboss")){
            pic.emboss();
        }
        else if(args[2].equals("motionblur")) {
            int motionblurlength = Integer.parseInt(args[3]);
            pic.motionblur(motionblurlength);
        }
        else {
            System.out.println("INVALID TRANSFORMATION TYPE!");
        }

        Print(args[1], pic);
    }

    public static void Print(String output, Image new_pic){
        //this prints the new image created by the modifiers
        PrintWriter pw;
        try {
            pw = new PrintWriter(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        /*this will print out:
        P3
        *width* *height*
        255
        */
        pw.write("P3\n");
        pw.print(new_pic.get_col());
        pw.write(" ");  //space between w and h
        pw.print(new_pic.get_row());
        pw.write("\n255\n");
        //print out the pixel values
        for(int row = 0; row < new_pic.get_row(); row++){
            for(int col = 0; col < new_pic.get_col(); col++){
                pw.write(new_pic.get_image()[row][col].toString());
            }
        }
        pw.close();
    }
}
