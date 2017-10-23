package mutilThreadDemo;

import org.junit.Test;

/**
 * Created by damon on 2017/10/22.
 */
public class App7{

    @Test
    public void testDaemon(){
        Runnable r = ()->{
            for(int i=1;i<=100000;i++){
                System.out.printf("守护线程正在运行，计算100000，当前:%s\r\n",i);
            }
        };
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();
        System.out.println("main emd");
    }
}
