package mutilThreadDemo;

import org.junit.Test;

/**
 *  锁重入(可以再次获取自己的内部锁)，父子间传递
 * Created by damon on 2017/10/23.
 */
public class App2 {

    @Test
    public void testTwiceGetLock()throws Exception{


        Runnable r = ()->{
            try {
                Handler handler = new Handler();
                handler.service1();
                handler.service2();
            }catch (Exception ex){ex.printStackTrace();}
        };

        Runnable r1 = ()->{
            try {
                ChildHandler handler = new ChildHandler();
                handler.serviceC2();
            }catch (Exception ex){ex.printStackTrace();}
        };
        System.out.println("锁重入");
        Thread t = new Thread(r);
        t.start();
        Thread.sleep(5000);
        System.out.println("父子类锁重入");
        System.out.println("service1 called end");
        Thread t1 = new Thread(r1);
        t1.start();
        Thread.sleep(5000);
    }

}
class Handler{

    synchronized public void service1()throws Exception{
        System.out.println("service1 called");
        service2();
        Thread.sleep(4000);
        System.out.println("service1 called end");
    }
    synchronized public void service2()throws Exception{
        System.out.println("service2 called");
        Thread.sleep(50);
        System.out.println("service2 called end");
    }
}

class ChildHandler extends Handler{
    synchronized public void serviceC2()throws Exception{
        System.out.println("child service2 called");
        service2();
        Thread.sleep(1000);
        System.out.println("child service2 called end");
    }
}