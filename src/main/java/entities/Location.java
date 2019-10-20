package entities;

public class Location {
    private long location_id;
    private String location;

    public Location(long location_id, String location) {
        this.location_id = location_id;
        this.location = location;
    }

    public long getLocation_id() {
        return location_id;
    }

    public void setLocation_id(long location_id) {
        this.location_id = location_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
