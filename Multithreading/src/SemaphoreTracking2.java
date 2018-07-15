import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

enum Downloader2 implements Runnable{

    INSTANCE;

    private Semaphore semaphore = new Semaphore(1000);
    private AtomicInteger atomicCounter = new AtomicInteger(0); //Atomic is syncnhronized
    private int nonVolatileCounter = 0;
    private volatile int volatileCounter = 0; //Volatile is not synchronized

    public void run(){
        try{
            semaphore.acquire();
            download();
            semaphore.release();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void download(){
        try {
            TimeUnit.SECONDS.sleep(1);
            nonVolatileCounter++;
            volatileCounter++;
            System.out.println(" AtomicIncrementAndGet:"+atomicCounter.incrementAndGet() +" AtomicGet:"+ atomicCounter.get());

        } catch(Exception e){

        }
    }

    @Override
    public String toString() {
        return "Downloader2{" +
                "atomicCounter=" + atomicCounter +
                ", nonVolatileCounter=" + nonVolatileCounter +
                ", volatileCounter=" + volatileCounter +
                '}';
    }
}

public class SemaphoreTracking2 {

    public static void main(String[] args){
        ExecutorService executorService = Executors.newCachedThreadPool();

        for(int i = 0; i < 1000; i++){
            executorService.execute(Downloader2.INSTANCE);
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch(Exception e){
            e.printStackTrace();
        }
        Downloader2 downloader2 = Downloader2.INSTANCE;
        System.out.println("Finally");

        System.out.println(downloader2);
    }
}
