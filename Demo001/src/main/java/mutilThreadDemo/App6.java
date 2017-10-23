package mutilThreadDemo;

import org.junit.Test;

/**
 * 线程优先级的传递性 优先级高先执行完成
 * Created by damon on 2017/10/22.
 */
public class App6 {

    /**
     * /**
     * 线程优先级的传递性
     * @throws Exception
     */
    @Test
    public void testPriority()throws Exception{
        System.out.println("虚拟机内核数："+Runtime.getRuntime().availableProcessors());
        Runnable r = ()->{
            Thread1 t1 = new Thread1();
            System.out.printf("i am %s,my priority is %s.\r\n",Thread.currentThread().getName(),Thread.currentThread().getPriority());
            t1.start();
        };
        Thread t2 = new Thread(r);
        t2.setName("线程2号");
        t2.start();
        t2 = new Thread(r);
        t2.setPriority(6);
        t2.setName("线程2号");
        t2.start();

    }

    /**
     * 优先级高先执行完成 跑得快和执行完先后 跟优先级高低不是必然关系
     */
    @Test
    public void testHighPriority(){
        System.out.println("虚拟机内核数："+Runtime.getRuntime().availableProcessors());
        try {
            Runnable r = () -> {
                Long begin = System.currentTimeMillis();
                int count = 0;
                for (int i = 1; i <= 1000000000; i++) {
                    count = i;
                }
                Long end = System.currentTimeMillis();
                Long useTime = end - begin;
                System.out.printf("I am %s,count=%s,use time:%s,Priority:%s\r\n", Thread.currentThread().getName(), count, useTime, Thread.currentThread().getPriority());
            };

            for (int i = 1; i <= 100; i++) {
                Thread t = new Thread(r, "子线程" + i + "号");
                if (i > 50)
                    t.setPriority(10);
                else
                    t.setPriority(1);
                t.start();
            }
         //   Thread.sleep(5000);
        }catch (Exception ex){ex.printStackTrace();}
    }


}

class Thread1 extends Thread{
    public void run(){
        this.setName("线程1号");
        System.out.printf("i am %s,my priority is %s.\r\n",this.getName(),this.getPriority());
    }
}
