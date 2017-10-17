package mutilThreadDemo;

/**
 * Created by damon on 2017/10/16.
 * **app1:** 多线程执行的随机性
 */
public class App1 {
    public static void main( String[] args ) throws Exception
    {
        System.out.println("虚拟机内核数："+Runtime.getRuntime().availableProcessors());
        Runnable r = ()->{
            Thread self = Thread.currentThread();
            self.setName("子线程1号");
            threadRun();
        };
        Thread childThread = new Thread(r);
        childThread.start();

        Thread self = Thread.currentThread();
        self.setName("主线程main");
        threadRun();

    }

    private static void threadRun(){
        try {
            for (int i = 0; i < 10; i++) {
                int time = (int) (Math.random() * 1000);
                Thread.sleep(time);
                System.out.println(Thread.currentThread().getName()+"--Running !");
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
