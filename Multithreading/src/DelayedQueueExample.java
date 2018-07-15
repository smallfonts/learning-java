import java.util.concurrent.*;

/**
 * This is an unbounded BlockingQueue of objects that implements the Delayed interface
 * - DelayedQueue keeps the elements internally until a certain delay has expired
 * - an object can only be taken from teh queue when its delay has expired
 *
 * we cannot place null items in the queue - the queue is sorted so that the objects at the head has a delay that has expired for the longest time
 *
 * If no delay has expired, then there is no head element and poll() will return null
 *
 * size() returns the count of both expired and unexpired items in the queue
 *
 * Needs to override
 *
 * @Override
 * public long getDelay(TimeUnit timeUnit){
 * return timeUnit.convert(duration-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
 * }
 *
 *
 */

class DelayedItem implements Delayed {

    private long duration;
    private String message;

    public DelayedItem(long duration, String message){
        this.duration = System.currentTimeMillis() + duration;
        this.message = message;
    }

    @Override
    public int compareTo(Delayed otherDelayed){

        if(this.duration < ((DelayedItem) otherDelayed).duration)
            return -1;

        if(this.duration > ((DelayedItem) otherDelayed).duration)
            return +1;

        return 0;
    }

    public long getDuration(){
        return this.duration;
    }

    @Override
    public long getDelay(TimeUnit timeUnit){
        return timeUnit.convert(duration-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public String getMessage(){
        return this.message;
    }

}

public class DelayedQueueExample {

    public static void main(String[] args){
        BlockingQueue<DelayedItem> queue = new DelayQueue<>();

        try {
            queue.put(new DelayedItem(5000, "5 secs delay"));
            queue.put(new DelayedItem(1000, "1 sec delay"));
            queue.put(new DelayedItem(2000, "2 secs delay"));
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        while(!queue.isEmpty()){
            try {
                DelayedItem item = queue.take();
                System.out.println(item.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
