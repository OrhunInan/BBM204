import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

class Main {


    private static int[] reverse(int[] list) {

        int range = list.length / 2;
        int temp;

        for (int i = 0; i < range; i++) {
            temp = list[i];
            list[i] = list[list.length - i - 1];
            list[list.length - i -1] = temp;
        }

        return list;
    }

    private static double testInsertionSort(int[] arr) {
        long average = 0;
        int[] clone;

        for (int i = 0; i < 10; i++) {
            clone = arr.clone();
            long start = System.nanoTime();
            SortAlgorithms.insertionSort(clone);
            average += System.nanoTime() - start;
        }

        return (double) average / 10000000;
    }

    private static double testMergeSort(int[] arr) {
        long average = 0;
        int[] clone;

        for (int i = 0; i < 10; i++) {
            clone = arr.clone();
            long start = System.nanoTime();
            SortAlgorithms.mergeSort(clone);
            average += System.nanoTime() - start;
        }

        return (double) average / 10000000;
    }

    private static double testCountingSort(int[] arr, int max) {
        long average = 0;
        int[] clone;

        for (int i = 0; i < 10; i++) {
            clone = arr.clone();
            long start = System.nanoTime();
            SortAlgorithms.countingSort(clone, max);
            average += System.nanoTime() - start;
        }

        return (double) average / 10000000;
    }

    private static double testLinearSearch(int[] arr) {
        long average = 0;
        int randomNum, randomSearch;

        randomNum = ThreadLocalRandom.current().nextInt(0, arr.length);
        randomSearch = arr[randomNum];
        SearchAlgorithms.BinarySearch(arr, randomSearch);

        for (int i = 0; i < 1000; i++) {
            randomNum = ThreadLocalRandom.current().nextInt(0, arr.length);

            long start = System.nanoTime();
            SearchAlgorithms.LinearSearch(arr, randomSearch);
            average += System.nanoTime() - start;
        }

        return (double) average / 1000000000;
    }

    private static double testBinarySearch(int[] arr) {
        long average = 0;
        int randomNum, randomSearch;

        for (int i = 0; i < 1000; i++) {
            randomNum = ThreadLocalRandom.current().nextInt(0, arr.length);
            randomSearch = arr[randomNum];


            long start = System.nanoTime();
            SearchAlgorithms.BinarySearch(arr, randomSearch);
            average += System.nanoTime() - start;
        }

        return (double) average / 1000000000;
    }

    public static void main(String[] args) throws IOException {

        String[] titles = new String[]{
                "Sort Algorithms With Random Data",
                "Sort Algorithms With Sorted Data",
                "Sort Algorithms With Sorted and Reversed Data",
                "Search Algorithms",
        };

        String[][] lineNames = new String[][] {
                {
                        "Insertion Sort",
                        "merge Sort",
                        "Counting Sort",
                },
                {
                        "Insertion Sort",
                        "merge Sort",
                        "Counting Sort",
                },
                {
                        "Insertion Sort",
                        "merge Sort",
                        "Counting Sort",
                },
                {
                        "Linear Search With Random Data",
                        "Linear Search With Sorted Data",
                        "Binary Search",
                },
        };

        // X axis data
        int[] inputAxis = {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000};
        // Create sample data for linear runtime
        double[][] yAxis = new double[3][10];

        ArrayList<Integer> input = FileReader.getElements(args[0]);
        int[] data = input.stream().mapToInt(i -> i).toArray();


        int[] sample, sortedSample, reversedSortedSample;
        double[][] results = new double[12][10];
        int max;

        for (int i = 0; i < 10; i++) {
            sample = Arrays.copyOfRange(data,0,inputAxis[i]);
            sortedSample = SortAlgorithms.mergeSort(sample);
            reversedSortedSample =  reverse(sortedSample.clone());
            max = reversedSortedSample[0];
            System.out.println(i+1);

            results[0][i] = testInsertionSort(sample);
            results[1][i] = testMergeSort(sample);
            results[2][i] = testCountingSort(sample, max);

            results[3][i] = testInsertionSort(sortedSample);
            results[4][i] = testMergeSort(sortedSample);
            results[5][i] = testCountingSort(sortedSample, max);

            results[6][i] = testInsertionSort(reversedSortedSample);
            results[7][i] = testMergeSort(reversedSortedSample);
            results[8][i] = testCountingSort(reversedSortedSample, max);

            results[9][i] = testLinearSearch(sample);
            results[10][i] = testLinearSearch(sortedSample);
            results[11][i] = testBinarySearch(sortedSample);
        }

        String s = "";
        for (int i = 0; i < 4; i++) {
            yAxis[0] = results[i*3];
            yAxis[1] = results[i*3 +1];
            yAxis[2] = results[i*3 +2] ;

            saveChart(titles[i], inputAxis, yAxis, lineNames[i]);

            s += lineNames[i][0] + " " + Arrays.toString(results[i*3]) + "\n" +
                lineNames[i][1] + " " + Arrays.toString(results[i*3+1]) + "\n" +
                lineNames[i][2] + " " + Arrays.toString(results[i*3+2]) + "\n";
        }
        FileWriter fWriter = new FileWriter("output.txt");
        fWriter.write(s);
        fWriter.close();


    }

    public static void saveChart(String title, int[] xAxis, double[][] yAxis, String[] lineNames) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Milliseconds").xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();


        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        for (int i = 0; i < yAxis.length; i++) {
            chart.addSeries(lineNames[i], doubleX, yAxis[i]);
        }

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, "results/" + title + ".png", BitmapEncoder.BitmapFormat.PNG);
    }
}
