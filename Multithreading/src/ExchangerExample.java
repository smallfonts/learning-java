import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * With the help of Exchanger -> two threads can exchange objects
 *
 * exchange() -> exchanging objects is done via one of the two exchange()
 *
 */

class FirstThread implements Runnable {
    private int counter;
    private Exchanger<Integer> exchanger;

    public FirstThread(Exchanger<Integer> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run(){

        while(true){
            counter = counter + 1;
            System.out.println("FirstThread increment the counter " + counter);

            try {
                counter = exchanger.exchange(counter);
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

class SecondThread implements Runnable {
    private int counter;
    private Exchanger<Integer> exchanger;

    public SecondThread(Exchanger<Integer> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run(){

        while(true){

            counter = counter - 1;
            System.out.println("FirstThread decrement the counter " + counter);

            try {
                counter = exchanger.exchange(counter);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

public class ExchangerExample {

    public static void main(String[] args){

        Exchanger<Integer> exchanger = new Exchanger<>();

        Thread a = new Thread(new FirstThread(exchanger));
        Thread b = new Thread(new SecondThread(exchanger));

        a.start();
        b.start();
    }

}
