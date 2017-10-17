package mutilThreadDemo;

import org.junit.Test;

/**
 * Created by damon on 2017/10/17.
 */
public class App2 {
    public static void main( String[] args ) throws Exception
    {
        System.out.println("虚拟机内核数："+Runtime.getRuntime().availableProcessors());

        Runnable r = new Runnable() {
            int count = 100;
            @Override
            public void run() {
                threadRun(count);
            }
        };

        Thread ct1 = new Thread(r,"子线程1号");
        Thread ct2 = new Thread(r,"子线程2号");
        Thread ct3 = new Thread(r,"子线程3号");
        Thread ct4 = new Thread(r,"子线程4号");
        Thread ct5 = new Thread(r,"子线程5号");
        Thread ct6 = new Thread(r,"子线程6号");
        Thread ct7 = new Thread(r,"子线程7号");
        Thread ct8 = new Thread(r,"子线程8号");
        Thread ct9 = new Thread(r,"子线程9号");

        ct1.start();
        ct2.start();
        ct3.start();
        ct4.start();
        ct5.start();
        ct6.start();
        ct7.start();
        ct8.start();
        ct9.start();
    }
    private static void threadRun( int count){
        count--;
        System.out.println(Thread.currentThread().getName()+"--计算count,计算后值为："+count);
    }

    /**
     * 同步处理
     */
    @Test
    public void syncVar(){
        System.out.println("虚拟机内核数："+Runtime.getRuntime().availableProcessors());

        Runnable r = new Thread() {
            private int count = 100;
            @Override
            synchronized public void run() {
                threadRun(count);
            }
        };

        Thread ct1 = new Thread(r,"子线程1号");
        Thread ct2 = new Thread(r,"子线程2号");
        Thread ct3 = new Thread(r,"子线程3号");
        Thread ct4 = new Thread(r,"子线程4号");
        Thread ct5 = new Thread(r,"子线程5号");
        Thread ct6 = new Thread(r,"子线程6号");
        Thread ct7 = new Thread(r,"子线程7号");
        Thread ct8 = new Thread(r,"子线程8号");
        Thread ct9 = new Thread(r,"子线程9号");

        ct1.start();
        ct2.start();
        ct3.start();
        ct4.start();
        ct5.start();
        ct6.start();
        ct7.start();
        ct8.start();
        ct9.start();
    }
}
