/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package complextask_maxflow;

import java.util.Random;
import java.util.Scanner;
import static java.lang.Math.*;

/**
 *
 * @author dkh
 */
public class Utils {
     public static int readInput(String prompt) //Reads input,returns double.
    {
        Scanner in = new Scanner(System.in);
        System.out.print(prompt);
        int input = in.nextInt();
        return input;
    }
     
     public static void generateRandomArray //Generates random 2-D array.
            (int[][] array, String randomMethod) {
        Random generator;
        generator = new Random();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (randomMethod.equals("random")) {
                    //array[i][j]=generator.nextDouble();
                    if (generator.nextInt(3) == 0) {
                        array[i][j] = 0;
                    } else {
                       array[i][j] = (int) Math.abs((generator.nextGaussian() * 1.5));
                      //  array[i][j] = (int) generator.nextGaussian() * 1.5;
                      
                    }
                }

            }
        }
        System.out.println("generated");
    }
     
     public static void printTime(double time) //Formats time output.
    {
        String timeElapsed = "";
        int days = (int) floor(time) / (24 * 3600);
        int hours = (int) floor(time % (24 * 3600)) / (3600);
        int minutes = (int) floor((time % 3600) / 60);
        int seconds = (int) round(time % 60);

        if (days > 0) {
            timeElapsed = Integer.toString(days) + "d:";
        }
        if (hours > 0) {
            timeElapsed = timeElapsed + Integer.toString(hours) + "h:";
        }
        if (minutes > 0) {
            timeElapsed = timeElapsed + Integer.toString(minutes) + "m:";
        }

        timeElapsed = timeElapsed + Integer.toString(seconds) + "s";
        System.out.print("\nTotal time required: " + timeElapsed + "\n\n");
    }
    
}
