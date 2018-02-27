package mutilThreadDemo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Damon
 * @create 2018-01-09 13:12
 **/

public class MailReptile {
    public static void main(String[] args)  throws Exception {
        String mail_regex="[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+";
        TreeSet<String> list=new TreeSet<String>();
        Pattern p= Pattern.compile(mail_regex);
        String line=null;
        URL url=new URL("https://tieba.baidu.com/p/5314758062?pn=1");
        BufferedReader buf=new BufferedReader(new InputStreamReader(url.openStream()));
        while((line=buf.readLine())!=null) {
            Matcher m = p.matcher(line);
            while(m.find())
                list.add(m.group());
        }
        URL url2=new URL("https://tieba.baidu.com/p/5314758062?pn=2");
        BufferedReader buf2=new BufferedReader(new InputStreamReader(url2.openStream()));
        while((line=buf2.readLine())!=null) {
            Matcher m = p.matcher(line);
            while(m.find())
                list.add(m.group());
        }

        for (String s:list
                ) {
            System.out.println(s);
        }

    }
}
