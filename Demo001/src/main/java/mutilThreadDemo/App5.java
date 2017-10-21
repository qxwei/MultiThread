package mutilThreadDemo;

import org.junit.Test;

/**
 * Created by damon on 2017/10/21.
 */
public class App5 {

    /**
     * CPU时间让出Yield
     * @throws Exception
     */
    @Test
    public void testYield()throws Exception{
        boolean isYield = false;
        boolean finalIsYield = isYield;
        Runnable r = ()->{
                long begin = System.currentTimeMillis();
                int count = 0;
                for (int i = 1; i <= 10000000; i++) {
                    i++;
                    count = i;
                    if (finalIsYield) Thread.yield();
                }
                long end = System.currentTimeMillis();
                System.out.printf("%s:i=%s,用时：%s毫秒。\r\n", Thread.currentThread().getName(), count, (end-begin));
            };

        new Thread(r).start();
        Thread.sleep(5000);
        isYield=true;
        new Thread(r).start();
        Thread.sleep(5000);
    }

}
