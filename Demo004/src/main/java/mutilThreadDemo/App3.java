package mutilThreadDemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Damon
 * @create 2018-03-05 14:31
 **/

public class App3 {

    public static void main(String[] args){
        try {
            PCService service = new PCService();
            PThread p = new PThread(service);
            CThread c = new CThread(service);
            p.start();
            c.start();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class PThread extends Thread{
    PCService PService;

    public PThread(PCService p){
        this.PService = p;
    }
    public void run(){
        for(int i=0;i<=10;i++) {
            PService.set();
        }
    }
}
class CThread extends Thread{
    PCService CService;

    public CThread(PCService c){
        this.CService = c;
    }
    public void run(){
        for(int i=0;i<=10;i++) {
            CService.get();
        }
    }
}
class PCService{
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private boolean hasValue = false;
    public void set(){
        try {
            lock.lock();
            while (hasValue) {
                condition.await();
            }
            System.out.println("生产了一个产品");
            hasValue = true;
            condition.signal();
        }catch (Exception ex){ex.printStackTrace();}
        finally {
            lock.unlock();
        }
    }
    public void get(){
        try {
            lock.lock();
            while (!hasValue) {
                condition.await();
            }
            System.out.println("消费了一个产品");
            hasValue = false;
            condition.signal();
        }catch (Exception ex){ex.printStackTrace();}
        finally {
            lock.unlock();
        }
    }
}
