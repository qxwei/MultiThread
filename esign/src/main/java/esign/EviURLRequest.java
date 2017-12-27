package esign;

import java.time.LocalDate;
import java.util.List;

/**
 * 存证URL请求 实体
 * @author Damon
 * @create 2017-12-22 15:43
 **/

public class EviURLRequest {
    private String eviName;
     private EviRequestContent content;

    public List<KeyValuePair> geteSignIds() {
        return eSignIds;
    }

    public void seteSignIds(List<KeyValuePair> eSignIds) {
        this.eSignIds = eSignIds;
    }

    private List<KeyValuePair> eSignIds;
    private List<KeyValuePair> bizIds;


    public List<KeyValuePair> getBizIds() {
        return bizIds;
    }

    public void setBizIds(List<KeyValuePair> bizIds) {
        this.bizIds = bizIds;
    }

    public String getEviName() {
        return eviName;
    }

    public void setEviName(String eviName) {
        this.eviName = eviName;
    }

    public EviRequestContent getContent() {
        return content;
    }

    public void setContent(EviRequestContent content) {
        this.content = content;
    }

    public static class EviRequestContent{
        private LocalDate storageLife;
        private String contentDescription;
        private String contentLength;
        private String contentBase64Md5;
        public LocalDate getStorageLife() {
            return storageLife;
        }

        public void setStorageLife(LocalDate storageLife) {
            this.storageLife = storageLife;
        }

        public String getContentDescription() {
            return contentDescription;
        }

        public void setContentDescription(String contentDescription) {
            this.contentDescription = contentDescription;
        }

        public String getContentLength() {
            return contentLength;
        }

        public void setContentLength(String contentLength) {
            this.contentLength = contentLength;
        }

        public String getContentBase64Md5() {
            return contentBase64Md5;
        }

        public void setContentBase64Md5(String contentBase64Md5) {
            this.contentBase64Md5 = contentBase64Md5;
        }
    }
    public static class KeyValuePair{
        private String type;
        private String value;
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
