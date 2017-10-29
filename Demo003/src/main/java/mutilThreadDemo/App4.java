package mutilThreadDemo;

import org.junit.Test;

/**
 * @author Damon
 * @create 2017-10-26 16:55
 **/

public class App4 {

    @Test
    public void TestJoin()throws Exception{
        Runnable r = () -> {
            try {
                for (int i = 1; i <= 100000; i++) {
                    System.out.printf("count i=%s\r\n", i);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        System.out.println("===演示一（join的作用）===");
        Thread t1 = new Thread(r, "子线程1号");
        t1.start();
        t1.join();
        System.out.println("===演示一（join的作用）end ===");

        System.out.println("===演示一（join和interrupt异常了）===");
        try {
            Thread t2 = new Thread(r, "子线程2号");
            t1.start();
            t1.join();
            t1.interrupt();
        }catch (InterruptedException ex){ex.printStackTrace();}
        System.out.println("===演示一（join的作用）end ===");

    }
}
