import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class ConsumerAndProducerProcessor {

    private List<Integer> list = new ArrayList<>();
    private final int LIMIT = 5;
    private final int BOTTOM = 0;
    private int value = 0;

    private final Object lock = new Object();

    public void produce() throws InterruptedException {

        synchronized(lock){

            while(true){

                if(list.size() == LIMIT){
                    System.out.println("Waiting for removing items from the list...");
                    lock.wait();
                } else {
                    System.out.println("Adding: "+value);
                    list.add(value);
                    value++;
                    lock.notify(); //while notify is called, the lock is only handed over to another waiting thread once it completes the execution or goes into waiting state
                }

                TimeUnit.MILLISECONDS.sleep(500);

            }

        }

    }

    public void consume() throws InterruptedException {

        synchronized(lock){

            while(true) {
                if (list.size() == BOTTOM) {
                    System.out.println("Waiting for items to be added to the list");
                    lock.wait();
                } else {
                    System.out.println("Removed: " + list.remove(--value));
                    lock.notify(); //while notify is called, the lock is only handed over to another waiting thread once it completes the execution or goes into waiting state
                }

                TimeUnit.MILLISECONDS.sleep(500);
            }

        }

    }


}


public class ConsumerAndProducer {

    public static void main(String[] args){

        ConsumerAndProducerProcessor processor = new ConsumerAndProducerProcessor();

        Thread t1 = new Thread(() -> {
                try {
                    processor.produce();
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
        });

        Thread t2 = new Thread(()->{
                try {
                    processor.consume();
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
        });

        t1.start();
        t2.start();




    }

}
