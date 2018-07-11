
class Processor {

    int id;

    public Processor(int id){
        this.id=id;
    }

    public void produce() throws InterruptedException {

        synchronized(this){
            System.out.println("We are in the produce method");
            wait(10000); //wait for another thread to notify (ie. wake the thread)
            System.out.println("Again producer method...");
        }

    }

    public void consume() throws InterruptedException {

        Thread.sleep(1000);

        synchronized(this){
            System.out.println("Consumer method...");
            notify(); //notifies the waiting thread that it can wake up
        }

    }


}


public class WaitAndNotify {

    public static void main(String[] args){

        Processor processor = new Processor(1);
        Processor processor2 = new Processor(2);

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
