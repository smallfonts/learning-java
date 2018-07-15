import java.util.Arrays;
import java.util.concurrent.*;

class SimpleRecursiveTask extends RecursiveTask<Integer> {

    Integer[] arrayOfNumbers;

    public SimpleRecursiveTask(Integer[] arrayOfNumbers) {
        this.arrayOfNumbers = arrayOfNumbers;
    }

    @Override
    protected Integer compute(){
        if(arrayOfNumbers.length > 1000){

            System.out.println("Splitting tasks");
            int sizeA = arrayOfNumbers.length /2 ;

            SimpleRecursiveTask simpleRecursiveTask1 = new SimpleRecursiveTask(Arrays.copyOfRange(arrayOfNumbers, 0, sizeA));
            SimpleRecursiveTask simpleRecursiveTask2 = new SimpleRecursiveTask(Arrays.copyOfRange(arrayOfNumbers, sizeA, arrayOfNumbers.length));


            simpleRecursiveTask1.fork();
            simpleRecursiveTask2.fork();

            Integer sum = simpleRecursiveTask1.join();
            Integer sum2 = simpleRecursiveTask2.join();

            return sum + sum2;
        } else {
            return sum(arrayOfNumbers);
        }
    }

    private Integer sum(Integer[] intArray){
        return Arrays.stream(intArray)
                .reduce((a,b)->a+b)
                .get();
    }
}

class SimpleRecursiveAction extends RecursiveAction {

    private int simulatedWork;

    public SimpleRecursiveAction(int simulatedWork) {
        this.simulatedWork = simulatedWork;
    }

    @Override
    protected void compute(){

        if (this.simulatedWork > 100){
            System.out.println("Parallel execution and split task "+simulatedWork);

            int simulatedWork1 = this.simulatedWork/2;
            int simulatedWork2 = this.simulatedWork - simulatedWork1;
            SimpleRecursiveAction simpleRecursiveAction = new SimpleRecursiveAction(simulatedWork1);
            SimpleRecursiveAction simpleRecursiveAction2 = new SimpleRecursiveAction(simulatedWork2);

            //FORK
            simpleRecursiveAction.fork();
            simpleRecursiveAction2.fork();
        } else {
            System.out.println(simulatedWork);
        }

    }
}

public class ForkJoinExample {

    public static void main(String[] args){

        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        SimpleRecursiveAction simpleRecursiveAction = new SimpleRecursiveAction(100);
        pool.invoke(simpleRecursiveAction);
        try {
            pool.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int numberOfIntegers = 10000000;
        int min = 0;
        int max = 100000;

        Integer[] ints = new Integer[numberOfIntegers];

        for(int i = 0; i < numberOfIntegers; i++){
            ints[i] = ThreadLocalRandom.current().nextInt(min, max);
        }

        System.out.println("Finished generating array of size:"+numberOfIntegers);

        SimpleRecursiveTask simpleRecursiveTask = new SimpleRecursiveTask(ints);
        Integer result = pool.invoke(simpleRecursiveTask);

        System.out.println("result "+result);

        try {
            pool.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
