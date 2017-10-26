package mutilThreadDemo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Producer Consumers
 * Created by damon on 2017/10/26.
 */
public class App2 {
    @Test
    public void testP_c()throws Exception{
        Object lock = new Object();
        Warehouse store = new Warehouse();
        Runnable r_p =  ()->{
            while(true) {
                try {
                    synchronized (lock) {
                        while (store.shelf.size() >= 2) {
                        //    System.out.println("生产者进入等待");
                            lock.wait();
                        }
                        store.in("产品");
                        lock.notifyAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable r_c =  ()->{
            while(true) {
                try {
                    String name = Thread.currentThread().getName();
                    synchronized (lock) {
                        while (store.shelf.size() <= 0) {
                      //      System.out.println("消费者进入等待");
                            lock.wait();
                        }
                        store.out();
                        lock.notifyAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        for(int i=0;i<100;i++){
            new Thread(r_p,"worker_"+i).start();
        }

        for(int i=0;i<90;i++){
            new Thread(r_c,"Consumers_"+i).start();
        }
        while(true){}
    }
}
class Warehouse {

    /**
     * 代码控制库存为2
     */
    public List<String> shelf = new ArrayList();

    public List getShelf() {
        return shelf;
    }

    public void setShelf(List shelf) {
        shelf = shelf;
    }

    public boolean in(String p)throws Exception{
        String name = Thread.currentThread().getName();
        System.out.printf("%s:%s入库，入库前库存：%s\r\n",System.currentTimeMillis(),name,shelf.size());
        shelf.add(p);
       // Thread.sleep(3000);
        System.out.printf("%s:%s入库后库存：%s\r\n",System.currentTimeMillis(),name,shelf.size());
        return true;
    }

    public String out()throws Exception{
        String name = Thread.currentThread().getName();
        System.out.printf("%s:%s出库，出库前库存：%s\r\n",System.currentTimeMillis(),name,shelf.size());
        String p = shelf.get(0);
        shelf.remove(0);
     //   Thread.sleep(3000);
        System.out.printf("%s:%s出库后库存：%s\r\n",System.currentTimeMillis(),name,shelf.size());
        return p;
    }
}
