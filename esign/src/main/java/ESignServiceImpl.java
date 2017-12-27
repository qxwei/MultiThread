import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.timevale.esign.sdk.tech.bean.PosBean;
import com.timevale.esign.sdk.tech.bean.SignPDFStreamBean;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.timevale.esign.sdk.tech.bean.result.GetAccountProfileResult;
import com.timevale.esign.sdk.tech.bean.result.Result;
import com.timevale.esign.sdk.tech.impl.constants.SignType;
import com.timevale.esign.sdk.tech.service.factory.AccountServiceFactory;
import com.timevale.esign.sdk.tech.service.factory.EsignsdkServiceFactory;
import com.timevale.esign.sdk.tech.service.factory.UserSignServiceFactory;
import com.timevale.tech.sdk.bean.HttpConnectionConfig;
import com.timevale.tech.sdk.bean.ProjectConfig;
import com.timevale.tech.sdk.bean.SignatureConfig;
import com.timevale.tech.sdk.constants.AlgorithmType;
import com.timevale.tech.sdk.constants.HttpType;
import esign.EviURLRequest;
import esign.EviURLResponse;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author Damon
 * @create 2017-12-25 21:19
 **/

public class ESignServiceImpl {

    private static boolean isInit = false;
    private static final String projectId = "1111563517";
    private static final String projectSecret = "95439b0863c241c63a861b87d1e647b7";
    private static final String eviRequestUrl = "http://smlcunzheng.tsign.cn:8083/evi-service/evidence/v1/preservation/original/url";


    public void init(){
        ProjectConfig projectConfig = new ProjectConfig();
        projectConfig.setItsmApiUrl("http://121.40.164.61:8080/tgmonitor/rest/app!getAPIInfo2");
        projectConfig.setProjectId("1111563517");
        projectConfig.setProjectSecret("95439b0863c241c63a861b87d1e647b7");
        HttpConnectionConfig httpConfig = new HttpConnectionConfig();
            httpConfig.setHttpType(HttpType.HTTP);

        //  httpConfig.setProxyIp(proxyIp);
        //  httpConfig.setProxyPort(proxyPort);
        httpConfig.setRetry(5);
        SignatureConfig signConfig = new SignatureConfig();
        signConfig.setPrivateKey("");
        signConfig.setEsignPublicKey("");

        Result result = EsignsdkServiceFactory.instance().init(projectConfig, httpConfig, signConfig);
        if(result.getErrCode()==0&&result.getMsg().equals("成功")) isInit = true;
    }
    public FileDigestSignResult drawSign(String sealData,String accountId,String key) throws DocumentException, IOException {
        if(!isInit)init();
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font FontChinese = new Font(bfChinese, 11, Font.NORMAL);
        SignType signType = SignType.Key; //关键字定位

        GetAccountProfileResult profile = AccountServiceFactory.instance().getAccountInfoByIdNo(accountId,11);
        accountId = profile.getAccountInfo().getAccountUid();
        PosBean signPos = new PosBean();
        signPos.setPosType(signType.val()).setKey(key).setPosType(2).setPosX(0).setPosY(0).setWidth(200);

        SignPDFStreamBean stream = new SignPDFStreamBean();// 文件流，待签署文档本地二进制数据

        Document document = new Document(PageSize.A4, 10, 10, 15, 10);

        File directory = new File("E:/esign/pdf");
        if (!directory.exists()) directory.mkdirs();

        String name = String.format("%s.pdf", System.currentTimeMillis());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("E:/esign/pdf/"+name));
        document.open();
        document.add(new Paragraph("卖身契",FontChinese));
        document.add(new Paragraph("同意吗？",FontChinese));
        document.add(new Paragraph("签字处一",FontChinese));
     /*   document.add(new Paragraph("签字处二",FontChinese));*/
        writer.flush();
        document.close();
        writer.close();

        stream.setStream(Files.readAllBytes(Paths.get("E:/esign/pdf/"+name)));

