package mutilThreadDemo;

import org.junit.Test;

/**
 * 异常锁释放 (妹妹的抛出异常线程都结束了)
 * @author Damon
 * @create 2017-10-23 11:35
 **/

public class App3 {
    @Test
    public void ExceptionFreedLock()throws Exception{
        Runnable r = new Runnable() {
            @Override
            synchronized public void run() {
                String name = Thread.currentThread().getName();
                try {
                    for (int i = 0; i <= 10; i++) {
                        System.out.printf("Name:%s,i=%s\r\n", name, i);
                        Thread.sleep(100);
                        if(name.equals("子线程1号")&&i==5){

                            System.out.printf("name:%s,异常抛出后线程status:%s,is alive:%s\r\n", Thread.currentThread().getName(),Thread.currentThread().getState(),Thread.currentThread().isAlive());
                            throw new Exception("抛出异常释放锁");
                        }
                    }
                }catch (Exception ex){ex.printStackTrace();}
            }
        };

        Thread t1 = new Thread(r,"子线程1号");
        Thread t2 = new Thread(r,"子线程2号");
        t1.start();
        t2.start();
        Thread.sleep(5000);
    }
}
