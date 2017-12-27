package mutilThreadDemo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Damon
 * @create 2017-12-13 11:09
 **/
class MutableInt {
    private int value;

    public MutableInt(int initial) {
        value = initial;
    }

    public int get() {
        return value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public void inc() {
        value++;
    }
}
class Task implements Runnable {

    private Integer start;
    private Integer end;
    public Task(Integer start,Integer end){
        this.start = start;
        this.end = end;
    }
    @Override
    public void run() {
        String id ="";
        Thread.currentThread().setName(start+"");
        System.out.println(String.format("线程：%s,end:%s,运行中",Thread.currentThread().getName(),end));
        try {
            Document doc=null;
            List<List<Object>> dataList = new ArrayList<List<Object>>();
            List<Object> rowList = null;
            for(int i=start;i<=end;i++) {
                rowList = new ArrayList<Object>();
                id = String.format("%06d", i+1);
                System.out.println(String.format("线程：%s,end:%s,运行中,id:%s",Thread.currentThread().getName(),end,id));
                doc = Jsoup.connect("http://wx.zhanben.net.cn:8081/lmp-api/sbjbxx/querySbjbxxByZcdm.action")
                        .data("zcdm",id).userAgent("Chrome").timeout(1000*60*6).post();
                //.cookie("auth", "token")
                if(doc==null)continue;
                Element table = doc.select("table").first();
                if(table==null)continue;
                table = table.ownerDocument().selectFirst("tbody");

                List<Node> nodes = new ArrayList<>(table.childNodes());
                List<Node> textNodes = new ArrayList<>(nodes.stream().filter(x -> x instanceof TextNode).collect(Collectors.toList()));
                nodes.removeAll(textNodes);

                for (int j = 1; j < nodes.size(); j++) {
                    Node item = nodes.get(j);
                    String nodeText = ((Element) item.childNodes().get(3)).text();
                    rowList.add(nodeText);
                }
                dataList.add(rowList);
            }
            Reptile.createCSV(dataList ,start+"");
        } catch (IOException e) {
            System.out.println(id);
            e.printStackTrace();
        }

        System.out.println(String.format("线程：%s,运行结束",Thread.currentThread().getName()));
    }
}
public class Reptile {
    public static void main(String[] args) throws InterruptedException {
        Reptile reptile = new Reptile();
        int total = 100000;
        int child = 10;
        int start = 58000;
        int step = (total-start)/child;

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for(int i=start;i<=total;i+=step){
       //     reptile.teamRun(i,step);
         //   Integer end = start+step;
            cachedThreadPool.execute(new Task(i,i+step));
        }

        Thread.sleep(1000*60*60);
    }

    public synchronized static void createCSV(List<List<Object>> dataList ,String name) {

        // 表格头
        Object[] head = { "注册代码", "设备编号", "项目地点","使用单位","制造单位","维保单位","检验单位","下次检验日期" };
        List<Object> headList = Arrays.asList(head);

        String fileName = "lift"+name+".csv";//文件名称
        String filePath = "E:/temp/"; //文件路径

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
