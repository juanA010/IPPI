package parentapp.ippi.ippiparent.model;

public class SignUpModel {

    String username, useremailAddress, userpassword, userphonenumber, userAddress, userAge,userGender ;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremailAddress() {
        return useremailAddress;
    }

    public void setUseremailAddress(String useremailAddress) {
        this.useremailAddress = useremailAddress;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUserphonenumber() {
        return userphonenumber;
    }

    public void setUserphonenumber(String userphonenumber) {
        this.userphonenumber = userphonenumber;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public SignUpModel (String emailAddress, String username,  String phonenumber, String Address, String Age, String Gender) {

        this.useremailAddress = emailAddress;
        this.username = username;
        this.userphonenumber = phonenumber;
        this.userAddress = Address;
        this.userAge = Age;
        this.userGender = Gender;
    }
}
