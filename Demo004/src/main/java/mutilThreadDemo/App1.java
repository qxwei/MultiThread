package mutilThreadDemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class App1 {
    public static void main(String[] args){
        try {
            WaitService service = new WaitService();
            WaitThread th = new WaitThread(service);
            th.start();
            Thread.sleep(5);
            service.testSignal();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class WaitThread extends Thread{
   private WaitService service;
   public WaitThread(WaitService service){
       this.service = service;
   }
   public void run(){
       service.testWait();
   }
}

class WaitService{
    private ReentrantLock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();
    public void testWait() {

        try {
            lock.lock();
            for (int i = 0; i <= 100; i++) {
                if (i == 13) {
                    System.out.println("进入等待:"+System.currentTimeMillis());
                    condition.await();
                }
                if (i == 13) {
                    System.out.println("等待结束:"+System.currentTimeMillis());
                }
                System.out.println("打印值：" + i);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void testSignal() {
        try {
            lock.lock();
            System.out.println("通知唤醒:"+System.currentTimeMillis());
            condition.signal();
            System.out.println("通知唤醒完成:"+System.currentTimeMillis());
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            lock.unlock();
        }

    }
}