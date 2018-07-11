import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

enum Downloader {

    INSTANCE;

    private Semaphore semaphore = new Semaphore(1, true);

    public void downloadData(){
        try {
            semaphore.acquire();
            download();
        } catch(InterruptedException e){
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public void download(){
        System.out.println("Downloading data from the web...");
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}

public class SemaphoreTracking {

    public static void main(String[] args){
        ExecutorService executorService = Executors.newCachedThreadPool();
        Downloader downloader = Downloader.INSTANCE;

        for(int i=0; i < 10; i++){
            executorService.execute(downloader::downloadData);
        }
    }

}
