import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceExample {

    public static void main(String[] args){
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for(int i=0; i<10; i++){
            executorService.submit(new Worker());
        }

        executorService.shutdown(); //when shutdown, tasks in queue will be executed. However, executor will no longer accept new tasks.

        System.out.println("Finished!");
    }
}


class Worker implements Runnable {

    @Override
    public void run(){
        for(int i=0 ; i<5; i++){
            System.out.println(i);
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}