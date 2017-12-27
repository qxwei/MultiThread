import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Damon
 * @create 2017-12-25 21:51
 **/

public class AlgorithmHelper {

    // protected  Logger LOG = LoggerFactory.getLogger(this.getClass());
    /***
     * 获取HMAC加密后的X-timevale-signature签名信息
     *
     * @param data
     *            加密前数据
     * @param key
     *            密钥
     * @param algorithm
     *            HmacMD5 HmacSHA1 HmacSHA256 HmacSHA384 HmacSHA512
     * @param encoding
     *            编码格式
     * @return HMAC加密后16进制字符串
     * @throws Exception
     */
    public static String getXtimevaleSignature(String data, String key, String algorithm, String encoding) {
        Mac mac = null;
        try {
            mac = Mac.getInstance(algorithm);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(encoding), algorithm);
            mac.init(secretKey);
            mac.update(data.getBytes(encoding));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //    LOG.info("获取Signature签名信息异常：" + e.getMessage());
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            //     LOG.info("获取Signature签名信息异常：" + e.getMessage());
            return null;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            //    LOG.info("获取Signature签名信息异常：" + e.getMessage());
            return null;
        }
        return byte2hex(mac.doFinal());
    }

    /***
     * 计算Content-MD5 1.先计算MD5加密的二进制数组（128位）。 2.再对这个二进制进行base64编码（而不是对32位字符串编码）。
     *
     * @param filePath
     * @return
     */
    public static String getContentMD5(String filePath) {
        // 获取文件MD5的二进制数组（128位）
        byte[] bytes = getFileMD5Bytes128(filePath);
        // 对文件MD5的二进制数组进行base64编码
        return new String(Base64.encodeBase64(bytes));
    }

    /***
     * 获取文件MD5-二进制数组（128位）
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] getFileMD5Bytes128(String filePath) {
        FileInputStream fis = null;
        byte[] md5Bytes = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md5.update(buffer, 0, length);
            }
            md5Bytes = md5.digest();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //      LOG.info(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //     LOG.info(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            //       LOG.info(e.getMessage());
        }
        return md5Bytes;
    }

    /***
     * 获取文件MD5-字符串（32位）
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(String filePath) {
        BigInteger bigInt = new BigInteger(1, getFileMD5Bytes128(filePath));
        return bigInt.toString(16);
    }


    public static byte[] File2byte(String filePath){
        byte[] buffer = null;
        try{
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }
    /***
     * 将byte[]转成16进制字符串
     *
     * @param data
     *
     * @return 16进制字符串
     */
    public static String byte2hex(byte[] data) {
        StringBuilder hash = new StringBuilder();
        String stmp;
        for (int n = 0; data != null && n < data.length; n++) {
            stmp = Integer.toHexString(data[n] & 0XFF);
            if (stmp.length() == 1)
                hash.append('0');
            hash.append(stmp);
        }
        return hash.toString();
    }
    public static String HMACSHA256(byte[] data, byte[] key)
    {
        try  {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return byte2hex(mac.doFinal(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String byte2File(byte[] buf, String filePath, String fileName)
    {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try
        {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory())
            {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
            return file.getAbsolutePath();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

}
