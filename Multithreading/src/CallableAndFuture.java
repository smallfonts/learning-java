import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class CallableProducer implements Callable<String> {

    private int id;

    public CallableProducer(int id){
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(1000);
        return "id:"+id;
    }

}

public class CallableAndFuture {


    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<Future<String>> futureList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Future<String> future = executorService.submit(new CallableProducer(i));
            futureList.add(future);
        }

//        executorService.shutdown();
//        try {
//            executorService.awaitTermination(1000, TimeUnit.SECONDS);
//        } catch(Exception e){
//            e.printStackTrace();
//        }

        for(Future<String> future : futureList){
            try {
                System.out.println(future.isDone());
                System.out.println(future.get());
            } catch(Exception e){
                e.printStackTrace();
            }
        }


    }

}
