package parentapp.ippi.ippiparent.model;

public class AvailableBabysitter {
    private String username;
    private String rating;

    public AvailableBabysitter() {
    }
    public AvailableBabysitter(String username, String rating) {
        this.username = username;
        this.rating = rating;
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
