import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CyclicBarrier is a re-usable countdown latch
 *
 * CyclicBarrier has a barrier action: a runnable, that will run automatically when the count reaches zero
 *
 */

public class CyclicBarrierExample {

    public static void main(String[] args){
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        ExecutorService executorService = Executors.newCachedThreadPool();

        for(int i=0; i < 20; i++){
            executorService.execute(new CyclicBarrierWorker(i, cyclicBarrier));
        }
    }
}

class CyclicBarrierWorker implements Runnable{

    private int id;
    private CyclicBarrier cyclicBarrier;
    private Random random;

    public CyclicBarrierWorker(int id, CyclicBarrier cyclicBarrier) {
        this.id = id;
        this.cyclicBarrier = cyclicBarrier;
        this.random = new Random();
    }

    @Override
    public void run(){
        doWork();
    }

    public void doWork(){
        System.out.println("Thread with ID "+id+" has started");
        try {
            Thread.sleep(random.nextInt(3000));
            cyclicBarrier.await();
        } catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("After await ID "+id);
    }

}
