package mutilThreadDemo;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by damon on 2017/10/17.
 */
public class App3 {

    /**
     * 中断
     */
    @Test
    public void testInterrupte()throws Exception{
        Thread r = new Thread(){
            public void run(){
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (int i = 0; i < 100; i++) {
                        System.out.printf("%s：%s--Running！times:%s\r\n", df.format(new Date()), this.getName(), i);
                    }
                }catch (Exception e){}
            }
        };
        r.start();
        r.interrupt();
        System.out.println("interrupt execute");
        System.out.printf("%s：is Interrupted %s\r\n",r.getName(), r.isInterrupted());
        System.out.printf("%s：is Interrupted %s\r\n",r.getName(), r.isInterrupted());
        Thread.currentThread().interrupt();
        System.out.printf("%s：is Interrupted %s\r\n",Thread.currentThread().getName(), Thread.interrupted());
        System.out.printf("%s：is Interrupted %s\r\n",Thread.currentThread().getName(), Thread.interrupted());
        Thread.sleep(6000);
    }

    /**
     * 中断
     */
    @Test
    public void testStopThread()throws Exception{

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Thread r = new Thread(){
            public void run(){
                try {
                    for (int i = 0; i < 100; i++) {
                        System.out.printf("%s：%s--Running！times:%s\r\n", df.format(new Date()), this.getName(), i);
                        if(this.interrupted()) {
                            System.out.println("I am interrupted,I will exit\r\n");
                            break;
                        }
                    }
                }catch (Exception e){}
            }
        };
        r.start();
        Thread.sleep(2);
        r.interrupt();
        Thread.sleep(2000);


        Thread r1 = new Thread(){
            public void run(){
                try {

                    for (int i = 0; i < 100; i++) {
                        System.out.printf("%s：%s--Running！times:%s\r\n", df.format(new Date()), this.getName(), i);
                        if(this.interrupted()) {
                            System.out.println("I am interrupted,I will exit\r\n");
                            throw new InterruptedException("线程中断结束运行");
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        r1.start();
        Thread.sleep(3);
        r1.interrupt();
        Thread.sleep(2000);

        Thread r2 = new Thread(){
            public void run(){
                try {
                    System.out.printf("%s：%s--I am sleep！\r\n", df.format(new Date()), this.getName());
                    Thread.sleep(20000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        r2.start();
        try {
            Thread.sleep(200);
            r2.interrupt();
        }catch (InterruptedException ex){
            System.out.printf("%s：%s--Interrupted Exception！\r\n", df.format(new Date()), r2.getName());
        }
        Thread.sleep(2000);


        //暴力中断
        Thread r3 = new Thread(){
            public void run(){
                try {

                    for (int i = 0; i < 100; i++) {
                        System.out.printf("%s：%s--Running！times:%s\r\n", df.format(new Date()), this.getName(), i);
                        Thread.sleep(1000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        r3.start();
        r3.sleep(5000);
        try {
            r3.stop();
        }catch (ThreadDeath ex){System.out.printf("%s：%s--Interrupted Exception！\r\n", df.format(new Date()), r3.getName());ex.printStackTrace();}//不需要现实捕获
        Thread.sleep(2000);
        System.out.println("main end");
    }
}
