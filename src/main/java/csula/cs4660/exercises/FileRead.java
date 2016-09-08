package csula.cs4660.exercises;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Introduction Java exercise to read file
 */
public class FileRead {
    private int[][] numbers;
    /**
     * Read the file and store the content to 2d array of int
     * @param file read file
     */

    public FileRead(File file) throws FileNotFoundException, IOException{
        numbers = new int[5][8];
        int index = 0;
        int row = 0;
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        while (line != null)
        {
            String elements[] = line.split(" ");
            for (int j = 0; j < elements.length; j++)
            {
                int i = Integer.parseInt(elements[j]);
                numbers[row][index] = i;
                index++;
            }
            row++;
            index = 0;
            line = br.readLine();
        }
        int greatestLine = 0;
        int greatestLineMean = 0;
        for (int i = 0; i < numbers.length; i++) {
            System.out.println("line " + i + " has sum " + sum(i));
            int mean = mean(i);
            if (mean > greatestLineMean)
            {
                greatestLine = i;
                greatestLineMean = mean;
            }
        }
        System.out.println("line " + greatestLine + " has the greatest mean of " + greatestLineMean);
    }

    /**
     * Read the file assuming following by the format of split by space and next
     * line. Display the sum for each line and tell me
     * which line has the highest mean.
     *
     * lineNumber starts with 0 (programming friendly!)
     */
    public int mean(int lineNumber) {
        int sum = 0;
        for (int i = 0; i < numbers[lineNumber].length; i ++)
        {
            sum += numbers[lineNumber][i];
        }
        return sum / numbers[lineNumber].length;
    }

    public int max(int lineNumber) {
        int max = 0;
        for (int i = 0; i < numbers[lineNumber].length; i ++)
        {
            if (numbers[lineNumber][i] > max)
            {
                max = numbers[lineNumber][i];
            }
        }
        return max;
    }

    public int min(int lineNumber) {
        int min = 0;
        for (int i = 0; i < numbers[lineNumber].length; i ++)
        {
            if (numbers[lineNumber][i] < min)
            {
                min = numbers[lineNumber][i];
            }
        }
        return min;
    }

    public int sum(int lineNumber) {
        int sum = 0;
        for (int i = 0; i < numbers[lineNumber].length; i ++)
        {
            sum += numbers[lineNumber][i];
        }
        return sum;
    }
}
