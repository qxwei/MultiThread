package mutilThreadDemo;

import org.junit.Test;

import java.io.PrintStream;

/**
 * Created by damon on 2017/10/21.
 */
public class App4 {

    /**
     * 独占不同步 线程挂起和复苏
     */
    @Test
    public void testSuspend(){
        try{
            MyThread r = new MyThread();
            r.start();
            Thread.sleep(1000);

            r.suspend();
            System.out.printf("%s:i=%s\r\n", System.currentTimeMillis(), r.getI());
            Thread.sleep(1000);
            System.out.printf("%s:i=%s\r\n", System.currentTimeMillis(), r.getI());

            r.resume();
            Thread.sleep(1000);
            System.out.printf("%s:i=%s\r\n", System.currentTimeMillis(), r.getI());
            System.out.println("");


        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }
}

class MyThread extends Thread{
    int i ;

    public int getI(){
        return i;
    }
    @Override
    public void run(){
        super.run();
        while(true){i++;}
    }
}
