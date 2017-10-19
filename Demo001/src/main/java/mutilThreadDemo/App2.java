package mutilThreadDemo;

import org.junit.Test;

/**
 * Created by damon on 2017/10/17.
 * 共享变量的线程不安全
 * Integer 属于不可变对象，你每次修改 count 的值，其实 count 的引用都已经被改变了。你可以自己模拟一个内部值的可变的 Integer 对象：
 */
public class App2 {
    public static void main( String[] args ) throws Exception
    {
        System.out.println("虚拟机内核数："+Runtime.getRuntime().availableProcessors());

        Runnable r = new Runnable() {
            Integer count = 500;
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
    /*
    * synchronized 无效问题？
    * */
    synchronized private static void threadRun( Integer count){
        try {
            count++;
            System.out.println(Thread.currentThread().getName() + "--计算count,计算后值为：" + count);
        }catch (Exception e){}
    }

    /**
     * 同步处理
     */
    @Test
    public void syncVar()throws Exception{
        System.out.println("虚拟机内核数："+Runtime.getRuntime().availableProcessors());

        Runnable r = new Thread() {
            private int count = 100;
            @Override
            synchronized public void run() {
                try {
                    count--;
                    System.out.println(Thread.currentThread().getName() + "--计算count,计算后值为：" + count);
                }catch (Exception e){}
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
        Thread.sleep(5000);
    }

    @Test
    public  void notAlterVar() throws Exception {
        System.out.println("虚拟机内核数："
                + Runtime.getRuntime().availableProcessors());

        Runnable r = new Runnable() {
            MutableInt count = new MutableInt(100);

            @Override
            public void run() {
                threadRun(count);
            }
        };

        Thread ct1 = new Thread(r, "子线程1号");
        Thread ct2 = new Thread(r, "子线程2号");
        Thread ct3 = new Thread(r, "子线程3号");
        Thread ct4 = new Thread(r, "子线程4号");
        Thread ct5 = new Thread(r, "子线程5号");
        Thread ct6 = new Thread(r, "子线程6号");
        Thread ct7 = new Thread(r, "子线程7号");
        Thread ct8 = new Thread(r, "子线程8号");
        Thread ct9 = new Thread(r, "子线程9号");

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

    /*
     * synchronized 无效问题？
     */
    synchronized private static void threadRun(MutableInt count) {
        try {
            count.inc();
            System.out.println(Thread.currentThread().getName()
                    + "--计算count,计算后值为：" + count);
        } catch (Exception e) {
        }
    }
}

 class MutableInt {
    private int value;

    public MutableInt(int initial) {
        value = initial;
    }

    public int get() {
        return value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public void inc() {
        value++;
    }
}
