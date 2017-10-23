package mutilThreadDemo;

import org.junit.Test;

/**
 * synchronized static  class  object
 * @author Damon
 * @create 2017-10-23 15:36
 **/

public class App5 {

    @Test
    public void syncDiff()throws Exception{
        Runnable r = () -> {
            try {
                Handler1 h = new Handler1();
                String name = Thread.currentThread().getName();
                if (name.equals("线程1号")) {
                    h.staticSyncMethod();
                } else if (name.equals("线程2号")) {
                    h.classSyncMethod();
                } else if (name.equals("线程3号")) {
                    h.objectSyncMethod();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        Thread t1 = new Thread(r, "线程1号");
        Thread t2 = new Thread(r, "线程2号");
        Thread t3 = new Thread(r, "线程3号");

        t1.start();
        t2.start();
        t3.start();

        Thread.sleep(6000);
    }

    @Test
    public void deathLock()throws Exception{
        DeathLock death = new DeathLock();

        Thread t1 = new Thread(death,"线程1号");
        Thread t2 = new Thread(death,"线程2号");
        t1.start();
        t2.start();
        Thread.sleep(5000);
    }
}

class Handler1 {
    synchronized public static void staticSyncMethod() throws Exception {
        for (int i = 1; i <= 10; i++) {
            Thread.sleep(50);
            System.out.printf("staticSyncMethod run，thread name:%s,计算i=%s\r\n", Thread.currentThread().getName(), i);
        }
    }
    public void classSyncMethod() throws Exception {
        synchronized (this.getClass()) {
            for (int i = 1; i <= 10; i++) {
                Thread.sleep(50);
                System.out.printf("classSyncMethod run，thread name:%s,计算i=%s\r\n", Thread.currentThread().getName(), i);
            }
        }
    }

    synchronized public void objectSyncMethod() throws Exception {
        for (int i = 1; i <= 10; i++) {
            Thread.sleep(50);
            System.out.printf("objectSyncMethod run，thread name:%s,计算i=%s\r\n", Thread.currentThread().getName(), i);
        }
    }
}

/**
 * 来个死锁
 */
class DeathLock extends Thread{
    Object lock1 =  new Object();
    Object lock2 = new Object();

    public void run(){
        try {
            String name = Thread.currentThread().getName();
            if (name.equals("线程1号")) {
                this.lock1to2();
            } else if (name.equals("线程2号")) {
                this.lock2to1();
            }
        }catch (Exception ex){ex.printStackTrace();}
    }
    public void lock1to2()throws Exception{
        synchronized(lock1){
            System.out.println("get lock1 run start");
            Thread.sleep(1000);
            synchronized (lock2){
                System.out.println("get lock2 run start");
                Thread.sleep(100);
                System.out.println("free lock2 run start");
            }
            System.out.println("free lock1 run start");
        }
    }
    public void lock2to1()throws Exception{
        synchronized(lock2){
            System.out.println("get lock2 run start");
            Thread.sleep(1000);
            synchronized (lock1){
                System.out.println("get lock1 run start");
                Thread.sleep(100);
                System.out.println("free lock1 run start");
            }
            System.out.println("free lock2 run start");
        }
    }
}

