package esign;

/**
 * @author Damon
 * @create 2017-12-22 16:41
 **/

public class EviURLResponse {
    private String url;
    private String evid;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private int errCode;
    private String msg;
    private boolean success;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEvid() {
        return evid;
    }

    public void setEvid(String evid) {
        this.evid = evid;
    }
}
