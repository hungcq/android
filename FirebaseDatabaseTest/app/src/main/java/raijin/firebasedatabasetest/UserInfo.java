package raijin.firebasedatabasetest;

/**
 * Created by 1918 on 21-Dec-16.
 */

public class UserInfo {

    private String name;
    private String phoneNumber;
    private String avatarUrl;

    public UserInfo(String name, String phoneNumber, String avatarUrl) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.avatarUrl = avatarUrl;
    }

    public UserInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
