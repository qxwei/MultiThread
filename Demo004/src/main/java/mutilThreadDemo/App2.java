package mutilThreadDemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Damon
 * @create 2018-02-28 12:22
 **/

public class App2 {
    public static void main(String[] args) throws InterruptedException {
        ConditionService service = new ConditionService();
        ConditionThread th1 = new ConditionThread(service,true);
        ConditionThread th2 = new ConditionThread(service,false);
        th1.start();
        th2.start();
        Thread.sleep(5);
        service.signalC2();
        service.signalC1();
        Thread.sleep(5000);
    }
}
class ConditionThread extends Thread{
    boolean isRun1 = false;
    public ConditionService service;
    public ConditionThread(ConditionService service,boolean run1){
        super();
        this.service = service;
        this.isRun1=run1;
    }
    public void run(){
        if(isRun1)service.conditionMethod1();
        else service.conditionMethod2();
    }
}
class ConditionService{
    private ReentrantLock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    public void conditionMethod1(){
        try {
            lock.lock();
            for (int i = 0; i <= 10; i++) {
                if (i == 3) {
                    System.out.println("方法conditionMethod1进入等待");
                    c1.await();
                }
                System.out.println("方法conditionMethod1打印值："+i);
                Thread.sleep(10);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void signalC1(){
        try{
            lock.lock();
            System.out.println("唤醒condition1");
            c1.signalAll();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void conditionMethod2(){
        try {
            lock.lock();
            for (int i = 0; i <= 10; i++) {
                if (i == 3) {
                    System.out.println("方法conditionMethod2进入等待");
                    c2.await();
                }
                System.out.println("方法conditionMethod2打印值："+i);
                Thread.sleep(10);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void signalC2(){
        try{
            lock.lock();
            System.out.println("唤醒condition2");
            c2.signalAll();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
