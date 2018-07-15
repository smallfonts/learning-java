import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Philosopher implements Runnable {

    Lock lock1;
    Lock lock2;
    int id;
    int eaten;

    public Philosopher(Lock lock1, Lock lock2, int id) {
        this.lock1 = lock1;
        this.lock2 = lock2;
        this.id = id;
    }

    @Override
    public void run(){

        while(eaten<10) {
            philosophize();
        }

        System.out.println(String.format("Philosopher %s has finished eating", id));

    }

    public void philosophize() {
        boolean lock1Acquired = false;
        boolean lock2Acquired = false;

        try {
            lock1Acquired = lock1.tryLock(10, TimeUnit.MILLISECONDS);
            lock2Acquired = lock2.tryLock(10, TimeUnit.MILLISECONDS);

            if (lock1Acquired && lock2Acquired)
                eat();
            else
                think();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock1Acquired) lock1.unlock();
            if (lock2Acquired) lock2.unlock();
        }
    }

    public void eat() {
        try {
            System.out.println(String.format("Philosopher %s is eating", id));
            TimeUnit.MILLISECONDS.sleep(11);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        eaten++;
    }

    public void think() {
        System.out.println(String.format("Philosopher %s is thinking and has ate %s times", id, eaten));
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class PhilosopherExample {

    public static void main(String[] args) throws InterruptedException {
        Lock lock1 = new ReentrantLock(true);
        Lock lock2 = new ReentrantLock(true);
        Lock lock3 = new ReentrantLock(true);
        Lock lock4 = new ReentrantLock(true);
        Lock lock5 = new ReentrantLock(true);

        ExecutorService executorService = Executors.newCachedThreadPool();


        executorService.execute(new Philosopher(lock1, lock2, 1));
        executorService.execute(new Philosopher(lock2, lock3, 2));
        executorService.execute(new Philosopher(lock3, lock4, 3));
        executorService.execute(new Philosopher(lock4, lock5, 4));
        executorService.execute(new Philosopher(lock5, lock1, 5));


        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);

        System.out.println("All Philosophers have eaten!");


    }

}
