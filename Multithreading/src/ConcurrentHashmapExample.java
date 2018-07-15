import java.util.Objects;
import java.util.concurrent.*;

class ConcurrentHashmapWorker implements Runnable {

    private ConcurrentMap<String, Integer> map;
    private String id;

    public ConcurrentHashmapWorker(ConcurrentMap<String, Integer> map, String id) {
        this.map = map;
        this.id = id;
    }

    @Override
    public void run() {

        Integer value = map.get(id);
        if (Objects.isNull(value)){
            value = Integer.valueOf(0);
            map.put(id, ++value);
        }

        for(Integer i = map.get(id); i < 1000; i = map.get(id)){

            try {
                ++i;
                TimeUnit.MILLISECONDS.sleep(50);
//                System.out.println(String.format("Setting id:%s value:%s", id, i));
                map.put(id, i);
                TimeUnit.MILLISECONDS.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ConcurrentHashmapConsumer implements Runnable {

    private ConcurrentMap<String, Integer> map;

    public ConcurrentHashmapConsumer(ConcurrentMap<String, Integer> map) {
        this.map = map;
    }

    @Override
    public void run(){

        while(true){
            System.out.println("Printing Results...");
            System.out.println("map:"+map);
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}


public class ConcurrentHashmapExample {

    public static void main(String[] args){

        ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>();

        ExecutorService service = Executors.newCachedThreadPool();

        ConcurrentHashmapConsumer consumer = new ConcurrentHashmapConsumer(map);
        service.execute(consumer);


        for(int i = 1; i <=20; i++){
            for(int x = 0; x<=10; x++) {
                ConcurrentHashmapWorker worker = new ConcurrentHashmapWorker(map, Integer.toString(i));
                service.execute(worker);
            }
        }
    }

}
