import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * This is used to synchronize one or more tasks by forcing them to wait for the completion of a set of operations
 *  being performed by other tasks
 *
 * A typical use is to divide ap orblem into n independently solvable tasks and create a CountDownLatch with a value of N
 *   when each task is finished it calls countDown() on the latch. Tasks waiting for the problem to be solved call await()
 *     on the latch to hold themselves back until it is completed
 *
 * A countdown latch is triggered by the number of calls to "Countdown" method (a single thread can call countdown method multiple times and influence the behavior of the latch(
 * A Cyclic barrier is triggered by the number of threads that are in waiting state. Hence you cannot have multiple parties on a Cyclic barrier when there is only one instance of the thread.
 *
 */



public class CountdownLatchExample {

    public static void main(String[] args){

        CountDownLatch countDownLatch = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<String>> futureList = new ArrayList<>();
        for(int i = 0; i < 30; i++){
            Future<String> future = executorService.submit(new CountdownLatchWorker(i, countDownLatch));
            futureList.add(future);
        }

        try {
            countDownLatch.await();
        } catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Getting completed tasks");

    }

}

class CountdownLatchWorker implements Callable<String> {

    private int id;
    private CountDownLatch countDownLatch;
    private Random random;

    public CountdownLatchWorker(int id, CountDownLatch countDownLatch){
        this.id = id;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public String call() {
        System.out.println("Thread with id "+id+" started working...");
        try{
            Thread.sleep(1000);
            countDownLatch.countDown();
            countDownLatch.await(); //waits for other threads to complete before continuing!
            System.out.println("Countdown Reached for id "+id+" returning!");
            return "id "+id;
        } catch(InterruptedException e){
            e.printStackTrace();
            return "";
        }

    }
}
