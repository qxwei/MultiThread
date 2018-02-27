

/**
 * @author Damon
 * @create 2017-12-27 17:06
 **/

public class IdCard {

    private Long id;

    private String name;

    private String num;

    private String birthday;

    private String address;

    private String gender;

    private String ethnic;

    private String agency;

    private String startDate;

    private String endStart;

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndStart() {
        return endStart;
    }

    public void setEndStart(String endStart) {
        this.endStart = endStart;
    }

    @Override
    public String toString() {
        return "IdCard{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", num='" + num + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", ethnic='" + ethnic + '\'' +
                ", agency='" + agency + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endStart='" + endStart + '\'' +
                ", userId=" + userId +
                '}';
    }
}

