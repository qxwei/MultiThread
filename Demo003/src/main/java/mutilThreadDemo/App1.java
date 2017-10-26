package mutilThreadDemo;

import org.junit.Test;

/**
 * @author Damon
 * @create 2017-10-25 11:31
 **/

public class App1 {

    @Test
    public void TestWaitNotify(){
        try {
            Object lock = new Object();
            Runnable wait = () -> {
                try {
                    String name = Thread.currentThread().getName();
                    synchronized (lock) {
                        System.out.printf("%s:%s,进入等待状态\r\n", System.currentTimeMillis(), name);
                        lock.wait(1000);
                        System.out.printf("%s:%s,等待结束\r\n", System.currentTimeMillis(), name);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };

            Runnable notify = () -> {
                String name = Thread.currentThread().getName();
                try {
                    System.out.printf("%s,通知线程开始运行\r\n", name);
                    synchronized (lock) {
                        lock.notify();
                        System.out.printf("%s:已通知\r\n", name);
                        System.out.printf("%s,运行完成\r\n", name);
                    }
                    Thread.sleep(100);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            };
            System.out.println("演示1===开始（wait释放锁，notify不释放锁）");
            Thread t1 = new Thread(wait, "线程1号");
            Thread t2 = new Thread(notify, "通知线程1号");
            t1.start();
            t2.start();
            Thread.sleep(5000);
            System.out.println("演示1===结束");

            System.out.println("演示2===开始（wait时interrupt）");
            try {
                Thread t3 = new Thread(wait, "线程3号");
                t3.start();
                t3.interrupt();
            }catch (Exception ex){ex.printStackTrace();}
            Thread.sleep(5000);
            System.out.println("演示2===结束");

            System.out.println("演示3===开始（自动唤醒）");
            try {
                Thread t4 = new Thread(wait, "线程4号");
                t4.start();
            }catch (Exception ex){ex.printStackTrace();}
            Thread.sleep(5000);
            System.out.println("演示3===结束");

        }catch (Exception ex){ex.printStackTrace();}
    }
}
