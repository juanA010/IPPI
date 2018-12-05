package parentapp.ippi.ippiparent.model;

public class AvailableBabysitter {
    private String username;
    private String rating;
    private String userAge;
    private String userGender;

    public AvailableBabysitter() {
    }

    public AvailableBabysitter(String username, String rating, String userAge, String userGender) {
        this.username = username;
        this.rating = rating;
        this.userAge = userAge;
        this.userGender = userGender;

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



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


}
