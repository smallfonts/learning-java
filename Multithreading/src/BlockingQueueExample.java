import java.util.concurrent.*;

/**
 * A blocking queue is an interface which represent a queue that is Thread Safe
 *
 *
 *
 */

class BlockingQueueProducer implements Runnable {

    private BlockingQueue<Integer> blockingQueue;

    public BlockingQueueProducer(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {

        int counter = 0;

        while(true){
            try {
                blockingQueue.put(counter);
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
        }

    }
}

class BlockingQueueConsumer implements Runnable {

    private BlockingQueue<Integer> blockingQueue;
    private int threadId;

    public BlockingQueueConsumer(int threadId, BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
        this.threadId = threadId;
    }

    @Override
    public void run() {

        int counter = 0;

        while(true){
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                int number = blockingQueue.take();
                System.out.println(String.format("ID:%s Took value:%s", threadId, number));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
        }

    }
}

public class BlockingQueueExample {

    public static void main(String[] args){
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);

        BlockingQueueProducer producer = new BlockingQueueProducer(blockingQueue);

        BlockingQueueConsumer consumer = new BlockingQueueConsumer(1, blockingQueue);
        BlockingQueueConsumer consumer2 = new BlockingQueueConsumer(2, blockingQueue);


        ExecutorService executor = Executors.newCachedThreadPool();

        executor.execute(producer);
        executor.execute(consumer);
        executor.execute(consumer2);


    }

}
