/* A priorityQueue is an unbounded queue which sorts comparable objects from smallest to largest
 * Objects in priority queue needs to implement the Comparable interface
 * Cannot put null objects into a priority queue
 *
 */


import java.util.PriorityQueue;
import java.util.concurrent.*;

class PriorityQueueItem implements Comparable<PriorityQueueItem> {

    int priority;
    String message;

    public PriorityQueueItem(int priority, String message) {
        this.priority = priority;
        this.message = message;
    }

    @Override
    public int compareTo(PriorityQueueItem item){
        if (this.priority > item.priority)
            return -1;
        if (this.priority < item.priority)
            return 1;

        return 0;
    }

    public String getMessage() {
        return message;
    }
}

class PriorityQueueConsumer implements Runnable {

    BlockingQueue<PriorityQueueItem> blockingQueue;

    public PriorityQueueConsumer(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run(){
        while(true){
            try {
                PriorityQueueItem item = blockingQueue.take();
                System.out.println(item.getMessage());
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


public class PriorityQueueExample {

    public static void main(String[] args){

        BlockingQueue<PriorityQueueItem> queue = new PriorityBlockingQueue<>();

        queue.add(new PriorityQueueItem(10,"10"));
        queue.add(new PriorityQueueItem(3, "3"));
        queue.add(new PriorityQueueItem(1,"1"));
        queue.add(new PriorityQueueItem(9999,"9999"));
        queue.add(new PriorityQueueItem(2, "2"));
        queue.add(new PriorityQueueItem(5, "5"));
        queue.add(new PriorityQueueItem(-1,"-1"));
        queue.add(new PriorityQueueItem(1000,"1000"));


        ExecutorService executorService = Executors.newCachedThreadPool();

        PriorityQueueConsumer consumer = new PriorityQueueConsumer(queue);
//        PriorityQueueConsumer consumer2 = new PriorityQueueConsumer(queue);
//        PriorityQueueConsumer consumer3 = new PriorityQueueConsumer(queue);
//        PriorityQueueConsumer consumer4 = new PriorityQueueConsumer(queue);


        executorService.execute(consumer);
//        executorService.execute(consumer2);
//        executorService.execute(consumer3);
//        executorService.execute(consumer4);

    }

}
