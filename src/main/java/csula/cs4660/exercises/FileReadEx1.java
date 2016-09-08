package csula.cs4660.exercises;

<<<<<<< HEAD
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
=======
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
>>>>>>> 6c705035dd2e0fc0493533e929b85392ac775ab2

/**
 * Introduction Java exercise to read file
 */
public class FileRead {
    private int[][] numbers;
    /**
     * Read the file and store the content to 2d array of int
     * @param file read file
     */
<<<<<<< HEAD

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
=======
    public FileRead(File file) {
        // TODO: read the file content and store content into numbers
        List<List<Integer>> listOfNumbers = Lists.newArrayList();
        try (Stream<String> stream = Files.lines(file.toPath())) {
            stream.forEach(line -> {
                List<Integer> lineNumbers = Lists.newArrayList();
                for (String token: line.split(" ")) {
                    lineNumbers.add(Integer.parseInt(token));
                }
                System.out.println(line);
                listOfNumbers.add(lineNumbers);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        numbers = converList(listOfNumbers);
>>>>>>> 6c705035dd2e0fc0493533e929b85392ac775ab2
    }

    /**
     * Read the file assuming following by the format of split by space and next
     * line. Display the sum for each line and tell me
     * which line has the highest mean.
     *
     * lineNumber starts with 0 (programming friendly!)
     */
    public int mean(int lineNumber) {
<<<<<<< HEAD
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
=======
        return sum(lineNumber) / numbers[lineNumber].length;
    }

    public int max(int lineNumber) {
        int max = Integer.MIN_VALUE;
        for (int i : numbers[lineNumber]) {
            max = Integer.max(max, i);
>>>>>>> 6c705035dd2e0fc0493533e929b85392ac775ab2
        }
        return max;
    }

    public int min(int lineNumber) {
<<<<<<< HEAD
        int min = 0;
        for (int i = 0; i < numbers[lineNumber].length; i ++)
        {
            if (numbers[lineNumber][i] < min)
            {
                min = numbers[lineNumber][i];
            }
=======
        int min = Integer.MAX_VALUE;
        for (int i : numbers[lineNumber]) {
            min = Integer.min(min, i);
>>>>>>> 6c705035dd2e0fc0493533e929b85392ac775ab2
        }
        return min;
    }

    public int sum(int lineNumber) {
        int sum = 0;
<<<<<<< HEAD
        for (int i = 0; i < numbers[lineNumber].length; i ++)
        {
            sum += numbers[lineNumber][i];
        }
        return sum;
=======
        for (int i : numbers[lineNumber]) {
            sum += i;
        }
        return 0;
>>>>>>> 6c705035dd2e0fc0493533e929b85392ac775ab2
    }

    private int[][] converList(List<List<Integer>> arrayList) {
        int[][] array = new int[arrayList.size()][];
        for (int i = 0; i < arrayList.size(); i++) {
            List<Integer> row = arrayList.get(i);
            array[i] = new int[row.size()];
            for (int j = 0; j < row.size(); j ++) {
                array[i][j] = row.get(j);
            }
        }
        return array;
    }
}
