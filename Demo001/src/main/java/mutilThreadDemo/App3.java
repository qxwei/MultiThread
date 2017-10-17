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
}
