import com.baidu.aip.ocr.AipOcr;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.function.Function;

/**
 * @author Damon
 * @create 2017-12-27 15:27
 **/

public class BaiduAi {
    //设置APPID/AK/SK
    public static final String APP_ID = "10591185";
    public static final String API_KEY = "GV19Pd1zsiqBQFeGbxd0VBZW";
    public static final String SECRET_KEY = "ctGOuNDvs6UexWEbaCYQEFG6XOVu1IDT";
    private static  AipOcr client = null;
    public BaiduAi(){
        client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
    }

    public void txtTrans(String url,String type,String ext) throws Exception {

        HashMap<String, String> options = new HashMap<>();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");
        options.put("recognize_granularity", "big");
        options.put("probability", "false");
        options.put("language_type", "CHN_ENG");
        options.put("probability", "true");
        options.put("vertexes_location", "false");

        URL httpUrl = new URL(url);
        File directory = new File( "OCR_IMG");
        if (!directory.exists()) directory.mkdirs();
        File file = new File(String.format("OCR_IMG%s%s",File.separator,System.currentTimeMillis()));
        FileUtils.copyURLToFile(httpUrl,file);

        byte[] byteArr = FileUtils.readFileToByteArray(file);
        JSONObject res = null;
        if(type.equals("businessLicense")) {
            res = client.businessLicense(byteArr, options);
        }else if(type.equals("idCard")) {
            String idCardSide = ext;
            res = client.idcard(byteArr, idCardSide, options);
            IdCard card = new IdCard();

            JSONObject result = (JSONObject)res.get("words_result");
            Function<String,String> f = x->((JSONObject)result.get(x)).get("words").toString();
            card.setName(f.apply("姓名"));
            card.setEthnic(f.apply("民族"));
            card.setAddress(f.apply("住址"));
            card.setNum(f.apply("公民身份号码"));
            card.setBirthday(f.apply("出生"));
            card.setGender(f.apply("性别"));
            if(res!=null)System.out.println(card.toString());
        }else if(type.equals("receipt")){
            res = client.receipt(byteArr, options);
        }
        if(res!=null)System.out.println(res.toString(2));
        else System.out.println("识别失败！");
    }

    public static void main(String[] args) {
        BaiduAi ai = new BaiduAi();
        // 调用接口
        String path = "http://lifts.img-cn-shenzhen.aliyuncs.com/profile/img/6189/1504575516737.jpg@!800w";
        String idCardUrl = "http://lifts.img-cn-shenzhen.aliyuncs.com/user/15578/001c9837f8ecfecc5870f53b826df073.jpg@!800w";
        try {
         //   ai.txtTrans(path,"businessLicense",null);
            ai.txtTrans(idCardUrl,"idCard","front");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}