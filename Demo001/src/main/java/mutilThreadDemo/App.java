package mutilThreadDemo;

/**
 * Hello world!
 ** **app:** java线程的一些基本属性使用
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        System.out.println("虚拟机内核数："+Runtime.getRuntime().availableProcessors());
        Runnable r = ()->{
            Thread th = Thread.currentThread();
            System.out.printf("我是：%s,我的id:%s,我的优先级:%s,是否存活：%s,当前状态：%s\n",
                    th.getName(),th.getId(),th.getPriority(),th.isAlive(),th.getState());
        };
        Thread th1 = new Thread(r,"线程1号");
        Thread th2 = new Thread(r,"线程2号");
        th1.start();
        th2.start();

        Thread.sleep(1000);
    }
}
