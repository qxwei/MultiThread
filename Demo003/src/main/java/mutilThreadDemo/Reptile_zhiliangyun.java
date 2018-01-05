package mutilThreadDemo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Damon
 * @create 2017-12-13 11:09
 **/
public class Reptile_zhiliangyun {
    static Object lock = new Object();
    static boolean waitFileModify = true;

    private static WatchService watchService;
    private static String filename;

    static {

        try {
            File file = new File("E:/temp/client_key.txt");
            filename = file.getName();
            watchService = FileSystems.getDefault().newWatchService();
            Paths.get(file.getParent()).register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
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
                                synchronized (lock) {
                                    waitFileModify = false;
                                    lock.notifyAll();
                                }
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
        Reptile_zhiliangyun reptile = new Reptile_zhiliangyun();
        int total = 100000;
        int child = 20;
        int start = 0;
        int step = (total-start)/child;

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for(int i=start;i<=total;i+=step){
            cachedThreadPool.execute(new Reptile_zhiliangyun.TaskZhiliangyun(i,i+step));
        }

        Thread.sleep(1000*60*60);
    }

    public synchronized static void createCSV(List<List<Object>> dataList ,String name) {
        // 表格头
        Object[] head = { "电梯编号","注册代码", "制造单位","监管单位", "使用单位","设备地址","投用日期","检查机构","上次检验日期","下次次检验日期","维保单位","本次维保日期","下次维保日期","状态","经度","维度"};
        List<Object> headList = Arrays.asList(head);

        String fileName = "lift"+name+".csv";//文件名称
        String filePath = "E:/temp/质量云/"; //文件路径

        File csvFile = null;
        BufferedWriter csvWtriter = null;
        try {
            csvFile = new File(filePath + fileName);
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();

            // GB2312使正确读取分隔符","
            csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 1024);


            int num = headList.size() / 2;
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < num; i++) {
                buffer.append(" ,");
            }
            csvWtriter.write(buffer.toString() + fileName + buffer.toString());
            csvWtriter.newLine();

            // 写入文件头部
            writeRow(headList, csvWtriter);

            // 写入文件内容
            for (List<Object> row : dataList) {
                writeRow(row, csvWtriter);
            }
            csvWtriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWtriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写一行数据
     * @param row 数据列表
     * @param csvWriter
     * @throws IOException
     */
    private synchronized static void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
        for (Object data : row) {
            StringBuffer sb = new StringBuffer();
            String rowStr = sb.append("\"").append(data).append("\",").toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }

    public synchronized static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }


    static class TaskZhiliangyun implements Runnable {

        private Integer start;
        private Integer end;
        public TaskZhiliangyun(Integer start,Integer end){
            this.start = start;
            this.end = end;
        }
        @Override
        public void run() {
            String id ="";
            Thread.currentThread().setName(start+"");
            System.out.println(String.format("线程：%s,end:%s,运行中",Thread.currentThread().getName(),end));
            try {
                Connection.Response res=null;
                String body = null;
                List<List<Object>> dataList = new ArrayList<List<Object>>();
                List<Object> rowList = null;
                String client_key = Reptile_zhiliangyun.txt2String(new File("E:/temp/client_key.txt"));
                for(int i=start;i<=end;i++) {
                    try {
                        rowList = new ArrayList<>();
                        id = String.format("%06d", i + 1);
                        System.out.println(String.format("线程：%s,end:%s,运行中,id:%s", Thread.currentThread().getName(), end, id));
                        String url = "http://app.gzqts.gov.cn/QCApp/near_facilities?client_key="+client_key+"&keywords="+id+"&size=15&lon=4.9E-324&page=1&type=1&lat=4.9E-324";
                        res = Jsoup.connect(url).userAgent("Chrome").ignoreContentType(true).timeout(1000 * 60 * 6).execute();
                        ;

                        if (res == null) continue;
                        body = res.body();
                        if (body == null || StringUtil.isBlank(body)) continue;
                        //   {"error_code":"timeout","ret":"error"}
                        JSONObject json = JSONObject.parseObject(body);

                        if(json.containsKey("error_code")){
                            synchronized (lock) {
                                while (waitFileModify) {
                                    System.out.println(String.format("%s进入等待", Thread.currentThread().getName()));
                                    lock.wait();
                                }
                                System.out.println(String.format("%s跳出等待队列", Thread.currentThread().getName()));
                                lock.notifyAll();
                                i--;
                                client_key = Reptile_zhiliangyun.txt2String(new File("E:/temp/client_key.txt"));
                                continue;
                            }
                        }

                        JSONArray content = (JSONArray) json.get("content");

                        if(content==null||content.size()<=0) continue;
                        json = (JSONObject)content.get(0);
                        String fid = json.getString("facility_id");
                        url = "http://app.gzqts.gov.cn/QCApp/facility_detail/"+fid+"?client_key="+client_key;
                        res = Jsoup.connect(url).userAgent("Chrome").ignoreContentType(true).timeout(1000 * 60 * 6).execute();

                        if (res == null) continue;
                        body = res.body();
                        if (body == null || StringUtil.isBlank(body)) continue;
                        //   {"error_code":"timeout","ret":"error"}
                        json = JSONObject.parseObject(body);

                        JSONObject targetContent = (JSONObject) json.get("content");
                        JSONObject base = (JSONObject) targetContent.get("base");
                        JSONObject check = (JSONObject) targetContent.get("check");
                        JSONObject repair = (JSONObject) targetContent.get("repair");
                        //   Object[] head = { "电梯编号", "制造单位", "使用单位","设备地址","投用日期","检查机构","上次检验日期","下次次检验日期","维保单位","本次维保日期","下次维保日期"};
                        rowList.add(base.get("name"));
                        rowList.add(targetContent.get("facility_id"));
                        rowList.add(base.get("make_company"));
                        rowList.add(base.get("regist_office"));
                        rowList.add(base.get("use_company"));
                        rowList.add(base.get("install_address"));
                        rowList.add(base.get("years"));
                        rowList.add(check.get("check_company"));
                        rowList.add(check.get("pre_check_date"));
                        rowList.add(check.get("next_check_date"));
                        rowList.add(repair.get("repair_company"));
                        rowList.add(repair.get("pre_repair_date"));
                        rowList.add(repair.get("next_repair_date"));
                        rowList.add(targetContent.get("status"));
                        rowList.add(targetContent.get("lat"));
                        rowList.add(targetContent.get("lon"));
                        dataList.add(rowList);
                        waitFileModify = true;
                    }catch (Exception ex){
                        System.out.println(id);
                        ex.printStackTrace();
                        continue;
                    }
                }
                if(dataList!=null&&dataList.size()>0)
                    Reptile_zhiliangyun.createCSV(dataList ,start+"");
            } catch (Exception e) {
                System.out.println(id);
                e.printStackTrace();
            }

            System.out.println(String.format("线程：%s,运行结束",Thread.currentThread().getName()));
        }
    }

}