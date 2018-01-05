package mutilThreadDemo;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

/**
 * @author Damon
 * @create 2018-01-05 12:49
 **/

public class app7 {

    private static WatchService watchService;
    private static String filename;

    static {

        try {
            File file = new File("E:/temp/client_key.txt");
            filename = file.getName();
            watchService = FileSystems.getDefault().newWatchService();
            Paths.get(file.getParent()).register(watchService,StandardWatchEventKinds.ENTRY_MODIFY);
        } catch(IOException e) {
            e.printStackTrace();
        }

        //启动一个线程监听内容变化，并重新载入配置
        Thread watchThread = new Thread() {
            public void run() {
                while(true) {
                    try {
                        WatchKey watchKey = watchService.take();
                        for (WatchEvent event : watchKey.pollEvents()) {
                            if (Objects.equals(event.context().toString(), filename)){
                                System.out.println("文件修改了。");
                            }
                            watchKey.reset();
                        }
                    } catch (Exception e) {

                    }
                }
            };
        };

        //设置成守护进程
        watchThread.setDaemon(true);
        watchThread.start();

        //当服务器进程关闭时把监听线程close掉
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try{
                    watchService.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(">>>>>>>>>>>>>>");
        Thread.sleep(3*60*1000);
    }
}