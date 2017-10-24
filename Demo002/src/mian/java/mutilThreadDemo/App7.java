package mutilThreadDemo;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile 强制从公共对战取值，而不是线程私有栈中取值
 * @author Damon
 * @create 2017-10-24 14:08
 **/

public class App7 {

    @Test
    public void TestVolatile() {
        try {
            PrintService worker = new PrintService();
            worker.start();
            Thread.sleep(100);
            System.out.println("stop print service");
            worker.isContinue = false;
            Thread.sleep(5000);
            System.out.println("count service");
            CountService count = new CountService();
            new Thread(count).start();
            new Thread(count).start();
            Thread.sleep(5000);
            System.out.println("atomic count service");
            AtomicCountService atomicCount = new AtomicCountService();
            new Thread(atomicCount).start();
            new Thread(atomicCount).start();
            Thread.sleep(5000);
        }catch (Exception ex){ex.printStackTrace();}
    }


}

class CountService extends Thread {

     int count = 0;
    public void Count(){
        try {
            for (int i = 1; i <= 10; i++) {
                count++;
                Thread.sleep(100);
                System.out.println(count);
            }
        }catch (Exception ex){ex.printStackTrace();}
    }

    @Override
    public void run() {
        super.run();
        this.Count();
    }
}
class AtomicCountService extends Thread {
    AtomicInteger ai = new AtomicInteger(0);
    public void Count(){
        try {
            for (int i = 1; i <= 10; i++) {
                Thread.sleep(100);
                System.out.println(ai.incrementAndGet());
            }
        }catch (Exception ex){ex.printStackTrace();}
    }
    @Override
    public void run() {
        super.run();
        this.Count();
    }
}
class PrintService extends Thread {
    volatile boolean isContinue = true;
    public void Print(){
        while(isContinue){
            System.out.println("print service working");
        }
    }
    @Override
    public void run() {
        super.run();
        this.Print();
    }
}