        FileDigestSignResult result = UserSignServiceFactory.instance().localSignPDF(accountId,sealData, stream, signPos, com.timevale.esign.sdk.tech.impl.constants.SignType.Key);// null表示这两个参数可空
        //   com.timevale.esign.sdk.tech.bean.sign.SignType
        if (0 != result.getErrCode()) {
            System.out.println("平台用户摘要签署（文件流）失败，错误码=" + result.getErrCode() + ",错误信息="
                    + result.getMsg());
            return null;
        }
        String dstPath = AlgorithmHelper.byte2File(result.getStream(),"E:/esign/pdf/",String.format("签名后文件%s.pdf", System.currentTimeMillis()));
        result.setStream(null);
        result.setDstFilePath(dstPath);
        return result;
    }

    public EviURLResponse requestEviURL(String path,String singServiceId) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.ACCEPT, "application/json");
        headers.add("X-timevale-mode", "package");
        headers.add("X-timevale-project-id", projectId);
        headers.add("X-timevale-signature-algorithm", "hmac-sha256");

        EviURLRequest request = new EviURLRequest();
        request.setEviName(String.format("E电梯维修单存证%s",System.currentTimeMillis()));
        File pdf = new File(path);
        EviURLRequest.EviRequestContent content = new EviURLRequest.EviRequestContent();
        content.setContentLength(pdf.length()+"");
        content.setContentDescription(pdf.getName());
        content.setContentBase64Md5(AlgorithmHelper.getContentMD5(path));
        request.setContent(content);
        java.util.List<EviURLRequest.KeyValuePair> signIds = new ArrayList<EviURLRequest.KeyValuePair>();
        EviURLRequest.KeyValuePair pair = new EviURLRequest.KeyValuePair();
        pair.setType("0");
        pair.setValue(singServiceId);
        signIds.add(pair);
        request.seteSignIds(signIds);


        ParameterizedTypeReference<EviURLResponse> responseType = new ParameterizedTypeReference<EviURLResponse>(){};
        ObjectMapper mapper = new ObjectMapper();
        String signature = mapper.writeValueAsString(request);
        System.out.println(signature);
        signature =  AlgorithmHelper.HMACSHA256(signature.getBytes(),projectSecret.getBytes());
        System.out.println(signature);

        headers.add("X-timevale-signature", signature);
        HttpEntity httpRequest = new HttpEntity<EviURLRequest>(request,headers);
        ResponseEntity<EviURLResponse> response = restTemplate.exchange(eviRequestUrl, HttpMethod.POST, httpRequest, responseType);

        EviURLResponse result = response.getBody();
        return result;
    }

    public String uploadEviDoc(String uploadEviUrl,String path) throws IOException {
        uploadEviDoc1( uploadEviUrl, path);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper mapper = new ObjectMapper();

        //headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        headers.add("Content-MD5", AlgorithmHelper.getContentMD5(path));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        ParameterizedTypeReference<Object> responseType = new ParameterizedTypeReference<Object>(){};
        ;       byte[] buffer = AlgorithmHelper.File2byte(path);

        File file = new File(path);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPut put = new HttpPut(uploadEviUrl);
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
        MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
        mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mEntityBuilder.addPart("file",fileBody);
        mEntityBuilder.addBinaryBody("file", new File(path), ContentType.DEFAULT_BINARY, file.getName());
        put.setEntity(mEntityBuilder.build());
        put.setHeader(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        put.setHeader("Content-MD5", AlgorithmHelper.getContentMD5(path));

        CloseableHttpResponse httpResponse = httpclient.execute(put);
        String result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        System.out.println(result);
        HttpEntity httpRequest = new HttpEntity<byte[]>(buffer, headers);
        ResponseEntity response = restTemplate.exchange(uploadEviUrl, HttpMethod.PUT, httpRequest, responseType);

        String strRequest =  mapper.writeValueAsString(httpResponse);
        return strRequest;

        /*HttpEntity httpRequest = new HttpEntity<>(buffer, headers);
        ResponseEntity response = restTemplate.exchange(uploadEviUrl, HttpMethod.PUT, httpRequest, responseType);

        Object result = response.getBody();
        String strRequest =  mapper.writeValueAsString(result);
        return strRequest;*/
    }

    public void uploadEviDoc1(String uploadEviUrl,String path) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        // Create an instance of HttpClient.
        HttpClient client = new HttpClient();

        // Create a method instance.
        PutMethod method = new PutMethod(uploadEviUrl);
        method.setRequestBody(new FileInputStream(path));
        method.addRequestHeader("Content-MD5", AlgorithmHelper.getContentMD5(path));
        method.addRequestHeader(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary data
            System.out.println(new String(responseBody));

        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
    }
}
