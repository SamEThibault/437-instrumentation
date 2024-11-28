import java.util.Random;

public class Test
{
    static Instrumentation ins = Instrumentation.Instance();

    // generate array of specified size with random ints from 1 to 99999
    private static int[] populateArray(int size) {
        ins.startTiming("populateArray()");
        Random rand = new Random();
        int[] res = new int[size];
        for (int i = 0; i < size; i++) {
            res[i] = rand.nextInt(99999) + 1;
        }
        ins.stopTiming("populateArray()");
        return res;
    }

    // calls the utility class function for bubble sort
    private static void bubbleSort(int[] array, Boolean inst) {
        if (inst) {
            ins.startTiming("bubbleSort()");
            BubbleSort.sortInstrumented(array, array.length);
            ins.stopTiming("bubbleSort()");
        } else {
            ins.startTiming("bubbleSort()");
            BubbleSort.sort(array, array.length);
            ins.stopTiming("bubbleSort()");
        }
    }

    // calls the utility class function for quick sort
    private static void quickSort(int[] array, Boolean inst) {
        if (inst) {
            ins.startTiming("quickSort()");
            QuickSort.sortInstrumented(array, 0, array.length - 1);
            ins.stopTiming("quickSort()");
        } else
        {
            ins.startTiming("quickSort()");
            QuickSort.sort(array, 0, array.length - 1);
            ins.stopTiming("quickSort()");
        }
    }

    // dummy function for initial and overhead tests
    private static void doSomeStuff() {
        ins.startTiming("doSomeStuff()");
        System.out.println("Hello, world!");
        int x;
        for (int i = 0; i < 1000000; i++) {
            x = i;
        }
        ins.stopTiming("doSomeStuff()");
    }

    // dummy function for overhead tests (without nested instrumentation)
    private static void doSomeStuffNoInst() {
        System.out.println("Hello, world!");
        int x;
        for (int i = 0; i < 1000000; i++) {
            x = i;
        }
    }

    public static void main(String[] args)
    {
        // START: INITIAL TESTS
        ins.activate(true);
        ins.startTiming("main");
        ins.startTiming("loop");
        for (int i = 0; i < 5; i++) {
            doSomeStuff();
        }
        ins.stopTiming("loop");
        ins.comment("Hello, this is a comment");
        ins.stopTiming("main");
        // dump logs using both a provided file name and default name
        ins.dump("initialTest.log");
        ins.dump();
        ins.activate(false);
        // END: INITIAL TESTS

        // START: OVERHEAD TESTS
        // To measure the overhead costs of startTiming and stopTiming calls,
        // we'll measure the doSomeStuff() function execution time in a loop with and without instrumentation

        // with instrumentation
        ins.activate(true);
        ins.startTiming("loop-instrumentation");
        for (int i = 0; i < 5; i++) {
            doSomeStuff();
        }
        ins.stopTiming("loop-instrumentation");
        ins.dump("loop-instrumentation.log");
        ins.activate(false);

        // without instrumentation
        ins.activate(true);
        ins.startTiming("loop-no-instrumentation");
        for (int i = 0; i < 5; i++) {
            doSomeStuffNoInst();
        }
        ins.stopTiming("loop-no-instrumentation");
        ins.dump("loop-no-instrumentation.log");
        ins.activate(false);
        // END: OVERHEAD TESTS

        // START: SORTING ALGORITHMS TESTS
        // Here we test the non-instrumented versions of the sorting algorithms
        ins.activate(true);
        int size = 100000;

        int[] bsArray = populateArray(size);
        bubbleSort(bsArray, false);

        int[] qsArray = populateArray(size); // new array to avoid JVM optimizations
        quickSort(qsArray, false);
        ins.dump("sorting-algorithms.log");
        ins.activate(false);

        // Here we test the instrumented versions of the sorting algorithms
        ins.activate(true);

        int[] bsArray2 = populateArray(size);
        bubbleSort(bsArray2, true);

        int[] qsArray2 = populateArray(size); // new array to avoid JVM optimizations
        quickSort(qsArray2, true);
        ins.dump("sorting-algorithms-instrumented.log");
        ins.activate(false);
        // END: SORTING ALGORITHMS TESTS
    }
}
