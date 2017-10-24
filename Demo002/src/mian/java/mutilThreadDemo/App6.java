package mutilThreadDemo;

import org.junit.Test;

/**
 * @author Damon
 * @create 2017-10-24 12:49
 **/

public class App6 {

    @Test
    public void TestStaticInnerClass()throws Exception {
        OutClass.InnerClass1 inner1 = new OutClass.InnerClass1();
        OutClass.InnerClass2 inner2 = new OutClass.InnerClass2();
        Thread t1 = new Thread(() -> {
            inner1.LockOther(inner2);
        }, "T1");
        Thread t2 = new Thread(() -> {
            inner1.LockMethod1();
        }, "T2");
        Thread t3 = new Thread(() -> {
            inner2.LockMethod2();
        }, "T3");

        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(5000);
    }
}

class OutClass{
    static class InnerClass1{
        public void LockOther(InnerClass2 class2){
            try{
                synchronized (class2) {
                    String name = Thread.currentThread().getName();
                    for (int i = 0; i <= 5; i++) {
                        System.out.printf("Thread name：%s,count i=%s,InnerClass1 LockOther\r\n", name, i);
                        Thread.sleep(100);
                    }
                }
            }catch (Exception ex){ex.printStackTrace();}
        }

        synchronized public void LockMethod1(){
            try{
                String name = Thread.currentThread().getName();
                for (int i = 0; i <= 5; i++) {
                    System.out.printf("Thread name：%s,count i=%s,InnerClass1 LockMethod1\r\n", name, i);
                    Thread.sleep(100);
                }
            }catch (Exception ex){ex.printStackTrace();}
        }
    }

    static class InnerClass2{
        synchronized public void LockMethod2(){
            try{
                String name = Thread.currentThread().getName();
                for (int i = 0; i <= 5; i++) {
                    System.out.printf("Thread name：%s,count i=%s,InnerClass2 LockMethod2\r\n", name, i);
                    Thread.sleep(100);
                }
            }catch (Exception ex){ex.printStackTrace();}
        }
    }
}