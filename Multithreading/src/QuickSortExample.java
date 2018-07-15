import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

class ConcurrentQuickSort<T extends Comparable<T>> {

    ExecutorService service;

    public int getPivot(int arraySize){
        return ThreadLocalRandom.current().nextInt(0, arraySize);
    }

    public ConcurrentQuickSort(ExecutorService service) {
        this.service = service;
    }


    //returns sorted segment
    public List<T> quickSort(List<T> segment){
        System.out.println("Quicksorting segment:"+segment);
        if (segment.size() <= 1) return segment;

        int pivotIndex = getPivot(segment.size());
        swap(segment, pivotIndex, segment.size()-1);

        Integer indexGreaterThanPivot = null;
        Integer indexLesserThanPivot = null;
        T pivotValue = segment.get(segment.size()-1);

        Integer newPivotValueIndex = segment.size()-1;
        for(int i = 0 ; i < segment.size()-1; i++){ // Iterates through all values in segment except for the pivot value at the last index

            indexGreaterThanPivot = null;
            indexLesserThanPivot = null;

            if(segment.get(i).compareTo(pivotValue) > 0){ // Find a value greater than the pivot value
                indexGreaterThanPivot = i;

                for(int x = indexGreaterThanPivot+1; x < segment.size()-1; x++){ // Find any values smaller than pivot value after a value greater than pivot value is found
                    if(segment.get(x).compareTo(pivotValue) < 0) {
                        indexLesserThanPivot = x;
                        break;
                    }
                }

                if(Objects.nonNull(indexGreaterThanPivot) && Objects.nonNull(indexLesserThanPivot)) // If found both greater and smaller, then swap those 2 indexes and move on to the next counter
                    swap(segment,indexGreaterThanPivot,indexLesserThanPivot);
                else {//no values are smaller than the pivot, hence swapping pivot with last index greater than pivot
                    swap(segment, indexGreaterThanPivot, newPivotValueIndex);
                    newPivotValueIndex = indexGreaterThanPivot;
                    break; // breaks of loop in this case since there are no values smaller than pivot value and hence all items on right segment are already > than pivot value
                }
            }
        }

        System.out.println("Segment:"+segment);
        System.out.println("pivotValueIndex:"+newPivotValueIndex);
        System.out.println("pivotValue:"+pivotValue);
        List<T> leftSegment = newPivotValueIndex == 0 ? new ArrayList<T>() : new ArrayList<T>(segment.subList(0, newPivotValueIndex));
        List<T> rightSegment = newPivotValueIndex == segment.size()-1 ? new ArrayList<T>() : new ArrayList<T>(segment.subList(newPivotValueIndex+1, segment.size()));

        System.out.println("leftSegment"+leftSegment);
        System.out.println("rightSegment"+rightSegment);

        if(Objects.nonNull(leftSegment))
            leftSegment = quickSort(leftSegment);
        if(Objects.nonNull(rightSegment))
            rightSegment = quickSort(rightSegment);

        List<T> sortedSegment = leftSegment;
        sortedSegment.add(pivotValue);
        sortedSegment.addAll(rightSegment);
        System.out.println("Returning segment:"+sortedSegment);
        return sortedSegment;
    }

    public <T> void swap(List<T> segment, int index, int index2){

        System.out.println("swapping segment:"+segment+" index:"+index+" index2:"+index2);
        T temp = segment.get(index);
        segment.set(index, segment.get(index2));
        segment.set(index2,temp);
    }

}

public class QuickSortExample {

    public static void main(String[] args){
        List<Integer> integerList = Arrays.asList(1,2,3,5,4,1);
        ExecutorService service = Executors.newCachedThreadPool();

        ConcurrentQuickSort<Integer> sorter = new ConcurrentQuickSort(service);

        integerList = sorter.quickSort(integerList);

        System.out.println(integerList);
    }

}
