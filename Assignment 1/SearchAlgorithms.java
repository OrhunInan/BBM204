public class SearchAlgorithms {

    //todo test algorithms

    public static int LinearSearch(int[] list, int duration){
        for (int i = 0; i < list.length; i++) if (list[i] == duration) return i;

        return -1;
    }

    public static int BinarySearch(int[] list, int duration){
        int low = 0, high = list.length - 1, mid;

        while (high-low > 1) {
            mid = (high + low) / 2;

            if (list[mid] < duration) low = mid+1;
            else high = mid;
        }

        if (list[low] == duration) return low;
        if (list[high] == duration) return high;
        return -1;
    }
}
