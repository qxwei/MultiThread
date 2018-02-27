package mutilThreadDemo;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Damon
 * @create 2018-02-27 9:44
 **/

public class App {
    public static void main(String[] args) throws InterruptedException {
        MyService service = new MyService();
        MyThread r1 = new MyThread(service);
        MyThread r2 = new MyThread(service);
        MyThread r3 = new MyThread(service);

        r1.start();
        r2.start();
        r3.start();
        Thread.sleep(5000);
    }

}

class MyThread extends Thread{
    public MyService service;

    public MyThread(MyService service){
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.testMethod();
    }
}
class MyService{
    private ReentrantLock lock = new ReentrantLock();
    public void testMethod(){
        lock.lock();
        for(int i=0;i<=10;i++){
            System.out.println(String.format("当前线程%s:打印值：%s",Thread.currentThread().getName(),i));
        }
        lock.unlock();
    }
}