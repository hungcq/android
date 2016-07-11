package raijin.session6_hw;

/**
 * Created by 1918 on 11-Jul-16.
 */
public class PersonInfo {
    private String name;
    private String age;
    private String phone;
    private String address;
    private String gender;

    public PersonInfo(String name, String age, String phone, String address, String gender) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
