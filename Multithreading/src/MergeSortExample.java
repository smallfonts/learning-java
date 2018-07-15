import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

class MergeSorter<T extends Comparable> {

    private T[] sortingCandidate;
    private T[] tempArray;
    private Class<T> clazz;

    @SuppressWarnings("unchecked")
    public MergeSorter(T[] sortingCandidate, Class<T> clazz){
        this.sortingCandidate = sortingCandidate;
        this.clazz = clazz;
        this.tempArray = (T[]) Array.newInstance(clazz, sortingCandidate.length);
    }

    public T[] parallelMergeSort(int numOfThreads){
        int low = 0;
        int high = sortingCandidate.length-1;
        parallelMergeSort(low, high, numOfThreads);
        return sortingCandidate;
    }

    private void parallelMergeSort(int low, int high, int numOfThreads){

        if(numOfThreads <= 1){
            mergeSort(low, high);
            return;
        }

        int mid = (low + high) / 2;

        Thread leftSorter = mergeSortParallel(low, mid, numOfThreads);
        Thread rightSorter = mergeSortParallel(mid+1, high, numOfThreads);

        leftSorter.start();
        rightSorter.start();

        try {
            leftSorter.join();
            rightSorter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        merge(low, mid, high);
    }

    private Thread mergeSortParallel(int low, int high, int numOfThreads){
        return new Thread(()->parallelMergeSort(low, high, numOfThreads / 2));
    }


    public T[] mergeSort(){
        int low = 0;
        int high = this.sortingCandidate.length-1;
        mergeSort(low, high);
        return getSortingCandidate();
    }

    public void mergeSort(int low, int high){

        if(low >= high){
            return;
        }

        int mid = (low + high) / 2;

        mergeSort(low, mid); //left partition
        mergeSort(mid+1, high); // right partition
        merge(low, mid, high);
    }

    public T[] getSortingCandidate(){
        return this.sortingCandidate;
    }

    public void merge(int low, int mid, int high){

        //copy unsorted left-right partition of sub array to temporary array
        for(int i=low; i <= high; i++){
            tempArray[i] = sortingCandidate[i];
        }

        int i = low;
        int j = mid + 1;
        int mergePosition = low;

        while(i <= mid && j <= high){
            if(tempArray[j].compareTo(tempArray[i]) < 0) {
                    sortingCandidate[mergePosition] = tempArray[j];
                    j++;
            } else {
                sortingCandidate[mergePosition] = tempArray[i];
                i++;
            }
            mergePosition++;
        }

        while(i <= mid){
            sortingCandidate[mergePosition] = tempArray[i];
            i++;
            mergePosition++;
        }

        while(j <= high){
            sortingCandidate[mergePosition] = tempArray[j];
            j++;
            mergePosition++;
        }
    }
}

public class MergeSortExample {

    public static void main(String[] args){

        int numberOfIntegers = 10000000;
        int min = 0;
        int max = 100000;

        Integer[] ints = new Integer[numberOfIntegers];

        for(int i = 0; i < numberOfIntegers; i++){
            ints[i] = ThreadLocalRandom.current().nextInt(min, max);
        }

        System.out.println("Finished generating array of size:"+numberOfIntegers);

        long start = System.currentTimeMillis();
        MergeSorter<Integer> sorter = new MergeSorter(ints, Integer.class);
        sorter.mergeSort();
        long end = System.currentTimeMillis();

        long elapsedTimeSequential = end - start;

        start = System.currentTimeMillis();
        MergeSorter<Integer> parallelSorter = new MergeSorter<>(ints, Integer.class);
        parallelSorter.parallelMergeSort(4);
        end = System.currentTimeMillis();

        long elapsedTimeParallel = end - start;

        if(elapsedTimeParallel < elapsedTimeSequential)
            System.out.println("Parallel faster by:"+(elapsedTimeSequential-elapsedTimeParallel));
        else
            System.out.println("Sequential faster by:"+(elapsedTimeParallel-elapsedTimeSequential));




    }



}
