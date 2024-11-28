// credit to GeeksForGeeks - QuickSort utility function
class QuickSort
{
    static Instrumentation ins = Instrumentation.Instance();

    // Partition function
    static int partition(int[] arr, int low, int high)
    {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j <= high - 1; j++)
        {
            if (arr[j] < pivot)
            {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    // Swap function
    static void swap(int[] arr, int i, int j)
    {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // The QuickSort function implementation
    static void sort(int[] arr, int low, int high)
    {
        if (low < high)
        {

            // pi is the partition return index of pivot
            int pi = partition(arr, low, high);

            // Recursion calls for smaller elements
            // and greater or equals elements
            sort(arr, low, pi - 1);
            sort(arr, pi + 1, high);
        }
    }

    static void sortInstrumented(int[] arr, int low, int high)
    {
        ins.startTiming("quickSort::sortInstrumented()");
        if (low < high)
        {

            // pi is the partition return index of pivot
            int pi = partition(arr, low, high);

            // Recursion calls for smaller elements
            // and greater or equals elements
            sort(arr, low, pi - 1);
            sort(arr, pi + 1, high);
        }
        ins.stopTiming("quickSort::sortInstrumented()");
    }
}