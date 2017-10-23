package mutilThreadDemo;

import org.junit.Test;

/**
 * synchronized 代码块同步，一些异步，一些同步
 * @author Damon
 * @create 2017-10-23 12:01
 **/

public class App4 {

    @Test
    public void TestSynchronizedBlock()throws Exception{
        Runnable  r = () -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(50);
                    System.out.printf("name:%s,计算 i=%s\r\n", Thread.currentThread().getName(), i);
                }
                synchronized (this) {
                    for (int i = 1; i <= 10; i++) {
                        Thread.sleep(50);
                        System.out.printf("name:%s,休息%s下\r\n", Thread.currentThread().getName(), i);
                    }
                }
            }catch (Exception ex){ex.printStackTrace();}
        };

        Thread t1= new Thread(r,"线程1号");
        Thread t2= new Thread(r,"线程2号");
        t1.start();
        t2.start();
        Thread.sleep(5000);
    }
}
