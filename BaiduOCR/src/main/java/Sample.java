import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author Damon
 * @create 2018-01-12 13:05
 **/

public class Sample {
        //设置APPID/AK/SK
        public static final String APP_ID = "10591185";
        public static final String API_KEY = "GV19Pd1zsiqBQFeGbxd0VBZW";
        public static final String SECRET_KEY = "ctGOuNDvs6UexWEbaCYQEFG6XOVu1IDT";

        public static void main(String[] args) {
            // 初始化一个AipOcr
            AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

            // 可选：设置网络连接参数
            client.setConnectionTimeoutInMillis(2000);
            client.setSocketTimeoutInMillis(60000);

            // 调用接口
            String path = "e:/temp/333.png";
            String result = "";
                JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
                result +=res.toString(2);
            System.out.println(result);
        }
}
