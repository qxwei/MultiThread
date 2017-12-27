package mutilThreadDemo;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author Damon
 * @create 2017-12-22 10:05
 **/

public class App6 {
    private final static String separator = "/";
    public static void main(String[] args) throws InterruptedException {
        try {

            Document document = new Document(PageSize.A4, 10, 10, 15, 10);
            String rootPath = "/srv/assets/images/";
            String path = "2017" + separator + "12" + separator + "22" + separator;
            File directory = new File(rootPath + "pdf" + separator + path);
            if (!directory.exists()) directory.mkdirs();
            path = rootPath + "pdf" + separator + path;
            String name = String.format("test%s.pdf", System.currentTimeMillis());
            PdfWriter.getInstance(document, new FileOutputStream(path+name));
//Step 3—Open the Document.
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font FontChinese = new Font(bfChinese, 11, Font.NORMAL);
            document.open();
//Step 4—Add content.
            document.add(new Paragraph("卖身契",FontChinese));
//Step 5—Close the Document.
            document.close();
            /*Document document = new Document(PageSize.A4, 10, 10, 15, 10);
            String rootPath = "/srv/assets/images/";
            String path = "2017" + separator + "12" + separator + "22" + separator;
            File directory = new File(rootPath + "pdf" + separator + path);
            if (!directory.exists()) directory.mkdirs();
            path = rootPath + "pdf" + separator + path;
            String name = String.format("test%s.pdf", System.currentTimeMillis());
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(String.format("E:/test%s.pdf", System.currentTimeMillis())));
            document.open();
            document.add(new Paragraph("卖身契"));
*//*            document.add(new Paragraph("同意吗？"));
            document.add(new Paragraph("签字处"));*//*
            document.close();*/
         //   System.out.println(directory.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
