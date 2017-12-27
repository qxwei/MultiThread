package mutilThreadDemo;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Damon
 * @create 2017-12-13 11:09
 **/
class TaskNanjing implements Runnable {

    private Integer start;
    private Integer end;
    public TaskNanjing(Integer start,Integer end){
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
            for(int i=start;i<=end;i++) {
                try {
                    rowList = new ArrayList<>();
                    id = String.format("%06d", i + 1);
                    System.out.println(String.format("线程：%s,end:%s,运行中,id:%s", Thread.currentThread().getName(), end, id));
                    res = Jsoup.connect("http://njdt.njtjy.org.cn/lift/getLift/" + id).userAgent("Chrome").ignoreContentType(true).timeout(1000 * 60 * 6).execute();
                    ;

                    if (res == null) continue;
                    body = res.body();
                    if (body == null || StringUtil.isBlank(body)) continue;
                    JSONObject json = JSONObject.parseObject(body);

                    rowList.add(json.get("regcode"));
                    JSONObject maintain = (JSONObject) json.get("maintain");

                    if(maintain!=null)
                        rowList.add(maintain.get("maintainName"));
                    else rowList.add("");

                    JSONObject baseCompany = (JSONObject) json.get("baseCompany");
                    if(baseCompany!=null)
                    rowList.add(baseCompany.get("companyName"));
                    else rowList.add("");

                    JSONObject baseInsure = (JSONObject) json.get("baseInsure");
                    if(baseInsure!=null)
                        rowList.add(baseInsure.get("insureName"));
                    else rowList.add("");

                    JSONObject baseArea = (JSONObject) json.get("baseArea");
                    if(baseArea!=null)
                        rowList.add(baseArea.get("areaName"));
                    else rowList.add("");


                    rowList.add(json.get("liftaddress"));
                    rowList.add(json.get("inspect"));
                    rowList.add(json.get("lastinspect"));
                    rowList.add(json.get("nextinspect"));
                    rowList.add(json.get("begininsure"));
                    rowList.add(json.get("endinsure"));

                    if(baseInsure!=null)
                        rowList.add(baseInsure.get("contact"));
                    else rowList.add("");
                    rowList.add(json.get("placetype"));
                    rowList.add(json.get("longitude"));
                    rowList.add(json.get("latitude"));
                    rowList.add(json.get("lastmaintain"));
                    dataList.add(rowList);
                }catch (Exception ex){
                    System.out.println(id);
                    ex.printStackTrace();
                    continue;
                }
            }
            if(dataList!=null&&dataList.size()>0)
            Reptile_nanjing.createCSV(dataList ,start+"");
        } catch (Exception e) {
            System.out.println(id);
            e.printStackTrace();
        }

        System.out.println(String.format("线程：%s,运行结束",Thread.currentThread().getName()));
    }
}
public class Reptile_nanjing {
    public static void main(String[] args) throws InterruptedException {
        Reptile_nanjing reptile = new Reptile_nanjing();
        int total = 100000;
        int child = 10;
        int start = 50000;
        int step = (total-start)/child;

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for(int i=start;i<=total;i+=step){
            cachedThreadPool.execute(new TaskNanjing(i,i+step));
        }

        Thread.sleep(1000*60*60);
    }

    public synchronized static void createCSV(List<List<Object>> dataList ,String name) {
        // 表格头
        Object[] head = { "注册代码", "维保单位", "使用单位","保险公司","电梯区域","电梯地址","年检公司","上次检验日期","下次次检验日期","保险开始日期","保险结束日期","保险合同编号","区域类型","经度","维度","上次维保日期" };
        List<Object> headList = Arrays.asList(head);

        String fileName = "lift"+name+".csv";//文件名称
        String filePath = "E:/temp/南京电梯电梯数据/"; //文件路径

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

            //文件下载，使用如下代码
//            response.setContentType("application/csv;charset=gb18030");
//            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
//            ServletOutputStream out = response.getOutputStream();
//            csvWtriter = new BufferedWriter(new OutputStreamWriter(out, "GB2312"), 1024);

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
}
