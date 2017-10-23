package mutilThreadDemo;

import org.junit.Test;

/**
 * Created by damon on 2017/10/22.
 */
public class App1 {
    /**
     * 对象锁
     * @throws Exception
     */
    @Test
    public void testObjectLock()throws Exception{
        SyncObject so = new SyncObject();
        Runnable r = ()->{
            try {
                so.runA(Thread.currentThread().getName());
            }catch (Exception ex){ex.printStackTrace();}
        };
        Runnable r1 = ()->{
            try {
                synchronized (so){
                    so.runA(Thread.currentThread().getName());
                }
            }catch (Exception ex){ex.printStackTrace();}
        };
        Thread t1 = new Thread(r,"子线程1号");
        Thread t2 = new Thread(r,"子线程2号");
        t1.start();
        t2.start();
        Thread.sleep(2000);//防止线程结束资源回收
        System.out.println("同步执行");
         t1 = new Thread(r1,"子线程3号");
         t2 = new Thread(r1,"子线程4号");
        t1.start();
        t2.start();
        Thread.sleep(5000);//防止线程结束资源回收
    }
}

class SyncObject{
    public void runA(String name)throws Exception{
        for(int i=0;i<10;i++) {
            System.out.printf("caller：%s,i=%s\r\n", name,i);
            Thread.sleep(100);
        }
    }
}
