package parentapp.ippi.ippiparent.model;

public class ParentsLocationData {
    double Latitude;
    double Longitude;

    public ParentsLocationData (double Latitude, double Longitude) {

        this.Latitude = Latitude;
        this.Longitude = Longitude;

    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }






}
