import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReEntrantLockProcessor {

    private Lock lock = new ReentrantLock(true); //Fair locking transfer mechanism
    private Condition condition = lock.newCondition();

    public void producer() throws InterruptedException{
        lock.lock();
        try {
            System.out.println("Producer method...");
            condition.await(); //equivalent to Object.wait() method
            System.out.println("Producer method again...");
        }
        finally{
            lock.unlock();
        }
    }

    public void consumer() throws InterruptedException{
        Thread.sleep(1000);
        lock.lock();
        try {
            System.out.println("Consumer method...");
            condition.signal(); //  equivalent to Object.notify() method
            System.out.println("Consumer method again...");
        } finally {
            lock.unlock();
        }
    }


}


public class ReentrantLocking {

    public static void main(String[] args){
        ReEntrantLockProcessor processor = new ReEntrantLockProcessor();

        Thread t1 = new Thread(()->{
            try {
                processor.producer();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        });


        Thread t2 = new Thread(()->{
            try {
                processor.consumer();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("Finished");

    }

}
