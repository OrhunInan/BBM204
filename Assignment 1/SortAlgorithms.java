import java.util.Arrays;

public class SortAlgorithms {

    public static int[] insertionSort(int[] list) {
        for (int j = 1; j < list.length; ++j) {
            int key = list[j];
            int i = j - 1;

            while (i >= 0 && list[i] > key) {
                list[i + 1] = list[i];
                i--;
            }

            list[i + 1] = key;
        }

        return list;
    }

    private static int[] merge(int[] a, int[] b) {

        int[] c = new int[a.length + b.length];

        int i = 0, j = 0, k = 0;
        for (; i < a.length && j < b.length; k++) {

            if (a[i] > b[j]) c[k] = b[j++];
            else c[k] = a[i++];
        }
        for (; i < a.length; i++) {
            c[k++] = a[i];
        }
        for (; j < b.length; j++) {
            c[k++] = b[j];
        }

        return c;
    }

    public static int[] mergeSort(int[] list) {

        if (list.length < 2) return list;

        int half = list.length / 2;
        int[] l = Arrays.copyOfRange(list, 0 , half);
        int[] r = Arrays.copyOfRange(list, half, list.length);

        return merge(mergeSort(l), mergeSort(r));
    }

    public static int[] countingSort(int[] list, int max) {

        int[] counts = new int[max + 1];
        Arrays.fill(counts, 0);
        int[] output = new int[list.length];

        for(int d : list) {
            counts[d]++;
        }
        for (int i = 1; i < counts.length; i++) {
            counts[i] += counts[i-1];
        }

        for (int i = list.length -1; i > -1; i--) {
            counts[list[i]]--;
            output[counts[list[i]]] = list[i];
        }
        
        return output;
    }
}
