package ly.generalassemb.drewmahrt.project_03.model;

/**
 * Created by Drew on 12/3/15.
 */
public class Venue {
    private String name;
    private String city;
    private double latitude;
    private double longitude;
    private String category;

    public Venue() {
        this.latitude = 0;
        this.longitude = 0;
        this.name = "";
        this.city = "";
        this.setCategory("");
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLatitude(double lat){
        this.latitude = lat;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public String getCity() {
        if (city.length() > 0) {
            return city;
        }
        return city;
    }

    public void setCity(String city) {
        if (city != null) {
            this.city = city.replaceAll("\\(", "").replaceAll("\\)", "");
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
